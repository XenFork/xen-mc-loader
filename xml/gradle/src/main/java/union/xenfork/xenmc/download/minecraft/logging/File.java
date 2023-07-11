package union.xenfork.xenmc.download.minecraft.logging;

import com.google.gson.annotations.SerializedName;

public class File {
    @SerializedName("id")
    public String id;
    @SerializedName("sha1")
    public String sha1;

    @SerializedName("size")
    public double size;

    @SerializedName("url")
    public String url;
}
