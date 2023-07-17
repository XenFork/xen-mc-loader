package union.xenfork.xenmc.read;

import cn.hutool.core.io.FileUtil;
import union.xenfork.xenmc.extensions.MinecraftExtension;

import java.util.ArrayList;
import java.util.List;

public class MappingTableToXml {
    private List<String> packages = new ArrayList<>();
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

        for (String line : lines) {
            if (line.startsWith("#")) continue;//对注释进行跳过
            if (line.matches(NAME_LINE)) {
                String[] split = line.split(SPLITTER);
                String clean = internalize(split[0]);// class的原表
                if (packages.stream().filter(s -> s.equals(clean.substring(0, clean.lastIndexOf("/")))).toList().isEmpty()) {
                    packages.add(clean.substring(0, clean.lastIndexOf("/")));
                }
                String obf = internalize(split[1]);// class的映射表
                System.out.println(clean);
                System.out.println(obf);
            }
        }
        for (String line : linesServer) {
            if (line.startsWith("#")) continue;//对注释进行跳过
            if (line.matches(NAME_LINE)) {
                String[] split = line.split(SPLITTER);
                String clean = internalize(split[0]);// class的原表
                if (packages.stream().filter(s -> s.equals(clean.substring(0, clean.lastIndexOf("/")))).toList().isEmpty()) {
                    packages.add(clean.substring(0, clean.lastIndexOf("/")));
                }
                String obf = internalize(split[1]);// class的映射表
                System.out.println(clean);
                System.out.println(obf);
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
}
