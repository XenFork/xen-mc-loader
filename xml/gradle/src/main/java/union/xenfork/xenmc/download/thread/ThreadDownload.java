package union.xenfork.xenmc.download.thread;

import cn.hutool.http.HttpDownloader;
import org.gradle.api.logging.Logger;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.util.Utils;
import union.xenfork.xenmc.step.s1.over1_14_4.download.DownloadPlugin;
import union.xenfork.xenmc.step.s1.over1_14_4.download.StreamProgressImpl;
import union.xenfork.xenmc.step.s1.over1_14_4.download.assets.Something;

import java.io.File;
import java.util.ArrayList;

/**
 * @author baka4n
 * @apiNote 分线程下载量
 */
public class ThreadDownload extends Thread {
    private final String assets;
    private final ArrayList<Something> somethingMap = new ArrayList<>();
    private final File dir;
    private Logger logger;
    private final MinecraftExtension minecraft;
    public ThreadDownload(String assets, File dir, Logger logger, MinecraftExtension minecraft) {
        this.assets = assets;
        this.dir = dir;
        this.logger = logger;
        this.minecraft = minecraft;
    }

    public ThreadDownload add(Something something) {
        somethingMap.add(something);
        return this;
    }

    @Override
    public void run() {
        somethingMap.forEach(something -> {
            String getUrl = "%s%s/%s".formatted(assets, something.hash.substring(0, 2), something.hash);
            File targetFileOrDir = new File(dir, something.hash.substring(0, 2) + File.separator + something.hash);
            if (!targetFileOrDir.exists()) {
                if (!DownloadPlugin.preStartDownload.get())
                    Utils.setupMessagePrefix(logger, minecraft);
                logger.lifecycle("download {}",getUrl);
                HttpDownloader.downloadFile(getUrl, targetFileOrDir, 3000, new StreamProgressImpl(getUrl));
            }
        });

    }
}
