package union.xenfork.xenmc.download.thread;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Downloads {
    public ArrayList<Downloader> downloads = new ArrayList<>();
    public Downloads() {
    }
    public Downloads(File dir, String... urls) {
        for (String url : urls) {
            downloads.add(new Downloader(url, dir));
        }
    }

    /**
     * @apiNote asm注入拦截在下载之前添加链接
     */
    public Downloads add(File dir,String... urls) {
       Downloads downloads1 = new Downloads(dir, urls);
       this.downloads.addAll(downloads1.downloads);
       downloads1 = null;
       return downloads1;
    }

    public Downloads add(String url, int threadCount, File dir, String storageLocation) {
        this.downloads.add(new Downloader(url, threadCount, dir, storageLocation));
        return this;
    }

    public Downloads add(File dir, int thread, String... urls) {
        Downloads downloads1 = new Downloads(dir, thread, urls);
        this.downloads.addAll(downloads1.downloads);
        downloads1 = null;
        return downloads1;
    }

    public void downloads() {
        for (Downloader download : downloads) {
            download.start();
        }
    }

    public Downloads(File dir, int thread, String... urls) {
        for (String url : urls) {
            downloads.add(new Downloader(url, thread, dir));
        }
        for (Downloader download : downloads) {
            download.start();
        }
    }


    public void isDone() {
        for (Downloader download : downloads) {
            download.isDone();
        }
    }
}
