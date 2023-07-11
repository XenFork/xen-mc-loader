package union.xenfork.xenmc.download;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
import union.xenfork.xenmc.download.manifest.ManifestGson;
import union.xenfork.xenmc.download.manifest.VersionGson;
import union.xenfork.xenmc.download.thread.Downloader;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class DownloadPlugin implements BootstrappedPluginProject {

    public static final File tempTranslate = new File(System.getProperty("user.dir"), ".gradle" + File.separator + "t.txt");

    @Override
    public void apply(Project project, @NotNull MinecraftExtension minecraft) throws Exception {
        Utils.setupMessagePrefix(project, minecraft);
        project.getLogger().lifecycle("Download");
        minecraft.manifestFile = new File(minecraft.xenmc.cacheHome, "manifest" + File.separator + "version_manifest.json");
        download(minecraft.manifestFile.getParentFile(), minecraft.xenmc.threadDownloadCount, minecraft.manifestUrl);
        minecraft.versionJsonFile = new File(minecraft.xenmc.cacheHome, "versions" + File.separator + minecraft.version + ".json");
        serializable(minecraft.manifestFile, tempTranslate);
        ManifestGson manifest = Utils.gson.fromJson(new BufferedReader(new FileReader(minecraft.manifestFile)), ManifestGson.class);
        for (VersionGson version : manifest.versions) {
            if (minecraft.version.equals(version.id)) {
                download(minecraft.versionJsonFile.getParentFile(), minecraft.xenmc.threadDownloadCount, version.url);
                serializable(minecraft.versionJsonFile, tempTranslate);
                break;
            }
        }

    }

    public static void serializable(File file, File tempTranslate) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempTranslate));
        bw.write(Utils.gson.toJson(Utils.gson.fromJson(new BufferedReader(new FileReader(file)), Object.class)));
        bw.close();
        Files.copy(tempTranslate.toPath(), file.toPath().getParent().resolve(file.toPath().getFileName() + ".json"));


    }

    public void download(File dir, String url) {
        Downloader downloader = new Downloader(url, dir);
        downloader.start();
        downloader.isDone();
    }

    public void download(File dir, int thread, String url) {
        Downloader downloader = new Downloader(url, thread, dir);
        downloader.start();
        downloader.isDone();
    }
}
