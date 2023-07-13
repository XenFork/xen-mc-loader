package union.xenfork.xenmc.over1_14_4.download;

import cn.hutool.http.HttpDownloader;
import org.gradle.api.Project;
import org.gradle.internal.os.OperatingSystem;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.over1_14_4.download.minecraft.MinecraftVersionGson;
import union.xenfork.xenmc.over1_14_4.download.minecraft.library.Libraries;
import union.xenfork.xenmc.over1_14_4.download.minecraft.library.RulesNonnull;

import java.io.File;

import static cn.hutool.http.HttpDownloader.downloadFile;

public class DownloadLibraries implements BootstrappedPluginProject {
    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        System.out.println(OperatingSystem.current());
        MinecraftExtension.librariesDir = new File(minecraft.xenmc.cacheHome, "libraries");

        for (Libraries library : MinecraftExtension.versionSet.libraries) {

            File namePath = new File(MinecraftExtension.librariesDir, library.downloads.artifact.path.replace("/", File.separator));
            if (!namePath.exists()) {
                downloadFile(library.downloads.artifact.url, namePath, 3000, new StreamProgressImpl(library.downloads.artifact.url));
            }
            MinecraftExtension.librariesPaths.add(namePath);
//            var rules = library.rules;
//
//            if (rules != null) {
//                for (RulesNonnull rule : rules) {
//                    System.out.println(rule);
//                    System.out.println(rule.os.name);
//                    System.out.println(rule.action);
//                }
//            }

        }
    }
}
