package union.xenfork.xenmc.bean.manifest;

import cn.hutool.core.annotation.Alias;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import union.xenfork.xenmc.bean.version.VersionToBean;

import java.nio.charset.StandardCharsets;

public class Version {
    @Alias("id")
    public String id;
    @Alias("type")
    public McType type;

    @Alias("url")
    public String url;

    @Alias("time")
    public String time;

    @Alias("releaseTime")
    public String releaseTime;

    public VersionToBean versions() {
        return JSONUtil.toBean(HttpUtil.get(url, StandardCharsets.UTF_8), VersionToBean.class);
    }
}
