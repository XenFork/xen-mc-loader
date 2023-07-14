package union.xenfork.xenmc.step.s1.over1_14_4.download.minecraft;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Arguments {
    @SerializedName("game")
    public ArrayList<Object> game;//可能为String也可能是Map
    @SerializedName("jvm")
    public ArrayList<Object> jvm;

}
