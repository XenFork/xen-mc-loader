package union.xenfork.xenmc.download;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
import union.xenfork.xenmc.download.manifest.ManifestGson;
import union.xenfork.xenmc.download.manifest.VersionGson;
import union.xenfork.xenmc.download.minecraft.MinecraftVersionGson;
import union.xenfork.xenmc.download.thread.Downloader;
import union.xenfork.xenmc.download.thread.Downloads;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static union.xenfork.xenmc.gradle.Utils.*;

public class DownloadPlugin implements BootstrappedPluginProject {



    @Override
    public void apply(Project project, @NotNull MinecraftExtension minecraft) throws Exception {
        Utils.setupMessagePrefix(project, minecraft);
        project.getLogger().lifecycle("Download");
        MinecraftExtension.manifestFile = new File(minecraft.xenmc.cacheHome, "manifest" + File.separator + "version_manifest.json");
        download(MinecraftExtension.manifestFile.getParentFile(), minecraft.xenmc.threadDownloadCount, minecraft.manifestUrl);
        MinecraftExtension.versionJsonFile = new File(minecraft.xenmc.cacheHome, "versions" + File.separator + minecraft.version + ".json");
        serializable(MinecraftExtension.manifestFile, tempTranslate);
        MinecraftExtension.manifest = (gson.fromJson(new BufferedReader(new FileReader(MinecraftExtension.manifestFile)), ManifestGson.class));
        for (VersionGson version : MinecraftExtension.manifest.versions) {
            if (minecraft.version.equals(version.id)) {
                Utils.download(MinecraftExtension.versionJsonFile, minecraft.xenmc.threadDownloadCount, version.url);
//                if (!MinecraftExtension.versionJsonFile.exists()) {
//                    download(MinecraftExtension.versionJsonFile.getParentFile(), minecraft.xenmc.threadDownloadCount, version.url);
//                } else {
//                    RandomAccessFile rwd = new RandomAccessFile(MinecraftExtension.versionJsonFile, "rwd");
//                    HttpURLConnection conn = (HttpURLConnection) new URL(version.url).openConnection();
//                    conn.setConnectTimeout(3000);
//                    conn.setRequestMethod("HEAD");
//                    conn.connect();
//                    if (!(rwd.length() == conn.getContentLengthLong())) {
//                        rwd.close();
//                        conn.disconnect();
//                        download(MinecraftExtension.versionJsonFile.getParentFile(), minecraft.xenmc.threadDownloadCount, version.url);
//                    }
//                }
                serializable(MinecraftExtension.versionJsonFile, tempTranslate);
                break;
            }
        }
        MinecraftExtension.versionSet = gson.fromJson(new BufferedReader(new FileReader(MinecraftExtension.versionJsonFile)), MinecraftVersionGson.class);
        new DownloadAssets().apply(project, minecraft);
        System.out.println(MinecraftExtension.versionSet.mainClass);
    }



    /**
     * @apiNote download thread
     * @param dir dir
     * @param url link
     */
    public static void download(File dir, String url) {
        Downloader downloader = new Downloader(url, dir);
        downloader.start();
        downloader.isDone();
    }

    public static void download(File dir, int thread, String url) {
        Downloader downloader = new Downloader(url, thread, dir);
        downloader.start();
        downloader.isDone();
    }

    public static void downloads(File dir, String... url) {
        Downloads downloads = new Downloads(dir, url);
        downloads.downloads();
        downloads.isDone();
    }

    public static void downloads(File dir, int thread, String... url) {
        Downloads downloads = new Downloads(dir, thread, url);
        downloads.downloads();
        downloads.isDone();
    }

    public static void downloads(Downloader... downloader) {
        Downloads downloads = new Downloads();
        for (Downloader downloader1 : downloader) {
            downloads.add(downloader1.getDir(), downloader1.getThreadCount(), downloader1.getUrl());
        }
        downloads.downloads();
        downloads.isDone();
    }
}
