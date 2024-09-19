package union.xenfork.xenmc.bean.manifest;

import cn.hutool.core.annotation.Alias;

public enum McType {
    @Alias("release") release,
    @Alias("snapshot") snapshot,
    @Alias("old_beta") old_beta,
    @Alias("old_alpha") old_alpha;
}
