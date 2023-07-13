package union.xenfork.xenmc.over1_14_4.download.minecraft.library;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Libraries {
    @SerializedName("downloads")
    public DownloadLibrary downloads;
    @SerializedName("name")
    public String name;
    @SerializedName("rules")
    public ArrayList<RulesNonnull> rules;
}
