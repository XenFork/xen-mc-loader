package union.xenfork.xenmc.download.thread;

import cn.hutool.http.HttpDownloader;
import union.xenfork.xenmc.download.StreamProgressImpl;
import union.xenfork.xenmc.download.assets.Something;
import union.xenfork.xenmc.extensions.MinecraftExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author baka4n
 * @apiNote 分线程下载量
 */
public class ThreadDownload extends Thread {
    private final String assets;
    private final ArrayList<Something> somethingMap = new ArrayList<>();
    private final File dir;
    public ThreadDownload(String assets, File dir) {
        this.assets = assets;
        this.dir = dir;
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
            if (!targetFileOrDir.exists())
                HttpDownloader.downloadFile(getUrl, targetFileOrDir, 3000, new StreamProgressImpl(getUrl));
        });

    }
}
