package union.xenfork.xenmc.download;

import cn.hutool.json.JSONUtil;
import kotlin.text.Charsets;
import lombok.val;
import org.gradle.api.Project;
import union.xenfork.xenmc.resources.PathEnum;
import union.xenfork.xenmc.XenGradle;
import union.xenfork.xenmc.bean.version.VersionToBean;

public class Version {
    public static void version(Project target) {
        target.afterEvaluate(project -> {
            for (String version : XenGradle.minecraft.getVersions().get()) {
                val versionJson = PathEnum.versionDir
                        .dir(project.getLayout().getBuildDirectory())
                        .dir(version)
                        .file(version + ".json").getAsFile();
                val json = JSONUtil.readJSON(versionJson, Charsets.UTF_8);
                val bean = json.toBean(VersionToBean.class);

            }
        });
    }
}
