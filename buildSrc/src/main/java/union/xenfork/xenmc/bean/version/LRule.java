package union.xenfork.xenmc.bean.version;

import cn.hutool.core.annotation.Alias;
import org.jetbrains.annotations.Nullable;
import union.xenfork.xenmc.bean.OS;

public class LRule {
    @Alias("action")
    public Action action;

    @Alias("features")
    public Object features;

    @Alias("os")
    @Nullable
    public OS os;
}
