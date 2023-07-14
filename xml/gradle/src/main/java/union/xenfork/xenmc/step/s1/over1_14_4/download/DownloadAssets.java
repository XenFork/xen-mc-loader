package union.xenfork.xenmc.step.s1.over1_14_4.download;

import cn.hutool.http.HttpDownloader;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import union.xenfork.xenmc.step.s1.over1_14_4.download.assets.Something;
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
        MinecraftExtension.assetsVirtual = new File(MinecraftExtension.assetsDir, "virtual");
        HttpDownloader.downloadFile(MinecraftExtension.versionSet.assetIndex.url, MinecraftExtension.assetsIndexFile, 3000, new StreamProgressImpl(MinecraftExtension.versionSet.assetIndex.url));
        Utils.serializable(MinecraftExtension.assetsIndexFile, tempTranslate);
        TypeToken<Map<String, Map<String, Something>>> token = new TypeToken<>() {};
        Map<String, Map<String, Something>> assetsLoader = gson.fromJson(new BufferedReader(new FileReader(MinecraftExtension.assetsIndexFile)), token.getType());
        Downloads.downloads(assetsLoader, minecraft.assets, MinecraftExtension.assetsObjectsDir, minecraft.xenmc.threadDownloadCount, project.getLogger());
        FileUtils.copyDirectory(MinecraftExtension.assetsObjectsDir, new File(MinecraftExtension.assetsVirtual, "legacy"));
    }
}
