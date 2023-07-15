package union.xenfork.xenmc.step.s1.over1_14_4.download;

import cn.hutool.core.io.StreamProgress;

import java.util.concurrent.atomic.AtomicLong;

public class StreamProgressImpl implements StreamProgress {
    public String url;
    String formatted = "";
    long startTime, endTime;
    static AtomicLong fileSize = new AtomicLong(0);
    public static AtomicLong usingTime = new AtomicLong(0);
    public StreamProgressImpl(String url) {
        this.url = url;
    }
    @Override
    public void start() {
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
