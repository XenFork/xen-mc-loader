package union.xenfork.xenmc.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.language.nativeplatform.internal.CompileTaskConfig;
import org.jetbrains.annotations.NotNull;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.extensions.XenMcExtension;
import union.xenfork.xenmc.step.Select;
import union.xenfork.xenmc.step.s1.over1_14_4.download.DownloadPlugin;
import union.xenfork.xenmc.step.s1.over1_14_4.download.minecraft.library.Libraries;
import union.xenfork.xenmc.step.s2.TableRWD;
import union.xenfork.xenmc.step.s3.remapping.ReMappingPlugin;
import union.xenfork.xenmc.step.s4.entrypoints.EntryPointsPlugin;
import union.xenfork.xenmc.step.s5.mapping.MappingPlugin;

import java.io.File;

import static java.io.File.separator;

public class XenMcPlugin implements Plugin<Project> {
    @Override
    public void apply(@NotNull Project target) {
        MinecraftExtension minecraft = target.getExtensions().create("minecraft", MinecraftExtension.class);
        XenMcExtension xenmc = target.getExtensions().create("xenmc", XenMcExtension.class);
        target.afterEvaluate(project -> {
            if (minecraft.version == null) throw new NullPointerException("please set minecraft version");
            xenmc.project = project;
            if (xenmc.cacheHome == null) {
                xenmc.cacheHome = new File(project.getGradle().getGradleUserHomeDir(), "caches%sxenmc".formatted(separator));
            }
            if (xenmc.projectHome == null) {
                xenmc.projectHome = project.getProjectDir();
            }
            if (xenmc.remapTypesOf == null) {
                xenmc.remapTypesOf = "csv";
            }
            if (xenmc.remapTypesDir == null) {
                xenmc.remapTypesDir = new File(xenmc.cacheHome, "remapof" + separator + minecraft.version);
            }
            minecraft.xenmc = xenmc;

            if (minecraft.xenmc.threadDownloadCount == null) minecraft.xenmc.threadDownloadCount = 10;
            RepositoryHandler repositories = project.getRepositories();
            repositories.maven(maven -> {
                maven.setUrl(minecraft.libraries);
                maven.setName("minecraft libraries maven");
            });

            repositories.maven(maven -> {
                maven.setUrl("https://chinawaremc.github.io/maven-repo/");
                maven.setName("mod loader maven");
            });
            _1144p(project, minecraft);
//            project.getDependencies().add("implementation", project.getDependencies().create(project.files(MinecraftExtension.librariesDir)));
            for (Libraries library : MinecraftExtension.versionSet.libraries) {
                if (library.rules != null) {
                    continue;
                }
                project.getDependencies().add("implementation", library.name);
            }
//            JavaCompile compileJava = (JavaCompile) project.getTasks().getByName("compileJava");
//            compileJava.doFirst(task -> {
//                File file = new File(minecraft.xenmc.projectHome, "build/resources/main");
//
//            });
        });

    }

    /**
     * @apiNote 分步骤执行
     * @apiNote  第一步,依次下载 版本检索器， 资源检索器, 下载资源，下载lib库
     * @param project project
     * @param minecraft minecraft settings
     */
    public void _1144p(Project project, MinecraftExtension minecraft) {
        try {
            for (Select value : Select.values()) {
                value.apply(project, minecraft);
            }
//            new DownloadPlugin().apply(project, minecraft);
//            new TableRWD().apply(project, minecraft);
//            new ReMappingPlugin().apply(project, minecraft);
//            new EntryPointsPlugin().apply(project, minecraft);
//            new MappingPlugin().apply(project, minecraft);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//            actions(DownloadPlugin.class, project, minecraft);

    }
    @Deprecated(forRemoval = true, since = "this reflection is deprecated")
    public void actions(Class<? extends BootstrappedPluginProject> clazz,Project target, MinecraftExtension minecraft) {
        try {
            clazz.getConstructor().newInstance().apply(target, minecraft);
        } catch (Exception e) {
            throw new RuntimeException("fail don't have ", e);
        }
    }
}
