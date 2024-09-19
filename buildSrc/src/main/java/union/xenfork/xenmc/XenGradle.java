package union.xenfork.xenmc;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFile;
import org.jetbrains.annotations.NotNull;
import union.xenfork.xenmc.bean.manifest.McToBean;
import union.xenfork.xenmc.bean.manifest.Version;
import union.xenfork.xenmc.bean.version.VersionToBean;
import union.xenfork.xenmc.ext.MinecraftExtension;
import union.xenfork.xenmc.ext.XenMcExtension;
import union.xenfork.xenmc.processor.IManifest;
import union.xenfork.xenmc.resources.PathEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static union.xenfork.xenmc.resources.PathEnum.versionDir;


public class XenGradle implements Plugin<Project>, IManifest {

    public static MinecraftExtension minecraft;
    public static XenMcExtension xenmc;
    public static McToBean mcToBean = IManifest.loadManifest("https://launchermeta.mojang.com/mc/game/version_manifest.json");
    public static final List<VersionToBean> versions = new ArrayList<>();
    @Override
    public void apply(@NotNull Project target) {
        DirectoryProperty buildDirectory = target.getRootProject().getLayout().getBuildDirectory();
        xenmc = target.getExtensions().create("xenmc", XenMcExtension.class, target);
        minecraft = target.getExtensions().create("minecraft", MinecraftExtension.class, target);
        target.afterEvaluate(project -> {
            if (xenmc.isLoader.get()) {
                Directory directory = versionDir.dir(buildDirectory);
                mcToBean.versionsAndSave(versions, directory);

            }
        });
//        Manifest.manifest(target);
//        Version.version(target);

    }
}
