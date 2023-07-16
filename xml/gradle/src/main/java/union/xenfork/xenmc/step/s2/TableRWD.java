package union.xenfork.xenmc.step.s2;

import cn.hutool.core.text.csv.CsvWriter;
import org.gradle.api.Project;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;
import union.xenfork.xenmc.read.ClassReader;
import union.xenfork.xenmc.read.MappingTable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static java.io.File.separator;

public class TableRWD implements BootstrappedPluginProject {
    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        if (minecraft.xenmc.remapTypesDir == null) {
            minecraft.xenmc.remapTypesDir = new File(minecraft.xenmc.cacheHome, "remapof" + separator + minecraft.version);
        }
        Utils.setupMessagePrefix(project, minecraft);
//        new ClassReader(MinecraftExtension.clientGame.toPath(), new File(MinecraftExtension.game, "client").toPath()).cfr();

        CsvWriter writer;
        File file = new File(minecraft.xenmc.remapTypesDir, "xenmc.csv");
        writer = new CsvWriter(file);
        Map<String, String> map = new MappingTable().getMap(true);
        Map<String, String> result = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
        result.forEach((s, s2) -> {
            String[] split = s.split("\\.");
            writer.write(new String[] {split[0], split[1], s2});
        });
        writer.close();
//        if (minecraft.xenmc.remapTypesOf.equals("csv")) {
//
//        }

    }
}
