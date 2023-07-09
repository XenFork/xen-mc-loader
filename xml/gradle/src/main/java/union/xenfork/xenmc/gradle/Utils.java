package union.xenfork.xenmc.gradle;

import org.gradle.api.Project;
import union.xenfork.xenmc.extensions.MinecraftExtension;

public class Utils {
    public static void setupMessagePrefix(Project project, MinecraftExtension minecraft) {
        project.getLogger().lifecycle("--------------------------");
        project.getLogger().lifecycle("setup %d:".formatted(minecraft.setup));
        minecraft.setup++;
    }
}
