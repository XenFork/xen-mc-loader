package union.xenfork.xenmc.step.s1.over1_14_4.download.manifest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ManifestGson {

    @SerializedName("latest")
    public Latest latest;
    @SerializedName("versions")
    public ArrayList<VersionGson> versions;
}
