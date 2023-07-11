package union.xenfork.xenmc.download.minecraft.logging;

import com.google.gson.annotations.SerializedName;

public class ClientLogging {
    @SerializedName("argument")
    public String argument;
    @SerializedName("file")
    public File file;
    @SerializedName("type")
    public String type;
}
