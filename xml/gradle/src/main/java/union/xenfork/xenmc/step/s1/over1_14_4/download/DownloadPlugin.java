package union.xenfork.xenmc.step.s1.over1_14_4.download;

import cn.hutool.http.HttpDownloader;
import com.google.gson.reflect.TypeToken;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.jetbrains.annotations.NotNull;
import union.xenfork.xenmc.download.thread.Downloads;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;
import union.xenfork.xenmc.step.s1.over1_14_4.download.assets.Something;
import union.xenfork.xenmc.step.s1.over1_14_4.download.manifest.ManifestGson;
import union.xenfork.xenmc.step.s1.over1_14_4.download.manifest.VersionGson;
import union.xenfork.xenmc.step.s1.over1_14_4.download.minecraft.MinecraftVersionGson;
import union.xenfork.xenmc.step.s1.over1_14_4.download.minecraft.library.Libraries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static cn.hutool.http.HttpDownloader.downloadFile;
import static union.xenfork.xenmc.gradle.Utils.gson;

public class DownloadPlugin implements BootstrappedPluginProject {
    public static AtomicBoolean preStartDownload = new AtomicBoolean(false);//判断是否进行过setupMessagePrefix()
    private static void extracted(Logger logger, @NotNull MinecraftExtension minecraft) {
        Utils.setupMessagePrefix(logger, minecraft);
        logger.lifecycle("Download");
        preStartDownload.set(true);
    }
    public void downloadManifest(Logger logger, @NotNull MinecraftExtension minecraft) {
        if (!preStartDownload.get()) extracted(logger, minecraft);
        logger.lifecycle("download {}", minecraft.manifestUrl);
        downloadFile(minecraft.manifestUrl, MinecraftExtension.manifestFile, 3000, new StreamProgressImpl(minecraft.manifestUrl));
    }

    public void downloadVersionSelect(Logger logger, @NotNull MinecraftExtension minecraft) {
        VersionGson version = MinecraftExtension.manifest.versions.stream().filter(versionGson -> versionGson.id.equals(minecraft.version)).toList().get(0);
        logger.lifecycle("download {}", version.url);
        downloadFile(version.url, MinecraftExtension.versionJsonFile, 3000, new StreamProgressImpl(version.url));
    }

    public void downloadAssetsIndex(Logger logger) {
        logger.lifecycle("download {}", MinecraftExtension.versionSet.assetIndex.url);
        HttpDownloader.downloadFile(MinecraftExtension.versionSet.assetIndex.url, MinecraftExtension.assetsIndexFile, 3000, new StreamProgressImpl(MinecraftExtension.versionSet.assetIndex.url));
    }

    public void downloadClientGame(Logger logger, MinecraftExtension minecraft) {
        if (!preStartDownload.get()) {
            Utils.setupMessagePrefix(logger, minecraft);
            preStartDownload.set(true);
        }
        logger.lifecycle("download {}", MinecraftExtension.versionSet.downloads.client.url);
        HttpDownloader.downloadFile(MinecraftExtension.versionSet.downloads.client.url, MinecraftExtension.clientGame, 3000, new StreamProgressImpl(MinecraftExtension.versionSet.downloads.client.url));
        if (!Utils.isSha1(MinecraftExtension.clientGame, MinecraftExtension.versionSet.downloads.client.sha1)) {
            MinecraftExtension.clientGame.delete();
            throw new RuntimeException("fail to verify client game");
        }
    }

    public void downloadServerGame(Logger logger, MinecraftExtension minecraft) {
        if (!preStartDownload.get()) {
            Utils.setupMessagePrefix(logger, minecraft);
            preStartDownload.set(true);
        }
        logger.lifecycle("download {}", MinecraftExtension.versionSet.downloads.server.url);
        HttpDownloader.downloadFile(MinecraftExtension.versionSet.downloads.server.url, MinecraftExtension.serverGame, 3000, new StreamProgressImpl(MinecraftExtension.versionSet.downloads.server.url));
        if (!Utils.isSha1(MinecraftExtension.serverGame, MinecraftExtension.versionSet.downloads.server.sha1)) {
            MinecraftExtension.serverGame.delete();
            throw new RuntimeException("fail to verify server game");
        }
    }

    public void downloadClientMapping(Logger logger, MinecraftExtension minecraft) {
        if (!preStartDownload.get()) {
            Utils.setupMessagePrefix(logger, minecraft);
            preStartDownload.set(true);
        }
        logger.lifecycle("download {}", MinecraftExtension.versionSet.downloads.client_mappings.url);
        HttpDownloader.downloadFile(MinecraftExtension.versionSet.downloads.client_mappings.url, MinecraftExtension.clientMapping, 3000, new StreamProgressImpl(MinecraftExtension.versionSet.downloads.client_mappings.url));
        if (!Utils.isSha1(MinecraftExtension.clientMapping, MinecraftExtension.versionSet.downloads.client_mappings.sha1)) {
            MinecraftExtension.clientMapping.delete();
            throw new RuntimeException("fail to verify client mapping");
        }
    }

