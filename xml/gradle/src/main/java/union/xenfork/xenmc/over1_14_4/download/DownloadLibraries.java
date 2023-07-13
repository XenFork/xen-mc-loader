package union.xenfork.xenmc.over1_14_4.download;

import org.gradle.api.Project;
import org.gradle.internal.os.OperatingSystem;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.over1_14_4.download.minecraft.library.Libraries;
import union.xenfork.xenmc.over1_14_4.download.minecraft.library.RulesNonnull;

public class DownloadLibraries implements BootstrappedPluginProject {
    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        System.out.println(OperatingSystem.current());

        for (Libraries library : MinecraftExtension.versionSet.libraries) {
            var rules = library.rules;
            if (rules != null) {
                for (RulesNonnull rule : rules) {
                    System.out.println(rule);
                    System.out.println(rule.os.name);
                    System.out.println(rule.action);

                }
            }

        }
    }
}
