package union.xenfork.xenmc.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
import union.xenfork.xenmc.gradle.task.*;
import union.xenfork.xenmc.gradle.util.mc.LibrariesUtil;

import static union.xenfork.xenmc.gradle.util.mc.MinecraftImpl.getClientCleanFile;
import static union.xenfork.xenmc.gradle.util.mc.MinecraftImpl.getClientFile;

public class XenMcGradlePlugin implements Plugin<Project> {
    @Override
    public void apply(@NotNull Project target) {
        XenMcGradleExtension extension = target.getExtensions().create("xenmc", XenMcGradleExtension.class, target);
        target.afterEvaluate(project -> {
            isNull(extension.minecraft.version, "minecraft version is null");
            isNull(extension.asmVersion, "asm is not have" + extension.asmVersion);
            project.getRepositories().maven(mavenArtifactRepository -> {
               mavenArtifactRepository.setName("minecraft libraries");
               mavenArtifactRepository.setUrl(extension.minecraft.libraries);
            });
            project.getRepositories().maven(mavenArtifactRepository -> {
                mavenArtifactRepository.setName("sponge mixin and other");
                mavenArtifactRepository.setUrl("https://repo.spongepowered.org/repository/maven-public/");
            });

            project.getRepositories().mavenCentral();
            project.getRepositories().mavenLocal();

            for (String library : LibrariesUtil.getLibraries(extension)) {
                project.getDependencies().add("implementation", library);
            }

            project.getPlugins().apply("java");
            project.getPlugins().apply("idea");

            project.getTasks().create("downloadMapping", DownloadMapping.class);
            project.getTasks().create("downloadClient", DownloadClientGame.class);
            project.getTasks().create("downloadAssets", DownloadAssets.class);
            project.getTasks().create("deobf", Deobf.class);
            project.getTasks().create("obf", Obf.class);
            project.getTasks().create("genIdeaRunConfiguration", GenerateIdeaRun.class);
            project.getTasks().getByName("idea")
                            .finalizedBy(
                                    project.getTasks().getByName("downloadMapping"),
                                    project.getTasks().getByName("downloadClient"),
                                    project.getTasks().getByName("downloadAssets"),
                                    project.getTasks().getByName("deobf"),
                                    project.getTasks().getByName("genIdeaRunConfiguration")
                            );

            dep(project, "org.ow2.asm", extension.asmVersion,
                    "asm", "asm-analysis", "asm-commons", "asm-tree", "asm-util"
            );

            project.getTasks().getByName("compileJava").finalizedBy(project.getTasks().getByName("obf"));

            project.getDependencies().add("compileOnly", project.getDependencies().create(project.files(getClientCleanFile(extension).getAbsolutePath())));
            project.getDependencies().add("runtimeOnly", project.getDependencies().create(project.files(getClientFile(extension).getAbsolutePath())));

        });
    }

    public void isNull(Object obj, String message) {
        if (obj == null) throw new NullPointerException(message);
    }

    public void dep(Project project, String depPre, String depVersion, String... depModule) {
        if (depModule.length == 1)
            dep(project, depPre, depVersion, depModule[0]);
        else for (String module : depModule)
            dep(project, depPre, depVersion, module);
    }

    public void dep(Project project, String depPre, String depVersion, String depModule) {
        project.getDependencies().add("implementation", "%s:%s:%s".formatted(depPre, depModule, depVersion));
    }
}