    public void downloadServerMapping(Logger logger, MinecraftExtension minecraft) {
        if (!preStartDownload.get()) {
            Utils.setupMessagePrefix(logger, minecraft);
            preStartDownload.set(true);
        }
        logger.lifecycle("download {}", MinecraftExtension.versionSet.downloads.server_mappings.url);
        HttpDownloader.downloadFile(MinecraftExtension.versionSet.downloads.server_mappings.url, MinecraftExtension.serverMapping, 3000, new StreamProgressImpl(MinecraftExtension.versionSet.downloads.server_mappings.url));
        if (!Utils.isSha1(MinecraftExtension.serverMapping, MinecraftExtension.versionSet.downloads.server_mappings.sha1)) {
            MinecraftExtension.serverMapping.delete();
            throw new RuntimeException("fail to verify server mapping");
        }
    }

    @Override
    public void apply(Project project, @NotNull MinecraftExtension minecraft) throws Exception {
        Logger logger = project.getLogger();
        MinecraftExtension.manifestFile = new File(minecraft.xenmc.cacheHome, "manifest" + File.separator + "version_manifest.json");

        MinecraftExtension.assetsDir = new File(minecraft.xenmc.cacheHome, "assets");

        MinecraftExtension.assetsObjectsDir = new File(MinecraftExtension.assetsDir, "objects");
        MinecraftExtension.assetsVirtual = new File(MinecraftExtension.assetsDir, "virtual");
        MinecraftExtension.game = new File(minecraft.xenmc.cacheHome, "game");
        MinecraftExtension.mapping = new File(minecraft.xenmc.cacheHome, "mapping");

        MinecraftExtension.librariesDir = new File(minecraft.xenmc.cacheHome, "libraries");
        if (!MinecraftExtension.manifestFile.exists())
            downloadManifest(logger, minecraft);
        MinecraftExtension.manifest = gson.fromJson(new BufferedReader(new FileReader(MinecraftExtension.manifestFile)), ManifestGson.class);
        if (minecraft.version == null) {
            minecraft.version = MinecraftExtension.manifest.latest.release;
        }
        MinecraftExtension.clientGame = new File(MinecraftExtension.game, minecraft.version + File.separator + minecraft.version + "-client.jar");
        MinecraftExtension.clientMapping = new File(MinecraftExtension.mapping, minecraft.version + File.separator + minecraft.version + "-client.txt");
        MinecraftExtension.serverGame = new File(MinecraftExtension.game,minecraft.version + File.separator + minecraft.version + "-server.jar");
        MinecraftExtension.serverMapping = new File(MinecraftExtension.mapping,minecraft.version + File.separator + minecraft.version + "-server.txt");
        MinecraftExtension.versionJsonFile = new File(minecraft.xenmc.cacheHome, "versions" + File.separator + minecraft.version + ".json");
        if (MinecraftExtension.manifest.versions.stream().filter(versionGson -> versionGson.id.equals(minecraft.version)).toList().isEmpty()) {
            throw new RuntimeException("The version you described does not exist");
        }
        if (!MinecraftExtension.versionJsonFile.exists()) {
            downloadVersionSelect(logger, minecraft);
        }
        MinecraftExtension.versionSet = gson.fromJson(new BufferedReader(new FileReader(MinecraftExtension.versionJsonFile)), MinecraftVersionGson.class);
        MinecraftExtension.assetsIndexFile = new File(MinecraftExtension.assetsDir, "indexes" + File.separator + MinecraftExtension.versionSet.id + ".json");
        if (!MinecraftExtension.assetsIndexFile.exists()) {
            downloadAssetsIndex(logger);
        }


        TypeToken<Map<String, Map<String, Something>>> token = new TypeToken<>() {};
        Map<String, Map<String, Something>> assetsLoader = gson.fromJson(new BufferedReader(new FileReader(MinecraftExtension.assetsIndexFile)), token.getType());
        Downloads.downloads(assetsLoader, minecraft.assets, MinecraftExtension.assetsObjectsDir, minecraft.xenmc.threadDownloadCount, project.getLogger(), minecraft);

        if (!Utils.isSha1(MinecraftExtension.clientGame, MinecraftExtension.versionSet.downloads.client.sha1)) {
            downloadClientGame(logger, minecraft);
        }
        if (!Utils.isSha1(MinecraftExtension.serverGame, MinecraftExtension.versionSet.downloads.server.sha1)) {
            downloadServerGame(logger, minecraft);
        }
        if (!Utils.isSha1(MinecraftExtension.clientMapping, MinecraftExtension.versionSet.downloads.client_mappings.sha1)) {
            downloadClientMapping(logger, minecraft);
        }

        if (!Utils.isSha1(MinecraftExtension.serverMapping, MinecraftExtension.versionSet.downloads.server_mappings.sha1)) {
            downloadServerMapping(logger, minecraft);
        }
        for (Libraries library : MinecraftExtension.versionSet.libraries) {
            File namePath = new File(MinecraftExtension.librariesDir, library.downloads.artifact.path.replace("/", File.separator));
            if (!namePath.exists()) {
                if (!preStartDownload.get()) {
                    Utils.setupMessagePrefix(logger, minecraft);
                    preStartDownload.set(true);
                }
                logger.lifecycle("download {}", library.downloads.artifact.url);
                downloadFile(library.downloads.artifact.url, namePath, 3000, new StreamProgressImpl(library.downloads.artifact.url));
            }
            MinecraftExtension.librariesPaths.add(namePath);
        }
        if (preStartDownload.get()) {
            System.out.printf("%.2fs%n", ((double) StreamProgressImpl.usingTime.get()) / 1000);
        }

    }
}
