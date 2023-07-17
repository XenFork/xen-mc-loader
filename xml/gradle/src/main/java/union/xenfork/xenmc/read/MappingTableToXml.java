package union.xenfork.xenmc.read;

import cn.hutool.core.io.FileUtil;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.util.XenBiMap;

import java.util.*;

public class MappingTableToXml {
    private final List<String> packages = new ArrayList<>();
    private final XenBiMap<String, String> srgMap = new XenBiMap<>(new HashMap<>());
    private final XenBiMap<String, XenBiMap<String, XenBiMap<String, String>>> fieldSrgMap = new XenBiMap<>(new HashMap<>());
    private final XenBiMap<String, XenBiMap<String, XenBiMap<String, XenBiMap<String, ArrayList<String>>>>> methodSrgMap = new XenBiMap<>(new HashMap<>());
    public MappingTableToXml() {
        String text = FileUtil.readUtf8String(MinecraftExtension.clientMapping);
        String textServer = FileUtil.readUtf8String(MinecraftExtension.serverMapping);
        String NAME_LINE = "^.+:";// class区分
        String SPLITTER = "( |->)+";//映射队列区分

        //example
        /*
            net.minecraft.world.ticks.WorldGenTickAccess -> efy:
                java.util.function.Function containerGetter -> a
                10:12:void <init>(java.util.function.Function) -> <init>
                16:16:boolean hasScheduledTick(net.minecraft.core.BlockPos,java.lang.Object) -> a
                21:22:void schedule(net.minecraft.world.ticks.ScheduledTick) -> a
                26:26:boolean willTickThisTick(net.minecraft.core.BlockPos,java.lang.Object) -> b
                32:32:int count() -> a
         */
        String LINE = "\\r\\n|\\n";//多行模式
        String[] lines = text.split(LINE);
        String[] linesServer = textServer.split(LINE);

        step1(lines);
        step1(linesServer);
        step2(lines);
        step2(linesServer);


//        for (Map.Entry<String, String> stringStringEntry : srgMap) {
//            System.out.println(stringStringEntry.getKey() + ":" + stringStringEntry.getValue());
//        }
//        fieldSrgMap.forEach(stringXenBiMapEntry -> {
//            stringXenBiMapEntry.getValue().forEach(stringStringEntry -> {
//                System.out.println(stringStringEntry.getKey() + ":" + stringStringEntry.getValue());
//            });
//        });
//        for (Map.Entry<String, XenBiMap<String, XenBiMap<String, XenBiMap<String, ArrayList<String>>>>> stringXenBiMapEntry : methodSrgMap) {
//            for (Map.Entry<String, XenBiMap<String, XenBiMap<String, ArrayList<String>>>> xenBiMapEntry : stringXenBiMapEntry.getValue()) {
//                for (Map.Entry<String, XenBiMap<String, ArrayList<String>>> biMapEntry : xenBiMapEntry.getValue()) {
//                    for (Map.Entry<String, ArrayList<String>> stringArrayListEntry : biMapEntry.getValue()) {
//                        StringBuilder sb = new StringBuilder();
//                        for (String s : stringArrayListEntry.getValue()) {
//                            sb.append(s).append(",");
//                        }
//                        System.out.println(stringXenBiMapEntry.getKey() + ":" + xenBiMapEntry.getKey() + ":" + biMapEntry.getKey() + ":" + stringArrayListEntry.getKey() + ":" + sb);
//                    }
//                }
//            }
//        }
    }

