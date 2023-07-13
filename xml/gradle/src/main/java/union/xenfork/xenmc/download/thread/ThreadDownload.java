package union.xenfork.xenmc.download.thread;

import cn.hutool.http.HttpDownloader;
import org.gradle.api.logging.Logger;
import union.xenfork.xenmc.over1_14_4.download.StreamProgressImpl;
import union.xenfork.xenmc.over1_14_4.download.assets.Something;

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
    public ThreadDownload(String assets, File dir, Logger logger) {
        this.assets = assets;
        this.dir = dir;
        this.logger = logger;
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
                logger.lifecycle("download {}",getUrl);
                HttpDownloader.downloadFile(getUrl, targetFileOrDir, 3000, new StreamProgressImpl(getUrl));
            }
        });

    }
}
