package union.xenfork.xenmc.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.plugins.PluginAware;
import org.jetbrains.annotations.NotNull;
import union.xenfork.xenmc.download.DownloadPlugin;
import union.xenfork.xenmc.entrypoints.EntryPointsPlugin;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.mapping.MappingPlugin;
import union.xenfork.xenmc.remapping.ReMappingPlugin;

public class XenMcPlugin implements Plugin<PluginAware> {
    @Override
    public void apply(@NotNull PluginAware target) {
        MinecraftExtension minecraft;
        if (target instanceof Project project1) {
            minecraft = project1.getExtensions().create("minecraft", MinecraftExtension.class);
            project1.afterEvaluate(project -> {
                if (minecraft.version == null) throw new NullPointerException("please set minecraft version");
                RepositoryHandler repositories = project.getRepositories();
                repositories.maven(maven -> {
                    maven.setUrl(minecraft.libraries);
                    maven.setName("minecraft libraries maven");
                });
                repositories.maven(maven -> {
                    maven.setUrl("https://chinawaremc.github.io/maven-repo/");
                    maven.setName("mod loader maven");
                });
            });
        } else {
            minecraft = null;
        }
        actions(DownloadPlugin.class, target, minecraft);
        actions(ReMappingPlugin.class, target, minecraft);
        actions(EntryPointsPlugin.class, target, minecraft);
        actions(MappingPlugin.class, target, minecraft);
    }

    public void actions(Class<? extends BootstrappedPlugin> clazz,PluginAware target, MinecraftExtension minecraft) {
        try {
            clazz.getConstructor().newInstance().apply(target, minecraft);
        } catch (Exception e) {
            throw new RuntimeException("fail don't have ", e);
        }
    }
}