    private void step2(String[] lines) {
        String NAME_LINE = "^.+:";// class区分
        String SPLITTER = "( |->)+";//映射队列区分
        String currentObfClass = null;
        String currentCleanClass = null;
        for (String line : lines) {
            if (line.startsWith("#"))
                continue;

            if (line.matches(NAME_LINE)) {
                currentObfClass = internalize(line.substring(line.lastIndexOf(" ") + 1, line.indexOf(":")));
                currentCleanClass = srgMap.getKeyOrDefault(currentObfClass, internalize(currentObfClass));
                continue;
            }

            if (currentObfClass == null)
                continue;

            if (!line.contains("(")) {
                //Field
                XenBiMap<String, XenBiMap<String, String>> fieldEntry = fieldSrgMap.containsKey(currentCleanClass) ? fieldSrgMap.get(currentCleanClass) : new XenBiMap<>(new HashMap<>());
                String[] split = line.trim().split(SPLITTER);
                String returnType = split[0];
                String clean = split[1];
                String obf = split[2];
                XenBiMap<String, String> cleanReturn = fieldEntry.containsKey(obf) ? fieldEntry.get(obf) : new XenBiMap<>(new HashMap<>());
                cleanReturn.put(clean, returnType);
                fieldEntry.put(obf, cleanReturn);
                fieldSrgMap.put(currentCleanClass, fieldEntry);
            } else {
                //Method
                String[] split = line.contains(":") ? line.substring(line.lastIndexOf(":") + 1).trim().split(SPLITTER) : line.trim().split(SPLITTER);
                String cleanReturn = notPrimitive(split[0]) ? "L" + internalize(split[0]) + ";" : internalize(split[0]);
                String cleanName = split[1].substring(0, split[1].lastIndexOf("("));
                String cleanArgs = split[1].substring(split[1].indexOf("(") + 1, split[1].lastIndexOf(")"));
                String obfReturn = notPrimitive(split[0]) ? "L" + srgMap.getOrDefault(internalize(split[0]), internalize(split[0])) + ";" : cleanReturn;
                String obfName = split[2];
                XenBiMap<String, XenBiMap<String, XenBiMap<String, ArrayList<String>>>> entries =
                        methodSrgMap.containsKey(currentCleanClass) ?
                                methodSrgMap.get(currentCleanClass) :
                                new XenBiMap<>(new HashMap<>());
                XenBiMap<String, XenBiMap<String, ArrayList<String>>> cleanReturnArgs = entries.containsKey(obfName) ? entries.get(obfName) : new XenBiMap<>(new HashMap<>());
                XenBiMap<String, ArrayList<String>> returnArgs = cleanReturnArgs.containsKey(cleanName) ? cleanReturnArgs.get(cleanName) : new XenBiMap<>(new HashMap<>());
                ArrayList<String> args = returnArgs.containsKey(obfReturn) ? returnArgs.get(obfReturn) : new ArrayList<>(Arrays.stream(cleanArgs.split(",")).toList());
                returnArgs.put(obfReturn, args);
                cleanReturnArgs.put(cleanName, returnArgs);
                entries.put(obfName, cleanReturnArgs);
                methodSrgMap.put(currentCleanClass, entries);
//                System.out.println(cleanName + ":" + obfName);

            }
        }
    }

    private void step1(String[] lines) {
        String NAME_LINE = "^.+:";// class区分
        String SPLITTER = "( |->)+";//映射队列区分
        for (String line : lines) {
            if (line.startsWith("#")) continue;//对注释进行跳过
            if (line.matches(NAME_LINE)) {
                String[] split = line.split(SPLITTER);
                String clean = internalize(split[0]);// class的原表
                if (packages.stream().filter(s -> s.equals(clean.substring(0, clean.lastIndexOf("/")))).toList().isEmpty()) {
                    packages.add(clean.substring(0, clean.lastIndexOf("/")));
                }

                String obf = internalize(split[1]).replace(":", "");// class的映射表
                srgMap.put(clean, obf);
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

    private boolean notPrimitive(String name) {//判断否是primitive type类型
        return switch (name) {
            case "int", "float", "double", "long", "boolean", "short", "byte", "void" -> false;
            default -> true;
        };
    }

    public List<String> packages() {
        return new ArrayList<>(packages);
    }

    public XenBiMap<String, String> getSrgMap() {
        return new XenBiMap<>(srgMap);
    }

    public XenBiMap<String, XenBiMap<String, XenBiMap<String, String>>> fieldSrgMap() {
        return new XenBiMap<>(fieldSrgMap);
    }

    public XenBiMap<String, XenBiMap<String, XenBiMap<String, XenBiMap<String, ArrayList<String>>>>> methodSrgMap() {
        return new XenBiMap<>(methodSrgMap);
    }
}
