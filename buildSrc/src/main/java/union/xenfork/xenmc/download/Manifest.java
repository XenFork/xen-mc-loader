package union.xenfork.xenmc.download;

import cn.hutool.core.thread.AsyncUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import kotlin.text.Charsets;
import lombok.val;
import org.gradle.api.Project;
import union.xenfork.xenmc.resources.PathEnum;
import union.xenfork.xenmc.XenGradle;
import union.xenfork.xenmc.bean.manifest.McToBean;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class Manifest {
    private static final  String url = "https://launchermeta.mojang.com/mc/game/version_manifest.json";


    public static void manifest(Project target) {
        val dir = PathEnum.xenDir.dir(target.getLayout().getBuildDirectory());
        val asFile = dir.file("version_manifest.json").getAsFile();
        HttpUtil.downloadFile(url, asFile);
        val json = JSONUtil.readJSON(asFile, Charsets.UTF_8);
        val bean = json.toBean(McToBean.class);
        target.afterEvaluate(project -> {
            try(val executor = Executors.newVirtualThreadPerTaskExecutor()) {
                val version = XenGradle.minecraft.getVersions().get();
                if (version.isEmpty()) {
                    XenGradle.minecraft.getVersions().add(
                            XenGradle.minecraft.getIsRelease().get() ?
                                    bean.latest.release :
                                    bean.latest.snapshot
                    );

                }

                val completableFutures = new CompletableFuture[version.size()];
                for (int i = 0; i < version.size(); i++) {
                    val v = version.get(i);
                    val first = Arrays.stream(bean.versions).filter(ver -> ver.id.equals(v)).toList().getFirst();
                    completableFutures[i] = CompletableFuture.runAsync(() -> {
                        val url = first.url;
                        HttpUtil.downloadFile(url, PathEnum.versionDir
                                .dir(target.getLayout().getBuildDirectory())
                                .dir(v)
                                .file(v + ".json").getAsFile());
                    }, executor);
                }
                AsyncUtil.waitAll(completableFutures);



            }

        });
    }
}
