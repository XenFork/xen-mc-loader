package union.xenfork.xenmc.download.thread;

import org.gradle.api.logging.Logger;
import union.xenfork.xenmc.extensions.XenMcExtension;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Download extends DownloadThread {
    public int runningThead;
    public volatile int len = 0;
    public volatile int progress;
    public Download(String saveFile, String fileName, String url, XenMcExtension xenmc) {
        super(saveFile, fileName, url, xenmc);
        runningThead = xenmc.threadDownloadCount;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                int length = connection.getContentLength();
                len = length;
                try (RandomAccessFile raf = new RandomAccessFile(save, "rwd")) {
                    raf.setLength(length);
                }
                int blockSize = length / xenmc.threadDownloadCount;
                for (int i = 1; i <= xenmc.threadDownloadCount; i++) {
                    int start = (i - 1)*blockSize;
                    int end = i * blockSize - 1;
                    if (i == xenmc.threadDownloadCount) {
                        end = length;
                    }
                    Logger logger = xenmc.project.getLogger();
                    logger.lifecycle("thread: %d download %d ----> %d".formatted(i, start, end));
                    var son = new SonDownload(saveFile, fileName, this.url, xenmc, this);
                    son.setter(i, start, end);
                    new Thread(son).start();
                }
                speed();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void speed() {
        int temp = 0;
        while (progress!=len) {
            temp= progress;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double p  = (double) temp / (double) len*100;
            temp = progress-temp;
            listener.speed(temp, p);
            xenmc.project.getLogger().lifecycle("file is downloaded");
        }
    }

    SpeedListener listener;
    public Download addSpeedListener(SpeedListener listener) {
        this.listener = listener;
        return this;
    }
}
