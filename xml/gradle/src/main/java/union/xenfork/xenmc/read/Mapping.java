package union.xenfork.xenmc.read;

import cn.hutool.core.io.FileUtil;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.util.XenBiMap;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Mapping {
    public static final String NAME_LINE = "^.+:";
    public static final String SPLITTER = "( |->)+";
    public static final String LINE = "\\r\\n|\\n";

    private static Mapping instance;

    public static Mapping getInstance(File file) {
        if (instance == null) {
            instance = new Mapping(file);
        }
        return instance;
    }

    /**
     * @since 双向查找map
     */
    public final XenBiMap<String, String> classNatureObfMap = new XenBiMap<>(new HashMap<>());
    public final XenBiMap<String, String> fieldNatureObfMap = new XenBiMap<>(new HashMap<>());
    public final XenBiMap<String, String> methodNatureObfMap = new XenBiMap<>(new HashMap<>());

    private Mapping(File file) {
        String text = FileUtil.readUtf8String(file);

        {
            String[] lines = text.split(LINE);
            for (String line : lines) {
                if (line.startsWith("#"))
                    continue;
                if (line.matches(NAME_LINE)) {
                    String[] split = line.split(SPLITTER);
                    String nature = internalize(split[0]);
                    String obf = internalize(split[1]);
                    obf = obf.substring(0, obf.indexOf(":"));
                    classNatureObfMap.put(nature, obf);
                }
            }
        }

        {
            String[] lines = text.split(LINE);
            String currentObfClass = null;
            String currentNatureClass = null;
            for (String line : lines) {
                if (line.startsWith("#"))
                    continue;
                if (line.matches(NAME_LINE)) {
                    currentObfClass = internalize(line.substring(line.lastIndexOf(" ") + 1, line.indexOf(":")));
                    currentNatureClass = classNatureObfMap.getKeyOrDefault(currentObfClass, internalize(currentObfClass));
                    continue;
                }

                if (currentObfClass == null)
                    continue;

                if (!line.contains("(")) {
                    //Field
                    String[] split = line.trim().split(SPLITTER);
                    String nature = currentNatureClass + "." + split[1];
                    String obf = currentObfClass + "." + split[2];
                    fieldNatureObfMap.put(nature, obf);
                } else {
                    //Method
                    String[] split = line.contains(":") ? line.substring(line.lastIndexOf(":") + 1).trim().split(SPLITTER) : line.trim().split(SPLITTER);
                    String natureReturn = notPrimitive(split[0]) ? "L" + internalize(split[0]) + ";" : internalize(split[0]);
                    String natureName = split[1].substring(0, split[1].lastIndexOf("("));
                    String natureArgs = split[1].substring(split[1].indexOf("(") + 1, split[1].lastIndexOf(")"));
                    String obfReturn = notPrimitive(split[0]) ? "L" + classNatureObfMap.getOrDefault(internalize(split[0]), internalize(split[0])) + ";" : natureReturn;
                    String obfName = split[2];
                    String obfArgs;

                    if (!natureArgs.equals("")) {
                        StringBuilder tempNatureArs = new StringBuilder();
                        StringBuilder tempObfArs = new StringBuilder();
                        for (String s : natureArgs.split(",")) {
                            if (notPrimitive(s)) {
                                tempObfArs.append("L").append(classNatureObfMap.getOrDefault(internalize(s), internalize(s))).append(";");
                                tempNatureArs.append("L").append(internalize(s)).append(";");
                            } else {
                                tempObfArs.append(internalize(s));
                                tempNatureArs.append(internalize(s));
                            }
                        }
                        obfArgs = "(" + tempObfArs + ")";
                        natureArgs = "(" + tempNatureArs + ")";
                    } else {
                        obfArgs = "()";
                        natureArgs = "()";
                    }

                    String obf = currentObfClass + "." + obfName + " " + obfArgs + obfReturn;
                    String nature = currentNatureClass + "." + natureName + " " + natureArgs + natureReturn;
                    methodNatureObfMap.put(nature, obf);
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

    public Map<String, String> getMap(boolean nature) {
        Map<String, String> map = new HashMap<>();
        if (nature) {
            map.putAll(classNatureObfMap.getInverse());
        } else {
            map.putAll(classNatureObfMap);
        }
        fieldNatureObfMap.forEach((k, v) -> {
            String key = nature ? v : k;
            String value = nature ? k : v;
            String obfClassName = key.substring(0, key.lastIndexOf("."));
            String obfFieldName = key.substring(key.lastIndexOf(".") + 1);
            map.put(obfClassName + "." + obfFieldName, value.substring(value.lastIndexOf(".") + 1));
        });

        methodNatureObfMap.forEach((k, v) -> {
            String key = nature ? v : k;
            String value = nature ? k : v;
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
