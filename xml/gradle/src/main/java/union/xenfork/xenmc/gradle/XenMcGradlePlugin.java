package union.xenfork.xenmc.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

public class XenMcGradlePlugin implements Plugin<Project> {
    @Override
    public void apply(@NotNull Project target) {
        XenMcGradleExtension extension = target.getExtensions().create("xenmc", XenMcGradleExtension.class, target);
        target.afterEvaluate(project -> {
            isNull(extension.minecraft.version, "minecraft version is null");
            isNull(extension.asmVersion, "asm is not have" + extension.asmVersion);
            String[] asms = new String[] {
              "asm",
              "asm-analysis",
              "asm-commons",
              "asm-tree",
              "asm-util"
            };
            dep(project, "org.ow2.asm", extension.asmVersion,
                    "asm", "asm-analysis", "asm-commons", "asm-tree", "asm-util"
            );
        });
    }

    public void isNull(Object obj, String message) {
        if (obj == null) throw new NullPointerException(message);
    }

    public void dep(Project project, String depPre, String depVersion, String... depModule) {
        if (depModule.length == 1)
            dep(project, depPre, depVersion, depModule[0]);
        else for (String module : depModule)
            dep(project, depPre, depVersion, module);
    }

    public void dep(Project project, String depPre, String depVersion, String depModule) {
        project.getDependencies().add("compileOnly", "%s:%s:%s".formatted(depPre, depModule, depVersion));
        project.getDependencies().add("testCompileOnly", "%s:%s:%s".formatted(depPre, depModule, depVersion));
        project.getDependencies().add("runtimeOnly", "%s:%s:%s".formatted(depPre, depModule, depVersion));
        project.getDependencies().add("testRuntimeOnly", "%s:%s:%s".formatted(depPre, depModule, depVersion));
    }
}


