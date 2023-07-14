package union.xenfork.xenmc.download.thread;

import org.gradle.api.logging.Logger;
import union.xenfork.xenmc.step.s1.over1_14_4.download.assets.Something;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Downloads {
    private Map<Integer, ThreadDownload> threadDownloads = new HashMap<>();
    private final String assets;
    private final File dir;
    private Logger logger;

    public static void downloads(Map<String, Map<String, Something>> assetsLoader, String assets, File dir, int i, Logger logger) {
        Downloads downloads = new Downloads(assets, dir, logger);
        Map<String, Something> somethingMap = assetsLoader.get("objects");
        String[] strings = somethingMap.keySet().toArray(new String[0]);
        for (int j = 0; j < somethingMap.size(); j+=i) {
            for (int k = 0; k < i; k++) {
                if (!downloads.threadDownloads.containsKey(k)) {
                    downloads.threadDownloads.put(k, new ThreadDownload(assets, dir, logger));
                }
                if (strings.length <= j) {
                    break;
                }
                downloads.threadDownloads.get(k).add(somethingMap.get(strings[j]));
            }
        }
        downloads.threadDownloads.forEach((__, threadDownload) -> {
            threadDownload.start();
        });
        downloads.isDone();
    }
    public Downloads(String assets, File dir, Logger logger) {
        this.assets = assets;
        this.dir = dir;
        this.logger = logger;
    }

    public void isDone() {
        threadDownloads.forEach((__, threadDownload) -> {
            try {
                threadDownload.join();
            } catch (InterruptedException ignored) {}
        });
    }

    public void add(int i ,Something... somethings) {
        ThreadDownload value = new ThreadDownload(assets, dir, logger);
        for (Something something : somethings) {
            value.add(something);
        }
        threadDownloads.put(i, value);
    }

    public void set(int i, Something... somethings) {
        ThreadDownload value = threadDownloads.get(i);
        for (Something something : somethings) {
            value.add(something);
        }
        threadDownloads.put(i, value);
    }
}
