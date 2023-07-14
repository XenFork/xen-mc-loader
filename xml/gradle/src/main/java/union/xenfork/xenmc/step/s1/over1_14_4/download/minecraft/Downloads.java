package union.xenfork.xenmc.step.s1.over1_14_4.download.minecraft;

import com.google.gson.annotations.SerializedName;

public class Downloads {
    @SerializedName("client")
    public DownloadImpl client;
    @SerializedName("client_mappings")
    public DownloadImpl client_mappings;
    @SerializedName("server")
    public DownloadImpl server;
    @SerializedName("server_mappings")
    public DownloadImpl server_mappings;

}
