package union.xenfork.xenmc.processor;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.gradle.api.Project;
import org.gradle.api.file.Directory;
import org.gradle.api.file.RegularFile;
import union.xenfork.xenmc.XenGradle;
import union.xenfork.xenmc.bean.manifest.McToBean;
import union.xenfork.xenmc.resources.PathEnum;

import java.nio.charset.StandardCharsets;

public interface IManifest {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // TODO: 2024/9/18 REWRITE
    default void manifest() {

        XenGradle.mcToBean =
                loadManifest("https://launchermeta.mojang.com/mc/game/version_manifest.json");
    }

    static McToBean loadManifest(String url) {
        return JSONUtil.toBean(HttpUtil.get(url, StandardCharsets.UTF_8), McToBean.class);
    }
}
