package union.xenfork.xenmc.download.thread;

import com.google.gson.annotations.SerializedName;
import union.xenfork.xenmc.extensions.XenMcExtension;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ThreadDownload implements Runnable {
    @SerializedName("count")
    private int threadCount = 1;
    @SerializedName("url")
    private final URL url;

    final XenMcExtension xenmc;
    @SerializedName("save absolute path")
    private final String saveAbsolutePath;

    public ThreadDownload(String url, XenMcExtension xenmc, String saveAbsolutePath) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unable to load link, please check " ,e);
        }
        this.xenmc = xenmc;
        this.saveAbsolutePath = saveAbsolutePath;
    }

    @Override
    public void run() {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            File file = new File(xenmc.cacheHome, saveAbsolutePath);
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            int fileLength = conn.getContentLength();
            try (RandomAccessFile raf = new RandomAccessFile(file, "rwd")) {
                System.out.println(fileLength);
                raf.setLength(fileLength);
                threadCount = xenmc.threadDownloadCount;
            }
            int blockSize = fileLength / threadCount;
            for (int threadId = 1; threadId <= threadCount; threadId++) {
                int startPos = (threadId - 1) * blockSize;
                int endPos = threadId * blockSize - 1;
                if (threadCount == threadId) {
                    endPos = fileLength;
                }
                new Thread(new Download(threadId, startPos, endPos, url, file)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class Download implements Runnable {
        private int threadId;
        private int startPos;
        private int endPos;
        private URL path;
        private File file;

        public Download(int threadId, int startPos, int endPos, URL path, File file) {
            super();
            this.threadId = threadId;
            this.startPos = startPos;
            this.endPos = endPos;
            this.path = path;
            this.file = file;
        }



        @Override
        public void run() {
            try {
                HttpURLConnection conn = (HttpURLConnection) path.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(5000);
                File file = new File(threadId + ".xenmc");
                if (file.exists() && file.length() > 0) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    String s = br.readLine();
                    if (s != null && s.length() > 0) {
                        startPos = Integer.parseInt(s);
                    }
                }
                conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
                try (RandomAccessFile raf = new RandomAccessFile(this.file, "rwd")) {
                    raf.seek(startPos);
                    InputStream is = conn.getInputStream();
                    byte[] b = new byte[1024 * 1024 * 10];
                    int len = -1;
                    int newPos = startPos;
                    while ((len = is.read(b)) != -1) {
                        try (RandomAccessFile rr = new RandomAccessFile(file, "rwd")) {
                            raf.write(b, 0, len);
                            String savePoint = String.valueOf(newPos += len);
                            rr.write(savePoint.getBytes());
                        }
                    }
                }
                System.out.println("download success");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
