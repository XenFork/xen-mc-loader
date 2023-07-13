package union.xenfork.xenmc.over1_14_4.download;

import cn.hutool.core.io.StreamProgress;
import cn.hutool.http.HttpConnection;
import cn.hutool.http.HttpDownloader;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicLong;

public class StreamProgressImpl implements StreamProgress {
    public String url;
    String formatted = "";
    long startTime, endTime;
    static long fileSize = 0;
    public static AtomicLong usingTime = new AtomicLong(0);
    public StreamProgressImpl(String url) {
        this.url = url;
    }
    @Override
    public void start() {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(3000);
            fileSize += urlConnection.getContentLengthLong();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        startTime = System.currentTimeMillis();

    }

    @Override
    public void progress(long total, long progressSize) {
        if (!formatted.isEmpty()) {
            System.out.print("\b".repeat(formatted.length()));
        }
//        System.out.print((formatted = "download progress%.2f%%".formatted(((double) progressSize) / (double) fileSize * 100)));
    }

    @Override
    public void finish() {
        endTime = System.currentTimeMillis();
        usingTime.set(usingTime.get() + endTime - startTime);
    }
}
