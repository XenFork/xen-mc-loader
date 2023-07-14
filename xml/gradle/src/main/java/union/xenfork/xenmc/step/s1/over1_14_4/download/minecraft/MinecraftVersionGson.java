package union.xenfork.xenmc.step.s1.over1_14_4.download.minecraft;

import com.google.gson.annotations.SerializedName;
import union.xenfork.xenmc.step.s1.over1_14_4.download.minecraft.library.Libraries;
import union.xenfork.xenmc.step.s1.over1_14_4.download.minecraft.logging.Logging;

import java.util.ArrayList;

public class MinecraftVersionGson {
    @SerializedName("arguments")
    public Arguments arguments;
    @SerializedName("assetIndex")
    public AssetIndex assetIndex;

    @SerializedName("assets")
    public String assets;
    @SerializedName("complianceLevel")
    public double complianceLevel;
    @SerializedName("downloads")
    public Downloads downloads;
    @SerializedName("id")
    public String id;
    @SerializedName("javaVersion")
    public JavaVersion javaVersion;
    @SerializedName("libraries")
    public ArrayList<Libraries> libraries;
    @SerializedName("logging")
    public Logging logging;

    @SerializedName("mainClass")
    public String mainClass;

    @SerializedName("minimumLauncherVersion")
    public double minimumLauncherVersion;

    @SerializedName("releaseTime")
    public String releaseTime;

    @SerializedName("time")
    public String time;

    @SerializedName("type")
    public String type;

}
