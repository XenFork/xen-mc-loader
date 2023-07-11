package union.xenfork.xenmc.download.minecraft.library;

import com.google.gson.annotations.SerializedName;

public class DownloadLibrary {
    @SerializedName("artifact")
    public Artifact artifact;
    @SerializedName("name")
    public String name;
    @SerializedName("rules")
    public RulesNonnull rules;
}
