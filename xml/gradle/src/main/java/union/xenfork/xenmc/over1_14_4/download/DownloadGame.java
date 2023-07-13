package union.xenfork.xenmc.over1_14_4.download;

import cn.hutool.http.HttpDownloader;
import org.gradle.api.Project;
import union.xenfork.xenmc.over1_14_4.download.minecraft.MinecraftVersionGson;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;

import java.io.File;

import static cn.hutool.http.HttpDownloader.downloadFile;

/**
 * @author baka4n
 * @apiNote 下载游戏和mapping
 */
public class DownloadGame implements BootstrappedPluginProject {
    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        MinecraftVersionGson versionSet = MinecraftExtension.versionSet;
        MinecraftExtension.game = new File(minecraft.xenmc.cacheHome, "game");
        MinecraftExtension.mapping = new File(minecraft.xenmc.cacheHome, "mapping");
        MinecraftExtension.clientGame = new File(MinecraftExtension.game, minecraft.version + File.separator + minecraft.version + "-client.jar");
        MinecraftExtension.clientMapping = new File(MinecraftExtension.mapping, minecraft.version + File.separator + minecraft.version + "-client.txt");
        MinecraftExtension.serverGame = new File(MinecraftExtension.game,minecraft.version + File.separator + minecraft.version + "-server.jar");
        MinecraftExtension.serverMapping = new File(MinecraftExtension.mapping,minecraft.version + File.separator + minecraft.version + "-server.txt");
        if (!Utils.isSha1(MinecraftExtension.clientGame, versionSet.downloads.client.sha1)) {
            HttpDownloader.downloadFile(versionSet.downloads.client.url, MinecraftExtension.clientGame, 3000, new StreamProgressImpl(versionSet.downloads.client.url));
            if (!Utils.isSha1(MinecraftExtension.clientGame, versionSet.downloads.client.sha1)) {
                MinecraftExtension.clientGame.delete();
                throw new RuntimeException("fail to verify client game");
            }
        }
        if (!Utils.isSha1(MinecraftExtension.serverGame, versionSet.downloads.server.sha1)) {
            HttpDownloader.downloadFile(versionSet.downloads.server.url, MinecraftExtension.serverGame, 3000, new StreamProgressImpl(versionSet.downloads.server.url));
            if (!Utils.isSha1(MinecraftExtension.serverGame, versionSet.downloads.server.sha1)) {
                MinecraftExtension.serverGame.delete();
                throw new RuntimeException("fail to verify server game");
            }
        }
        if (!Utils.isSha1(MinecraftExtension.clientMapping, versionSet.downloads.client_mappings.sha1)) {
            HttpDownloader.downloadFile(versionSet.downloads.client_mappings.url, MinecraftExtension.clientMapping, 3000, new StreamProgressImpl(versionSet.downloads.client_mappings.url));
            if (!Utils.isSha1(MinecraftExtension.clientMapping, versionSet.downloads.client_mappings.sha1)) {
                MinecraftExtension.clientMapping.delete();
                throw new RuntimeException("fail to verify client mapping");
            }
        }

        if (!Utils.isSha1(MinecraftExtension.serverMapping, versionSet.downloads.server_mappings.sha1)) {
            HttpDownloader.downloadFile(versionSet.downloads.server_mappings.url, MinecraftExtension.serverMapping, 3000, new StreamProgressImpl(versionSet.downloads.server_mappings.url));
            if (!Utils.isSha1(MinecraftExtension.serverMapping, versionSet.downloads.server_mappings.sha1)) {
                MinecraftExtension.serverMapping.delete();
                throw new RuntimeException("fail to verify server mapping");
            }
        }
    }
}
