package union.xenfork.xenmc.download.minecraft;

import com.google.gson.annotations.SerializedName;

public class DownloadImpl {
    @SerializedName("sha1")
    public String sha1;
    @SerializedName("size")
    public double size;
    @SerializedName("url")
    public String url;
}
