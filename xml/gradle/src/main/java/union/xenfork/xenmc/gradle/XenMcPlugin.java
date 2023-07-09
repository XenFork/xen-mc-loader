package union.xenfork.xenmc.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginAware;
import org.jetbrains.annotations.NotNull;
import union.xenfork.xenmc.extensions.MinecraftExtension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class XenMcPlugin implements Plugin<PluginAware> {
    public static final String download_plugin_class = "union.xenfork.xenmc.download.DownloadPlugin";
    public static final String mapping_plugin_class = "union.xenfork.xenmc.mapping.MappingPlugin";
    @Override
    public void apply(@NotNull PluginAware target) {
        MinecraftExtension minecraft;
        if (target instanceof Project project1) {
            minecraft = project1.getExtensions().create("minecraft", MinecraftExtension.class);
            ((Project) target).afterEvaluate(project -> {
                if (minecraft.version == null) throw new NullPointerException("please set minecraft version");
            });
        } else {
            minecraft = null;
        }
        getActions(download_plugin_class).apply(target, minecraft);
        getActions(mapping_plugin_class).apply(target, minecraft);
    }


    BootstrappedPlugin getActions(String name) {
        try {
            return (BootstrappedPlugin) Class.forName(name).getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("fail don't have ", e);
        }
    }
}
