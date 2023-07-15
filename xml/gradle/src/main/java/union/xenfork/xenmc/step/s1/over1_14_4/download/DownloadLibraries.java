package union.xenfork.xenmc.step.s1.over1_14_4.download;

import org.gradle.api.Project;
import org.gradle.internal.os.OperatingSystem;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.step.s1.over1_14_4.download.minecraft.library.Libraries;

import java.io.File;

import static cn.hutool.http.HttpDownloader.downloadFile;

public class DownloadLibraries implements BootstrappedPluginProject {
    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        System.out.println(OperatingSystem.current());



    }
}
