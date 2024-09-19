package union.xenfork.xenmc.bean.version;

import cn.hutool.core.annotation.Alias;
import org.jetbrains.annotations.Nullable;

public class Library {
    @Alias("downloads")
    public LDAction downloads;
    @Alias("name")
    public String name;
    @Nullable
    @Alias("rules")
    public Object rules;
}
