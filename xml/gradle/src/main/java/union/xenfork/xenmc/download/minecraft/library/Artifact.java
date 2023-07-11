package union.xenfork.xenmc.download.minecraft.library;

import com.google.gson.annotations.SerializedName;

public class Artifact {
    @SerializedName("path")
    public String path;
    @SerializedName("sha1")
    public String sha1;
    @SerializedName("size")
    public double size;
    @SerializedName("url")
    public String url;
}
