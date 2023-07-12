package io.github.baka4n;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.http.HttpUtil;

import java.io.File;

/**
 * @author baka4n
 * @apiNote 实现下载
 */
public class Download {

    public static void download(String url, File dir) {
        long l = HttpUtil.downloadFile(url, FileUtil.file(dir), new StreamProgress() {
            @Override
            public void start() {
                System.out.printf("start download%s%n", url);
            }

            @Override
            public void progress(long total, long progressSize) {
                
                System.out.println((double)progressSize / ((double) total / 58193090278964L));
                System.out.printf("already download%s%n", FileUtil.readableFileSize(progressSize));
            }

            @Override
            public void finish() {

            }
        });
        System.out.println(l);
    }

    public Download(String url, File saveFile) {


    }

}
