package union.xenfork.xenmc.step.s3.remapping;

import org.gradle.api.Project;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;
import union.xenfork.xenmc.read.ClassReader;

import java.io.File;

public class ReMappingPlugin implements BootstrappedPluginProject {

    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        Utils.setupMessagePrefix(project, minecraft);
        project.getLogger().lifecycle("remapping minecraft");
        if (minecraft.xenmc.getIsLoader()) {

        }
//        ClassReader classReader = new ClassReader(MinecraftExtension.clientGame.toPath(), minecraft.xenmc.projectHome.toPath().resolve("src/main/java"));
//        classReader.cfr();
    }
}
