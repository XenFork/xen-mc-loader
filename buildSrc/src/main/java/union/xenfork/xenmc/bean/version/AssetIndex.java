package union.xenfork.xenmc.bean.version;

import cn.hutool.core.annotation.Alias;

public class AssetIndex {
    @Alias("id")
    public String id;

    @Alias("sha1")
    public String sha1;


    @Alias("size")
    public long size;


    @Alias("totalSize")
    public long totalSize;
}
