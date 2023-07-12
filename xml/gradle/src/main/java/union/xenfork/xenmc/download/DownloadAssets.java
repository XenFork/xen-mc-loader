package union.xenfork.xenmc.download;

import com.google.gson.reflect.TypeToken;
import org.gradle.api.Project;
import union.xenfork.xenmc.download.assets.Something;
import union.xenfork.xenmc.download.thread.Downloader;
import union.xenfork.xenmc.download.thread.Downloads;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

import static union.xenfork.xenmc.gradle.Utils.gson;
import static union.xenfork.xenmc.gradle.Utils.tempTranslate;

public class DownloadAssets implements BootstrappedPluginProject {
    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        MinecraftExtension.assetsDir = new File(minecraft.xenmc.cacheHome, "assets");
        MinecraftExtension.assetsIndexFile = new File(MinecraftExtension.assetsDir, "indexes" + File.separator + MinecraftExtension.versionSet.id + ".json");
        MinecraftExtension.assetsObjectsDir = new File(MinecraftExtension.assetsDir, "objects");
        Downloader downloader = new Downloader(MinecraftExtension.versionSet.assetIndex.url, MinecraftExtension.assetsIndexFile.getParentFile(), MinecraftExtension.assetsIndexFile.getName());
        downloader.start();
        downloader.isDone();
        Utils.serializable(MinecraftExtension.assetsIndexFile, tempTranslate);
        TypeToken<Map<String, Map<String, Something>>> token = new TypeToken<>() {};
        Map<String, Map<String, Something>> assetsLoader = gson.fromJson(new BufferedReader(new FileReader(MinecraftExtension.assetsIndexFile)), token.getType());
        Downloads downloads = new Downloads();
        assetsLoader.forEach((s, stringSomethingMap) -> {
            stringSomethingMap.forEach((s1, something) -> {
                String getUrl = "%s%s/%s".formatted(minecraft.assets, something.hash.substring(0, 2), something.hash);
                downloads.add(getUrl, minecraft.xenmc.threadDownloadCount, new File(MinecraftExtension.assetsObjectsDir, something.hash.substring(0, 2)), something.hash);
//                System.out.printf("%s->%s->%s :: %s%n", s, s1, something.hash, something.size);
            });
        });
        downloads.downloads();
        downloads.isDone();
    }
}
