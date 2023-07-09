package union.xenfork.xenmc.gradle.util.mc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import union.xenfork.xenmc.gradle.XenMcGradleExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static union.xenfork.xenmc.gradle.util.DownloadImpl.readString;
import static union.xenfork.xenmc.gradle.util.Other.gson;
import static union.xenfork.xenmc.gradle.util.mc.MinecraftImpl.*;
import static union.xenfork.xenmc.gradle.util.OsSys.isMac;
import static union.xenfork.xenmc.gradle.util.OsSys.isWin;

public class AssetsImpl {
    public static String getClientAsset(XenMcGradleExtension extension) {
        return readString(gson.fromJson(getJson(extension), JsonObject.class).get("assetIndex").getAsJsonObject().get("url").getAsString());
    }

    public static File getClientIndexFile(XenMcGradleExtension extension) {
        File file = new File(getClientIndexDir(extension), extension.getMinecraft().version + ".json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File getClientIndexDir(XenMcGradleExtension extension) {
        File index = new File(getClientAssetDir(extension), "indexes");
        if (!index.exists()) {
            index.mkdir();
        }
        return index;
    }

    public static File getClientAssetDir(XenMcGradleExtension extension) {
        File assets = new File(extension.getUserCache(), "assets");
        if (!assets.exists()) {
            assets.mkdir();
        }
        return assets;
    }

    public static String getClientAssetSha1(XenMcGradleExtension extension) {
        return gson.fromJson(getJson(extension), JsonObject.class).get("assetIndex").getAsJsonObject().get("sha1").getAsString();
    }

    public static File getNativeJarDir(XenMcGradleExtension extension) {
        File nativeJarDir = new File(getClientNativeDir(extension), "jars");
        if (!nativeJarDir.exists()) {
            nativeJarDir.mkdir();
        }
        return nativeJarDir;
    }

    public static File getClientNativeDir(XenMcGradleExtension extension) {
        File file = new File(getGameDir(extension), extension.getMinecraft().version + File.separator + extension.getMinecraft().version + "-native");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static File getNativeFileDir(XenMcGradleExtension extension) {
        File nativeFileDir = new File(getClientNativeDir(extension), "natives");
        if (!nativeFileDir.exists()) {
            nativeFileDir.mkdir();
        }
        return nativeFileDir;
    }

    public static List<String> getNatives(XenMcGradleExtension extension) {
        List<String> libraries = new ArrayList<>();

        for (JsonElement jsonElement : gson.fromJson(getJson(extension), JsonObject.class).get("libraries").getAsJsonArray()) {
            JsonObject downloads = jsonElement.getAsJsonObject().get("downloads").getAsJsonObject();
            if (downloads.has("classifiers")) {
                String name = "natives-linux";
                if (isWin()) {
                    name = "natives-windows";
                } else if (isMac()) {
                    name = "natives-macos";
                }
                JsonObject classifiers = downloads.get("classifiers").getAsJsonObject();
                if (classifiers.has(name)) {
                    libraries.add(downloads.get("classifiers").getAsJsonObject().get(name).getAsJsonObject().get("url").getAsString());
                }
            }
        }
        return libraries;
    }

    public static File getLocalClientObjectFile(String name) {
        File file = new File(getLocalClientObjectDir(), name.substring(0, 2));
        if (!file.exists()) {
            file.mkdir();
        }
        return new File(file, name);
    }

    public static File getLocalClientObjectDir() {
        return new File(getMinecraftDir(), "assets" + File.separator + "objects");
    }

    public static File getClientObjectFile(XenMcGradleExtension extension, String name) {
        File file = new File(getClientObjectDir(extension), name.substring(0, 2));
        if (!file.exists()) {
            file.mkdir();
        }
        return new File(file, name);
    }

    public static File getClientObjectDir(XenMcGradleExtension extension) {
        File index = new File(getClientAssetDir(extension), "objects");
        if (!index.exists()) {
            index.mkdir();
        }
        return index;
    }

}
