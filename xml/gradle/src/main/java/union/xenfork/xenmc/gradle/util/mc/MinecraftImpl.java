package union.xenfork.xenmc.gradle.util.mc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import union.xenfork.xenmc.gradle.XenMcGradleExtension;

import java.io.File;

import static union.xenfork.xenmc.gradle.util.DownloadImpl.readFile;
import static union.xenfork.xenmc.gradle.util.DownloadImpl.readString;
import static union.xenfork.xenmc.gradle.util.OsSys.isMac;
import static union.xenfork.xenmc.gradle.util.OsSys.isWin;

public class MinecraftImpl {
    public static File getMinecraftDir() {
        File minecraftFolder;
        if (isWin()) {
            minecraftFolder = new File(System.getenv("APPDATA"), File.separator + ".minecraft");
        } else if (isMac()) {
            minecraftFolder = new File(System.getProperty("user.home"), File.separator + "Library" + File.separator + "Application Support" + File.separator + "minecraft");
        } else {
            minecraftFolder = new File(System.getProperty("user.home"), File.separator + ".minecraft");
        }
        return minecraftFolder;
    }

    public static String getJson(XenMcGradleExtension extension) {
        String jsonUrl = "";
        for (JsonElement jsonElement : new Gson().fromJson(readString(extension.getMinecraft().manifest), JsonObject.class).get("versions").getAsJsonArray()) {
            if (jsonElement.getAsJsonObject().get("id").getAsString().equals(extension.getMinecraft().version)) {
                jsonUrl = jsonElement.getAsJsonObject().get("url").getAsString();
            }
        }
        return readString(jsonUrl);
    }

    public static JsonObject getDownloadsJson(XenMcGradleExtension extension) {
        return new Gson().fromJson(getJson(extension), JsonObject.class).get("downloads").getAsJsonObject();
    }

    public static File getClientFile(XenMcGradleExtension extension) {
        return new File(getGameDir(extension), extension.getMinecraft().version + File.separator + extension.getMinecraft().version + "-client.jar");
    }

    public static File getGameDir(XenMcGradleExtension extension) {
        File game = new File(extension.getUserCache(), "game");
        if (!game.exists()) {
            game.mkdir();
        }
        return game;
    }

    public static String getClientJarSha1(XenMcGradleExtension extension) {
        return getDownloadsJson(extension).getAsJsonObject().get("client").getAsJsonObject().get("sha1").getAsString();
    }

    public static byte[] getClientJar(XenMcGradleExtension extension) {
        return readFile(getDownloadsJson(extension).getAsJsonObject().get("client").getAsJsonObject().get("url").getAsString());
    }

    public static File getClientCleanFile(XenMcGradleExtension extension) {
        return new File(getGameDir(extension), extension.getMinecraft().version + File.separator + extension.getMinecraft().version + "-client-clean.jar");
    }

}
