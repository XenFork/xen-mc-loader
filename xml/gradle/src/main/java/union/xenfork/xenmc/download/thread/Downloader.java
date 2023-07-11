package union.xenfork.xenmc.download.thread;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

public class Downloader {
    private static final int DEFAULT_THREAD_COUNT = 10;
    private final AtomicBoolean canceled;
    private DownloadFile file;
    private final String storageLocation;
    private final int threadCount;
    private long fileSize;
    private final String url;
    private long beginTime;
    private Logger logger;
    private final File dir;
    private boolean isDone = false;

    public Downloader(String url, File dir) {
        this(url, DEFAULT_THREAD_COUNT, dir);
    }

    public Downloader(String url, int threadCount, File dir) {
        this.url = url;
        this.dir = dir;
        this.threadCount = threadCount;
        this.canceled = new AtomicBoolean(false);
        this.storageLocation = url.substring(url.lastIndexOf('/')+1);
        this.logger = new Logger(storageLocation + ".log", url, threadCount);
    }

    public void start() {
        boolean reStart = Files.exists(Path.of(storageLocation + ".log"));
        if (reStart) {
            logger = new Logger(storageLocation + ".log");
            System.out.printf("* Continue the last download progress[Downloaded: %.2fKB]：%s\n", logger.getWroteSize() / 1014.0, url);
        } else {
            System.out.println("* start download：" + url);
        }
        if (-1 == (this.fileSize = getFileSize()))
            return;
        System.out.printf("* file size is %.2fKB\n", fileSize / 1024.0);

        this.beginTime = System.currentTimeMillis();
        try {
            this.file = new DownloadFile(dir ,storageLocation, fileSize, logger);
            if (reStart) {
                file.setWroteSize(logger.getWroteSize());
            }

            dispatcher(reStart);

            printDownloadProgress();
        } catch (IOException e) {
            System.err.println("x error to create file[" + e.getMessage() + "]");
        }
    }

    private void dispatcher(boolean reStart) {
        long blockSize = fileSize / threadCount;
        long lowerBound = 0, upperBound = 0;
        long[][] bounds = null;
        int threadID = 0;
        if (reStart) {
           bounds = logger.getBounds();
        }
        for (int i = 0; i < threadCount; i++) {
            if (reStart) {
                threadID = (int)(bounds[i][0]);
                lowerBound = bounds[i][1];
                upperBound = bounds[i][2];
            } else {
                threadID = i;
                lowerBound = i * blockSize;
                // fileSize-1 !!!!! fu.ck，找了一下午的错
                upperBound = (i == threadCount - 1) ? fileSize-1 : lowerBound + blockSize;
            }
            new DownloadTask(url, lowerBound, upperBound, file, canceled, threadID).start();
        }
    }

    private void printDownloadProgress() {
        long downloadedSize = file.getWroteSize();
        int i = 0;
        long lastSize = 0;
        while (!canceled.get() && downloadedSize < fileSize) {
            if (i++ % 4 == 3) {
                System.out.printf("download process: %.2f%%, Downloaded: %.2fKB，Download speed: %.2fKB/s\n",
                        downloadedSize / (double)fileSize * 100 ,
                        downloadedSize / 1024.0,
                        (downloadedSize - lastSize) / 1024.0 / 3);
                lastSize = downloadedSize;
                i = 0;
            }
            try {
                //noinspection BusyWait
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {}
            downloadedSize = file.getWroteSize();
        }
        file.close();
        if (canceled.get()) {
            try {
                Files.delete(Path.of(storageLocation));
            } catch (IOException ignore) {
            }
            System.err.println("x Download failed and task canceled");
            isDone = true;
        } else {
            System.out.println("* The download was successful, use time"+ (System.currentTimeMillis() - beginTime) +"ms");
            isDone = true;
        }
    }

    private long getFileSize() {
        if (fileSize != 0) {
            return fileSize;
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setConnectTimeout(3000);
            conn.setRequestMethod("HEAD");
            conn.connect();
            System.out.println("* Connection to Server succeeded");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Incorrect URL");
        } catch (IOException e) {
            System.err.println("x Connection to Server failed["+ e.getMessage() +"]");
            return -1;
        }
        return conn.getContentLengthLong();
    }

    /**
     * @apiNote 线程阻塞防止串线
     */
    @SuppressWarnings({"WhileLoopSpinsOnField", "StatementWithEmptyBody"})
    public void isDone() {
        while (!isDone);
    }

    public boolean isIsDone() {
        return isDone;
    }
}

