package union.xenfork.xenmc.extensions;

import union.xenfork.xenmc.over1_14_4.download.manifest.ManifestGson;
import union.xenfork.xenmc.over1_14_4.download.minecraft.MinecraftVersionGson;

import java.io.File;
import java.util.ArrayList;

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
    public static File assetsVirtual;
    public static File assetsIndexFile;
    public static File assetsObjectsDir;
    public static File game;
    public static File clientGame;
    public static File serverGame;
    public static File mapping;
    public static File clientMapping;
    public static File serverMapping;
    public static File librariesDir;
    public static ArrayList<File> librariesPaths = new ArrayList<>();
    public static ManifestGson manifest = null;
    public static MinecraftVersionGson versionSet = null;


}
