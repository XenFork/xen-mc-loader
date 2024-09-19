package union.xenfork.xenmc.bean.version;

import cn.hutool.core.annotation.Alias;

public class DAction {
    @Alias("sha1")
    public String sha1;

    @Alias("size")
    public long size;

    @Alias("url")
    public String url;
}
