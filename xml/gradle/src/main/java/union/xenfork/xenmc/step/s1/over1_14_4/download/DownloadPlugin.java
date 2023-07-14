package union.xenfork.xenmc.step.s1.over1_14_4.download;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.jetbrains.annotations.NotNull;
import union.xenfork.xenmc.step.s1.over1_14_4.download.manifest.ManifestGson;
import union.xenfork.xenmc.step.s1.over1_14_4.download.manifest.VersionGson;
import union.xenfork.xenmc.step.s1.over1_14_4.download.minecraft.MinecraftVersionGson;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static cn.hutool.http.HttpDownloader.downloadFile;
import static union.xenfork.xenmc.gradle.Utils.gson;

public class DownloadPlugin implements BootstrappedPluginProject {



    @Override
    public void apply(Project project, @NotNull MinecraftExtension minecraft) throws Exception {
        Utils.setupMessagePrefix(project, minecraft);
        Logger logger = project.getLogger();
        logger.lifecycle("Download");
        MinecraftExtension.manifestFile = new File(minecraft.xenmc.cacheHome, "manifest" + File.separator + "version_manifest.json");

        logger.lifecycle("download {}", minecraft.manifestUrl);
        downloadFile(minecraft.manifestUrl, MinecraftExtension.manifestFile, 3000, new StreamProgressImpl(minecraft.manifestUrl));
        MinecraftExtension.manifest = gson.fromJson(new BufferedReader(new FileReader(MinecraftExtension.manifestFile)), ManifestGson.class);
        MinecraftExtension.versionJsonFile = new File(minecraft.xenmc.cacheHome, "versions" + File.separator + minecraft.version + ".json");
        for (VersionGson version : MinecraftExtension.manifest.versions)
            if (minecraft.version.equals(version.id)) {

                if (!MinecraftExtension.versionJsonFile.exists()) {
                    logger.lifecycle("download {}", version.url);
                    downloadFile(version.url, MinecraftExtension.versionJsonFile, 3000, new StreamProgressImpl(version.url));
                }
            }
        MinecraftExtension.versionSet = gson.fromJson(new BufferedReader(new FileReader(MinecraftExtension.versionJsonFile)), MinecraftVersionGson.class);
        new DownloadAssets().apply(project, minecraft);
        new DownloadGame().apply(project, minecraft);
        new DownloadLibraries().apply(project, minecraft);
        System.out.printf("%.2fs%n", ((double) StreamProgressImpl.usingTime.get()) / 1000);
        System.out.printf("v=%.2fmb/s%n", (((double)StreamProgressImpl.fileSize.get()) / 1024 / 1024) / ((double) StreamProgressImpl.usingTime.get()) / 1000);
    }
}
