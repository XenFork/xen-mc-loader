package union.xenfork.xenmc.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.plugins.PluginAware;
import org.gradle.internal.impldep.org.apache.ivy.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import union.xenfork.xenmc.download.DownloadPlugin;
import union.xenfork.xenmc.entrypoints.EntryPointsPlugin;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.extensions.XenMcExtension;
import union.xenfork.xenmc.mapping.MappingPlugin;
import union.xenfork.xenmc.remapping.ReMappingPlugin;

import java.io.File;

public class XenMcPlugin implements Plugin<Project> {
    @Override
    public void apply(@NotNull Project target) {
        MinecraftExtension minecraft = target.getExtensions().create("minecraft", MinecraftExtension.class);
        XenMcExtension xenmc = target.getExtensions().create("xenmc", XenMcExtension.class);
        target.afterEvaluate(project -> {
            xenmc.project = project;
            if (xenmc.cacheHome == null) {
                xenmc.cacheHome = new File(project.getGradle().getGradleUserHomeDir(), "caches%sxenmc".formatted(File.separator));
            }
            minecraft.xenmc = xenmc;
            if (minecraft.version == null) throw new NullPointerException("please set minecraft version");
            if (minecraft.xenmc.threadDownloadCount == null) minecraft.xenmc.threadDownloadCount = 20;
            RepositoryHandler repositories = project.getRepositories();
            repositories.maven(maven -> {
                maven.setUrl(minecraft.libraries);
                maven.setName("minecraft libraries maven");
            });
            repositories.maven(maven -> {
                maven.setUrl("https://chinawaremc.github.io/maven-repo/");
                maven.setName("mod loader maven");
            });

            try {
                new DownloadPlugin().apply(project, minecraft);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//            actions(DownloadPlugin.class, project, minecraft);
            actions(ReMappingPlugin.class, project, minecraft);
            actions(EntryPointsPlugin.class, project, minecraft);
            actions(MappingPlugin.class, project, minecraft);
        });

    }

    public void actions(Class<? extends BootstrappedPluginProject> clazz,Project target, MinecraftExtension minecraft) {
        try {
            clazz.getConstructor().newInstance().apply(target, minecraft);
        } catch (Exception e) {
            throw new RuntimeException("fail don't have ", e);
        }
    }
}
