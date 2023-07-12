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

    public Download(String url, File saveFile) {
        HttpUtil.downloadFile(url, FileUtil.file(saveFile.getParentFile()), new StreamProgress() {
            @Override
            public void start() {
                System.out.printf("start download%s%n", url);
            }

            @Override
            public void progress(long total, long progressSize) {
                System.out.printf("already download%d%%%n", progressSize / total * 100);
            }

            @Override
            public void finish() {

            }
        });

    }

}
