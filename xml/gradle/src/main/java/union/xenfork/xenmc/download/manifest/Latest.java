package union.xenfork.xenmc.download.manifest;

import com.google.gson.annotations.SerializedName;

public class Latest {
    @SerializedName("release")
    public String release;
    @SerializedName("snapshot")
    public String snapshot;
}