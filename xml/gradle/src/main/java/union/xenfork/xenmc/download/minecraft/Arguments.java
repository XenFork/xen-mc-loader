package union.xenfork.xenmc.download.minecraft;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Arguments {
    @SerializedName("game")
    public ArrayList<Object> game;//可能为String也可能是Map
    @SerializedName("jvm")
    public ArrayList<Object> jvm;

}
