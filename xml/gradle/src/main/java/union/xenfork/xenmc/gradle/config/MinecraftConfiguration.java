package union.xenfork.xenmc.gradle.config;

import groovy.lang.GroovyObjectSupport;

import java.util.Collections;
import java.util.Set;

public class MinecraftConfiguration extends GroovyObjectSupport {
    public String manifest = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    public String assets = "https://resources.download.minecraft.net";
    public String libraries = "https://libraries.minecraft.net/";

    public String runDir = "run";
    public String version = null;
    public String mainClass = "net.minecraft.launchwrapper.Launch";
    public Set<String> tweakClasses = Collections.emptySet();

}
