package union.xenfork.xenmc.download.manifest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ManifestGson {
    @SerializedName("latest")
    public Latest latest;
    @SerializedName("versions")
    public ArrayList<VersionGson> versions;
}
