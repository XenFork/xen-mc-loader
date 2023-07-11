package union.xenfork.xenmc.download.minecraft;

import com.google.gson.annotations.SerializedName;

public class AssetIndex {
    @SerializedName("id")
    public String id;
    @SerializedName("sha1")
    public String sha1;
    @SerializedName("size")
    public double size;
    @SerializedName("totalSize")
    public double totalSize;
    @SerializedName("url")
    public String url;
}
