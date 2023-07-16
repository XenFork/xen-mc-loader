package union.xenfork.xenmc.read;

import cn.hutool.core.io.FileUtil;
import union.xenfork.xenmc.extensions.MinecraftExtension;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingTable {

    public final Map<String, String> classObfToCleanMap = new HashMap<>();
    public final Map<String, String> classCleanToObfMap = new HashMap<>();
    public final Map<String, String> fieldObfToCleanMap = new HashMap<>();
    public final Map<String, String> fieldCleanToObfMap = new HashMap<>();
    public final Map<String, String> methodObfToCleanMap = new HashMap<>();
    public final Map<String, String> methodCleanToObfMap = new HashMap<>();

    public MappingTable() {
        String text = FileUtil.readUtf8String(MinecraftExtension.clientMapping);

        String NAME_LINE = "^.+:";
        String SPLITTER = "( |->)+";
        String LINE = "\\r\\n|\\n";

        {
            String[] lines = text.split(LINE);
            for (String line : lines) {
                if (line.startsWith("#")) continue;
                if (line.matches(NAME_LINE)) {
                    String[] split = line.split(SPLITTER);
                    String clean = internalize(split[0]);
                    String obf = internalize(split[1]);
                    classCleanToObfMap.put(clean, obf);
                }
            }
        }

        {
            String[] lines = text.split(LINE);
            String currentObfClass = null;
            String currentCleanClass = null;
            for (String line : lines) {
                if (line.startsWith("#"))
                    continue;

                if (line.matches(NAME_LINE)) {
                    currentObfClass = internalize(line.substring(line.lastIndexOf(" ") + 1, line.indexOf(":")));
                    currentCleanClass = classObfToCleanMap.getOrDefault(currentObfClass, internalize(currentObfClass));
                    continue;
                }

                if (currentObfClass == null)
                    continue;

                if (!line.contains("(")) {
                    //Field
                    String[] split = line.trim().split(SPLITTER);
                    String clean = currentCleanClass + "." + split[1];
                    String obf = currentObfClass + "." + split[2];
                    fieldObfToCleanMap.put(obf, clean);
                    fieldCleanToObfMap.put(clean, obf);
                } else {
                    //Method
                    String[] split = line.contains(":") ? line.substring(line.lastIndexOf(":") + 1).trim().split(SPLITTER) : line.trim().split(SPLITTER);
                    String cleanReturn = notPrimitive(split[0]) ? "L" + internalize(split[0]) + ";" : internalize(split[0]);
                    String cleanName = split[1].substring(0, split[1].lastIndexOf("("));
                    String cleanArgs = split[1].substring(split[1].indexOf("(") + 1, split[1].lastIndexOf(")"));
                    String obfReturn = notPrimitive(split[0]) ? "L" + classCleanToObfMap.getOrDefault(internalize(split[0]), internalize(split[0])) + ";" : cleanReturn;
                    String obfName = split[2];
                    String obfArgs;

                    if (!cleanArgs.equals("")) {
                        StringBuilder tempCleanArs = new StringBuilder();
                        StringBuilder tempObfArs = new StringBuilder();
                        for (String s : cleanArgs.split(",")) {
                            if (notPrimitive(s)) {
                                tempObfArs.append("L").append(classCleanToObfMap.getOrDefault(internalize(s), internalize(s))).append(";");
                                tempCleanArs.append("L").append(internalize(s)).append(";");
                            } else {
                                tempObfArs.append(internalize(s));
                                tempCleanArs.append(internalize(s));
                            }
                        }
                        obfArgs = "(" + tempObfArs + ")";
                        cleanArgs = "(" + tempCleanArs + ")";
                    } else {
                        obfArgs = "()";
                        cleanArgs = "()";
                    }

                    String obf = currentObfClass + "." + obfName + " " + obfArgs + obfReturn;
                    String clean = currentCleanClass + "." + cleanName + " " + cleanArgs + cleanReturn;
                    methodObfToCleanMap.put(obf, clean);
                    methodCleanToObfMap.put(clean, obf);
                }
            }
        }

    }


    private String internalize(String name) {
        return switch (name) {
            case "int" -> "I";
            case "float" -> "F";
            case "double" -> "D";
            case "long" -> "J";
            case "boolean" -> "Z";
            case "short" -> "S";
            case "byte" -> "B";
            case "void" -> "V";
            default -> name.replace('.', '/');
        };
    }

    private boolean notPrimitive(String name) {
        return switch (name) {
            case "int", "float", "double", "long", "boolean", "short", "byte", "void" -> false;
            default -> true;
        };
    }

    public Map<String, String> getMap(boolean clean) {
        Map<String, String> map = new HashMap<>();
        if (clean) {
            map.putAll(classObfToCleanMap);
        } else {
            map.putAll(classCleanToObfMap);
        }

        fieldObfToCleanMap.forEach((k, v) -> {
            String key = clean ? k : v;
            String value = clean ? v : k;
            String obfClassName = key.substring(0, key.lastIndexOf("."));
            String obfFieldName = key.substring(key.lastIndexOf(".") + 1);
            map.put(obfClassName + "." + obfFieldName, value.substring(value.lastIndexOf(".") + 1));
        });

        methodObfToCleanMap.forEach((k, v) -> {
            String key = clean ? k : v;
            String value = clean ? v : k;
            String obfLeft = key.split(" ")[0];
            String obfRight = key.split(" ")[1];
            String cleanLeft = value.split(" ")[0];
            String cleanMethodName = cleanLeft.substring(cleanLeft.lastIndexOf(".") + 1);
            String obfClassName = obfLeft.substring(0, obfLeft.lastIndexOf("."));
            String obfMethodName = obfLeft.substring(obfLeft.lastIndexOf(".") + 1);
            map.put(obfClassName + "." + obfMethodName + obfRight, cleanMethodName);
        });
        return map;
    }
}
