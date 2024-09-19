package union.xenfork.xenmc.bean.version;

import cn.hutool.core.annotation.Alias;
import cn.hutool.json.JSONObject;

public class Arguments {
    @Alias("game")
    public Object[] game;

    @Alias("jvm")
    public Object[] jvm;
}
