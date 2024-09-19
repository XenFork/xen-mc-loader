package union.xenfork.xenmc.bean.manifest;

import cn.hutool.core.annotation.Alias;

public class Latest {
    @Alias("release")
    public String release;

    @Alias("snapshot")
    public String snapshot;
}
