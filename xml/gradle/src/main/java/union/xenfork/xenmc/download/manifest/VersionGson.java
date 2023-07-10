package union.xenfork.xenmc.download.manifest;

import com.google.gson.annotations.SerializedName;

import java.net.URL;
import java.time.LocalDateTime;

public class VersionGson {
    @SerializedName("id")
    public String id;
    @SerializedName("type")
    public String type;
    @SerializedName("url")
    public String url;
    @SerializedName("time")
    public String time;
    @SerializedName("releaseTime")
    public String releaseTime;
    @SerializedName("sha1")
    public String sha1;
    @SerializedName("complianceLevel")
    public int complianceLevel;
}
