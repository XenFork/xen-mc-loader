package union.xenfork.xenmc.gradle.config;

import groovy.lang.GroovyObjectSupport;

import java.io.File;
import java.util.Collections;
import java.util.Set;

public class MinecraftConfiguration extends GroovyObjectSupport {
    public String manifest = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    public String assets = "https://resources.download.minecraft.net";
    public String libraries = "https://libraries.minecraft.net/";

    public String runClientDir = "runClient";
    public String runServerDir = "runServer";

    public void setRunClientDir(String runClientDir) {
        this.runClientDir = runClientDir;
    }

    public String getRunClientDir() {
        return runClientDir;
    }

    public void setRunServerDir(String runServerDir) {
        this.runServerDir = runServerDir;
    }

    public String getRunServerDir() {
        return runServerDir;
    }

    public String version = null;
    public String mainClass = "net.minecraft.launchwrapper.Launch";
    public Set<String> tweakClasses = Collections.emptySet();

    public File mappingClientFile, mappingServerFile;

    public void setTweakClasses(Set<String> tweakClasses) {
        this.tweakClasses = tweakClasses;
    }

    public Set<String> getTweakClasses() {
        return tweakClasses;
    }

    public File getMappingClientFile() {
        return mappingClientFile;
    }

    public File getMappingServerFile() {
        return mappingServerFile;
    }

    public void setMappingClientFile(File mappingClientFile) {
        this.mappingClientFile = mappingClientFile;
    }

    public void setMappingServerFile(File mappingServerFile) {
        this.mappingServerFile = mappingServerFile;
    }

    /**
     * @apiNote can edit to mirror lib
     * @param libraries minecraft lib
     */
    public void setLibraries(String libraries) {
        this.libraries = libraries;
    }

    public String getLibraries() {
        return libraries;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public String getAssets() {
        return assets;
    }
}
