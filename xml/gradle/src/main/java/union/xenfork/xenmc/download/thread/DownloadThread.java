package union.xenfork.xenmc.download.thread;

import union.xenfork.xenmc.extensions.XenMcExtension;

import java.io.File;

public abstract class DownloadThread implements Runnable {
    public String temp_file = System.getProperty("user.dir") + File.separator + ".gradle";
    public String url;
    public File save;
    public String saveFile;
    public String fileName;
    public XenMcExtension xenmc;
    public DownloadThread(String saveFile, String fileName, String url, XenMcExtension xenmc) {
        this.url = url;
        this.fileName = fileName;
        this.saveFile = saveFile;
        save = new File(xenmc.cacheHome, saveFile + File.separator + fileName);
        File parentFile = save.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        this.xenmc = xenmc;
    }

    @Override
    public abstract void run();
}
