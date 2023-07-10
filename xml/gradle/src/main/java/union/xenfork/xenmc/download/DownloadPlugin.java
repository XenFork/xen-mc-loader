package union.xenfork.xenmc.download;

import org.gradle.api.Project;
import org.gradle.api.plugins.PluginAware;
import union.xenfork.xenmc.download.thread.Download;
import union.xenfork.xenmc.download.thread.SpeedListener;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPlugin;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;

import java.util.concurrent.*;

public class DownloadPlugin implements BootstrappedPlugin, BootstrappedPluginProject {
    @Override
    public void apply(PluginAware target, MinecraftExtension extension) {
        if (target instanceof Project project) {
            apply(project, extension);
        }
    }

    @Override
    public void apply(Project project, MinecraftExtension minecraft) {
        Utils.setupMessagePrefix(project, minecraft);
        project.getLogger().lifecycle("test-download");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread manifest = new Thread(new Download("manifest", "manifest.json", minecraft.manifestUrl,
                minecraft.xenmc).addSpeedListener((speed, progress) -> {
            minecraft.xenmc.project.getLogger().lifecycle("%.2f m/s-- progress: %.2f %%".formatted((double) speed / 1024 / 1024, progress));
        }));
        manifest.start();

        while (manifest.isAlive()) {
        }
    }
}
