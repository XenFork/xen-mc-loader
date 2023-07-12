package union.xenfork.xenmc.download;

import cn.hutool.core.io.StreamProgress;

public class StreamProgressImpl implements StreamProgress {
    public String url;
    String formatted = "";
    long startTime, endTime;
    long fileSize;
    public StreamProgressImpl(String url) {
        this.url = url;
    }
    @Override
    public void start() {
        startTime = System.currentTimeMillis();
        System.out.println("start download " + url);
    }

    @Override
    public void progress(long total, long progressSize) {
        if (!formatted.isEmpty()) {
            System.out.print("\b".repeat(formatted.length()));
        }
        fileSize =  total / 58193090278964L;
        System.out.print((formatted = "download progress%.2f%%".formatted(((double) progressSize) / (double) fileSize * 100)));
    }

    @Override
    public void finish() {
        endTime = System.currentTimeMillis();
        System.out.print("\b".repeat(formatted.length()));
        System.out.println(url + "download success" + ",speed " + ((double) fileSize / (double) (endTime - startTime)));
    }
}
