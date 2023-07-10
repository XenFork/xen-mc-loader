package union.xenfork.xenmc.gradle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.gradle.api.Project;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.extensions.XenMcExtension;

import java.io.File;

public class Utils {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static void setupMessagePrefix(Project project, MinecraftExtension minecraft) {
        project.getLogger().lifecycle("--------------------------");
        project.getLogger().lifecycle("setup %d:".formatted(minecraft.setup));
        minecraft.setup++;
    }

    public static File getFile(String saveDir, String fileName, XenMcExtension xenmc) {
        return new File(xenmc.cacheHome, saveDir + File.separator + fileName);
    }
}
