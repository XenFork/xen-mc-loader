package union.xenfork.xenmc.resources;

import lombok.val;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;

public enum PathEnum {
    xenDir("xen-mc", true),
    versionDir(xenDir, "versions", true),
    assetDir(xenDir, "assets", true),
    assetsIndexDir(assetDir, "indexes", true),
    assetsObjectsDir(assetDir, "objects", true),
    assetsLogConfigDir(assetDir, "log_config", true),
    assetsSkhemeDir(assetDir, "skins", true),
    modsDir(xenDir, "mods", true),
    ;
    private final PathEnum anEnum;
    private final String path;
    private final boolean isDir;
    
    PathEnum(String path, boolean isDir) {
        this.anEnum = null;
        this.path = path;
        this.isDir = isDir;
    }
    PathEnum(PathEnum anEnum,String path, boolean isDir) {
        this.anEnum = anEnum;
        this.path = path;
        this.isDir = isDir;
    }


    public Directory dir(Directory dir) {
        val directory = anEnum != null ? anEnum.dir(dir) : dir;
        if (isDir) return directory.dir(path);
        else return dir;
    }
    public Directory dir(DirectoryProperty dir) {
        val directory = anEnum != null ? anEnum.dir(dir) : dir.get();
        if (isDir) return directory.dir(path);
        else return dir.get();
    }

}
