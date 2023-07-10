package union.xenfork.xenmc.download.thread;

import union.xenfork.xenmc.extensions.XenMcExtension;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SonDownload extends DownloadThread {
    public int threadId,start,end;
    public final Download download;
    public SonDownload(String saveFile, String fileName, String url, XenMcExtension xenmc, Download download) {
        super(saveFile, fileName, url, xenmc);
        this.download = download;
    }

    public void setter(int threadId, int start, int end) {
        this.threadId = threadId;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        InputStream is = null;
        RandomAccessFile raf = null;
        try {
            File temFile = new File(temp_file, saveFile + File.separator + fileName + threadId + ".temp");
            File parentFile = temFile.getParentFile();
            if (!parentFile.exists())
                parentFile.mkdirs();
            if (xenmc.bpDownload && temFile.exists() && temFile.length() > 0) {
                int leng;
                byte[] temp;
                try (FileInputStream fis = new FileInputStream(temFile)) {
                     temp = new byte[1024];
                     leng = fis.read(temp);
                }
                String s = new String(temp, 0, leng);
                int downloadLenInt = Integer.parseInt(s)-1;
                start+=downloadLenInt;
            }
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range", "bytes=" + start + "-" + end);
            int reponseCode = connection.getResponseCode();
            if (reponseCode==206) {
                raf = new RandomAccessFile(save, "rwd");
                is = connection.getInputStream();
                raf.seek(start);
                int len = 0;
                byte[] buff = new byte[1024*1024];
                int total = 0;
                while ((len = is.read(buff))!= -1) {
                    raf.write(buff, 0, len);
                    synchronized (download) {
                        download.progress += len;
                    }
                    total+=len;
                    if (xenmc.bpDownload) {
                        try (RandomAccessFile info = new RandomAccessFile(temFile, "rwd")) {
                            info.write(String.valueOf(total + start).getBytes());
                        }
                    }
                }
            }
            xenmc.project.getLogger().lifecycle("thread: %d is download success!".formatted(threadId));
            if (xenmc.bpDownload) {
                synchronized (download) {
                    download.runningThead--;
                    if (download.runningThead == 0) {
                        for (int i = 0; i < xenmc.threadDownloadCount; i++) {
                            File file = new File(temp_file, saveFile + File.separator + fileName + (i + 1) + ".temp");
                            file.delete();
                            xenmc.project.getLogger().lifecycle("%s%d.txt is download success, clear temp file!".formatted(fileName, i + 1));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
