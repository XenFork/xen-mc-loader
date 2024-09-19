package union.xenfork.xenmc.bean.version;

import cn.hutool.core.annotation.Alias;

public class Downloads {
    @Alias("client")
    public DAction client;

    @Alias("client_mappings")
    public DAction clientMappings;

    @Alias("server")
    public DAction server;

    @Alias("server_mappings")
    public DAction serverMappings;
}
