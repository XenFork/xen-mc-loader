package union.xenfork.xenmc.bean.version;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import org.gradle.api.file.Directory;

public class VersionToBean {
    @Alias("arguments")
    public Arguments arguments;
    @Alias("assetIndex")
    public AssetIndex assetIndex;

    @Alias("assets")
    public String assets;

    @Alias("complianceLevel")
    public int complianceLevel;

    @Alias("downloads")
    public Downloads downloads;

    @Alias("id")
    public String id;

    @Alias("javaVersion")
    public JavaVersion javaVersion;


    @Alias("libraries")
    public Library[] libraries;

    @Alias("logging")
    public Object logging;


    @Alias("mainClass")
    public String mainClass;

    @Alias("minimumLauncherVersion")
    public int minimumLauncherVersion;


    @Alias("releaseTime")
    public String releaseTime;

    @Alias("time")
    public String time;

    @Alias("type")
    public String type;

    // TODO: 2024/9/19 save
    public void save(Directory dir) {
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(this), dir.file("%s.json".formatted(id)).getAsFile());
    }
}
