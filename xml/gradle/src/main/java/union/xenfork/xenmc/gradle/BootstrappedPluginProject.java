package union.xenfork.xenmc.gradle;

import org.gradle.api.Project;
import union.xenfork.xenmc.extensions.MinecraftExtension;

public interface BootstrappedPluginProject {
    void apply(Project project, MinecraftExtension minecraft) throws Exception;
}
