package union.xenfork.xenmc.download.thread;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicLong;

class DownloadFile {
    private final RandomAccessFile file;
    private final FileChannel channel; // 线程安全类
    private final AtomicLong wroteSize; // 已写入的长度
    private final Logger logger;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    DownloadFile(File dir, String fileName, long fileSize, Logger logger) throws IOException {
        if (!dir.exists())
            dir.mkdirs();
        this.wroteSize = new AtomicLong(0);
        this.logger = logger;
        this.file = new RandomAccessFile(new File(dir, fileName), "rw");
        file.setLength(fileSize);
        channel = file.getChannel();
    }

    /**
     * 写数据
     * @param offset 写偏移量
     * @param buffer 数据
     * @throws IOException 写数据出现异常
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void write(long offset, ByteBuffer buffer, int threadID, long upperBound) throws IOException {
        buffer.flip();
        int length = buffer.limit();
        while (buffer.hasRemaining()) {
            channel.write(buffer, offset);
        }
        wroteSize.addAndGet(length);
        logger.updateLog(threadID, length, offset + length, upperBound); // 更新日志
    }


    long getWroteSize() {
        return wroteSize.get();
    }


    void setWroteSize(long wroteSize) {
        this.wroteSize.set(wroteSize);
    }

    void close() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
