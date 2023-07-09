package union.xenfork.xenmc.gradle;

import org.gradle.api.plugins.PluginAware;
import union.xenfork.xenmc.extensions.MinecraftExtension;

public interface BootstrappedPlugin {
    void apply(PluginAware target, MinecraftExtension minecraft);
}
