package union.xenfork.xenmc.gradle.task;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static union.xenfork.xenmc.gradle.util.mc.AssetsImpl.getClientAssetDir;
import static union.xenfork.xenmc.gradle.util.mc.AssetsImpl.getNativeFileDir;

public class GenerateIdeaRun extends CTask {
    @TaskAction
    public void genIdeaRun() {
        StringBuilder vmArgs = new StringBuilder("-Djava.library.path=" + getNativeFileDir(extension).getAbsolutePath());
        StringBuilder programArgs = new StringBuilder();

        if (extension.getMinecraft().tweakClasses != null && !extension.getMinecraft().tweakClasses.isEmpty()) {
            for (String tweakClass : extension.getMinecraft().tweakClasses) {
                programArgs.append("--tweakClass").append(" ").append(tweakClass).append(" ");
            }
        }

        File runDir = getProject().getRootProject().file(extension.getMinecraft().runClientDir);
        if (!runDir.exists()) {
            runDir.mkdir();
        }

        programArgs.append("--gameDir").append(" ").append(runDir.getAbsolutePath()).append(" ");
        programArgs.append("--assetsDir").append(" ").append(getClientAssetDir(extension)).append(" ");
        programArgs.append("--assetIndex").append(" ").append(extension.getMinecraft().version).append(" ");
        programArgs.append("--version").append(" ").append("xenmc").append(" ");
        programArgs.append("--accessToken").append(" ").append("0").append(" ");

        try {
            String idea = IOUtils.toString(Objects.requireNonNull(GenerateIdeaRun.class.getResourceAsStream("/IDEA_RUN_CONFIGURATION.xml")), StandardCharsets.UTF_8);
            idea = idea.replace("%NAME%", "XenMC Client Run");
            idea = idea.replace("%MAIN_CLASS%", extension.getMinecraft().mainClass);
            idea = idea.replace("%IDEA_MODULE%", getModule());
            idea = idea.replace("%PROGRAM_ARGS%", programArgs.toString().replaceAll("\"", "&quot;"));
            idea = idea.replace("%VM_ARGS%", vmArgs.toString().replaceAll("\"", "&quot;"));
            idea = idea.replace("%RUN_DIR%", extension.getMinecraft().runClientDir);

            String projectPath = getProject() == getProject().getRootProject() ? "" : getProject().getPath().replace(':', '_');
            File projectDir = getProject().getRootProject().file(".idea");
            File runConfigsDir = new File(projectDir, "runConfigurations");
            File runConfigs = new File(runConfigsDir, "xenmc_Client_Run" + projectPath + ".xml");
            if (!runConfigsDir.exists()) {
                runConfigsDir.mkdirs();
            }
            FileUtils.write(runConfigs, idea, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getModule() {
        Project project = getProject();
        StringBuilder module = new StringBuilder(project.getName() + ".main");

        while ((project = project.getParent()) != null) {
            module.insert(0, project.getName() + ".");
        }
        return module.toString();
    }
}
