package union.xenfork.xenmc.extensions;

import union.xenfork.xenmc.download.manifest.ManifestGson;

public class MinecraftExtension {
    public String version;
    public String manifestUrl = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    public String assets = "https://resources.download.minecraft.net";
    public String libraries = "https://libraries.minecraft.net/";
    public int setup = 1;
    public XenMcExtension xenmc;
    public ManifestGson manifest;
}
