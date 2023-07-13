package union.xenfork.xenmc.download.thread;

import union.xenfork.xenmc.download.assets.Something;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Downloads {
    private Map<Integer, ThreadDownload> threadDownloads = new HashMap<>();
    private final String assets;
    private final File dir;

    public static void downloads(Map<String, Map<String, Something>> assetsLoader, String assets, File dir, int i) {
        Downloads downloads = new Downloads(assets, dir);
        Map<String, Something> somethingMap = assetsLoader.get("objects");
        String[] strings = somethingMap.keySet().toArray(new String[0]);
        for (int j = 0; j < somethingMap.size(); j+=i) {
            for (int k = 0; k < i; k++) {
                if (!downloads.threadDownloads.containsKey(k)) {
                    downloads.threadDownloads.put(k, new ThreadDownload(assets, dir));
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
    public Downloads(String assets, File dir) {
        this.assets = assets;
        this.dir = dir;
    }

    public void isDone() {
        threadDownloads.forEach((__, threadDownload) -> {
            try {
                threadDownload.join();
            } catch (InterruptedException ignored) {}
        });
    }

    public void add(int i ,Something... somethings) {
        ThreadDownload value = new ThreadDownload(assets, dir);
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
