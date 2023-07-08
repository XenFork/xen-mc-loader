package union.xenfork.xenmc.gradle;

import groovy.lang.Closure;
import org.gradle.api.Project;
import union.xenfork.xenmc.gradle.config.MinecraftConfiguration;
import union.xenfork.xenmc.gradle.config.MixinConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class XenMcGradleExtension {
    private final Project project;
    MinecraftConfiguration minecraft = new MinecraftConfiguration();
    MixinConfiguration mixins = new MixinConfiguration();
    String asmVersion, mixinVersion;

    public XenMcGradleExtension(Project project) {
        this.project = project;
    }

    public void setMixinVersion(String mixinVersion) {
        this.mixinVersion = mixinVersion;
    }

    public String getMixinVersion() {
        return mixinVersion;
    }

    public MinecraftConfiguration minecraft(Closure<?> closure) {
        project.configure(minecraft, closure);
        return minecraft;
    }

    public MixinConfiguration mixins(Closure<?> closure) {
        project.configure(mixins, closure);
        return mixins;
    }

    public File getUserCache() {
        return getUserCache("xenmc");
    }

    public File getUserCache(String dirname) {
        File caches = new File(new File(project.getGradle().getGradleUserHomeDir(), "caches"), dirname);
        if (!caches.exists()) caches.mkdirs();
        return caches;
    }

    public void setAsmVersion(String asmVersion) {
        this.asmVersion = asmVersion;
    }

    public String getAsmVersion() {
        return asmVersion;
    }

    public void setMinecraft(MinecraftConfiguration minecraft) {
        this.minecraft = minecraft;
    }

    public MinecraftConfiguration getMinecraft() {
        return minecraft;
    }

    public Project getProject() {
        return project;
    }
}
