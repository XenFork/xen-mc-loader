package union.xenfork.xenmc.step.s2.remapping;

import cn.hutool.core.util.XmlUtil;
import org.gradle.api.Project;
import org.w3c.dom.*;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.read.ConfusionWidener;
import union.xenfork.xenmc.read.ConfusionWidenerMapping;
import union.xenfork.xenmc.read.Mapping;
import union.xenfork.xenmc.read.ReMapping;
import union.xenfork.xenmc.util.Utils;

import java.io.File;

public class ReMappingPlugin implements BootstrappedPluginProject {

    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        Utils.setupMessagePrefix(project, minecraft);
        project.getLogger().lifecycle("remapping minecraft");
        MinecraftExtension.clientRemapJar = new File(MinecraftExtension.clientGame.getParent(), minecraft.version + "-client-remap.jar");
        MinecraftExtension.serverRemapJar = new File(MinecraftExtension.clientGame.getParent(), minecraft.version + "-server-remap.jar");
        Mapping instance = Mapping.getInstance(MinecraftExtension.clientMapping);
        ReMapping reMapping = ReMapping.getInstance("remapping", instance.getMap(false));
        ConfusionWidener confusionWidener = new ConfusionWidener(minecraft);
        ConfusionWidenerMapping map = ConfusionWidenerMapping.getInstance("rename");

//        reMapping.assayJar(MinecraftExtension.clientGame);
        reMapping.remappingJar(MinecraftExtension.clientGame, MinecraftExtension.clientRemapJar);
        reMapping.remappingJar(MinecraftExtension.serverGame, MinecraftExtension.serverRemapJar);
        map.assayJar(MinecraftExtension.clientRemapJar);
//        ClassReader classReader = new ClassReader(MinecraftExtension.clientGame.toPath(), minecraft.xenmc.projectHome.toPath().resolve("src/main/java"));
//        classReader.cfr();
    }
}
