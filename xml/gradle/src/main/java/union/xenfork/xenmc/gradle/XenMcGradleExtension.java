package union.xenfork.xenmc.gradle;

import groovy.lang.Closure;
import org.gradle.api.Project;
import union.xenfork.xenmc.gradle.config.MinecraftConfiguration;

public class XenMcGradleExtension {
    private final Project project;
    MinecraftConfiguration minecraft = new MinecraftConfiguration();
    String asmVersion, mixinVersion;

    public XenMcGradleExtension(Project project) {
        this.project = project;
    }

    public MinecraftConfiguration minecraft(Closure<?> closure) {
        project.configure(minecraft, closure);
        return minecraft;
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
