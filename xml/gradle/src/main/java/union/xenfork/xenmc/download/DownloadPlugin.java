package union.xenfork.xenmc.download;

import org.gradle.api.Project;
import org.gradle.api.plugins.PluginAware;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPlugin;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;

public class DownloadPlugin implements BootstrappedPlugin, BootstrappedPluginProject {
    @SuppressWarnings({"LoopConditionNotUpdatedInsideLoop", "StatementWithEmptyBody"})
    private static void accept(Thread thread) {
        while (thread.isAlive()) ;
    }

    @Override
    public void apply(PluginAware target, MinecraftExtension extension) {
        if (target instanceof Project project) {
            apply(project, extension);
        }
    }

    @Override
    public void apply(Project project, MinecraftExtension minecraft) {
        Utils.setupMessagePrefix(project, minecraft);
        project.getLogger().lifecycle("test-manifestDownload");
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }




//        File manifestFile = Utils.getFile("manifest", "manifest.json", minecraft.xenmc);
//        if (!manifestFile.exists()) {
//            Thread manifest = new Thread(new Download("manifest", "manifest.json", minecraft.manifestUrl,
//                    minecraft.xenmc).addSpeedListener((speed, progress) -> minecraft.xenmc.project.getLogger().lifecycle("%.2f m/s-- progress: %.2f %%".formatted((double) speed / 1024 / 1024, progress))));
//            manifest.start();
//            accept(manifest);
//        }
//
//        try {
//            minecraft.manifest = Utils.gson.fromJson(new BufferedReader(new FileReader(manifestFile)), ManifestGson.class);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        File idFile = Utils.getFile("versions", minecraft.version + ".json", minecraft.xenmc);
//        for (VersionGson version : minecraft.manifest.versions) {
//            String id = version.id;
//            System.out.println(id);
//            if (!idFile.exists()) {
//                Thread version_ = new Thread(new Download("versions", id+".json", version.url, minecraft.xenmc)
//                        .addSpeedListener((speed, progress) -> minecraft.xenmc.project.getLogger().lifecycle("%.2f m/s-- progress: %.2f %%".formatted((double) speed / 1024 / 1024, progress))));
//                version_.start();
//                accept(version_);
//            } else {
//                break;
//            }
//
//
//        }

    }

//    private void downloadVersions(MinecraftExtension minecraft, ArrayList<Thread> threads, String s) {
//        VersionGson version = minecraft.manifest.versions.stream().filter(versionGson -> versionGson.id.equals(s)).toList().get(0);
//        String url = version.url;
//        String id = version.id;
//        threads.add(new Thread(new Download("versions", id+".json", url, minecraft.xenmc)
//                .addSpeedListener((speed, progress) -> minecraft.xenmc.project.getLogger().lifecycle("%.2f m/s-- progress: %.2f %%".formatted((double) speed / 1024 / 1024, progress)))));
//    }
}
