package union.xenfork.xenmc.extensions;

import union.xenfork.xenmc.download.manifest.ManifestGson;
import union.xenfork.xenmc.download.minecraft.MinecraftVersionGson;

import java.io.File;

public class MinecraftExtension {
    public String version;
    public String manifestUrl = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    public String assets = "https://resources.download.minecraft.net/";
    public String libraries = "https://libraries.minecraft.net/";
    public int setup = 1;
    public XenMcExtension xenmc;
    public static File manifestFile;
    public static File versionJsonFile;
    public static File assetsDir;
    public static File assetsIndexFile;
    public static File assetsObjectsDir;
    public static ManifestGson manifest = null;
    public static MinecraftVersionGson versionSet = null;

}
