package union.xenfork.xenmc.step;

import org.gradle.api.Project;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.step.s1.over1_14_4.download.DownloadPlugin;
import union.xenfork.xenmc.step.s2.TableRWD;
import union.xenfork.xenmc.step.s3.remapping.ReMappingPlugin;
import union.xenfork.xenmc.step.s4.entrypoints.EntryPointsPlugin;
import union.xenfork.xenmc.step.s5.mapping.MappingPlugin;

public enum Select {
    step1(new DownloadPlugin()),
    step2(new TableRWD()),
    step3(new ReMappingPlugin()),
    step4(new EntryPointsPlugin()),
    step5(new MappingPlugin()),
    ;
    private final BootstrappedPluginProject project;
    Select(BootstrappedPluginProject project) {
        this.project = project;
    }

    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        this.project.apply(project, minecraft);
    }
}
