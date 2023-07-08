package union.xenfork.xenmc.gradle.task;

import org.gradle.api.tasks.TaskAction;
import union.xenfork.xenmc.gradle.util.mapping.MappingImpl;
import union.xenfork.xenmc.gradle.util.mapping.MappingUtil;
import union.xenfork.xenmc.gradle.util.mapping.RemappingUtil;

import java.io.File;

import static union.xenfork.xenmc.gradle.util.mc.MinecraftImpl.getClientCleanFile;
import static union.xenfork.xenmc.gradle.util.mc.MinecraftImpl.getClientFile;

public class Deobf extends CTask {

    @TaskAction
    public void mapping() {
        File clientCleanFile = getClientCleanFile(extension);
        File clientFile = getClientFile(extension);
        if (clientFile.exists()) {
            try {
                MappingUtil clientMappingUtil = MappingUtil.getInstance(extension.getMinecraft().mappingClientFile != null ? extension.getMinecraft().mappingClientFile : MappingImpl.getClientMappingFile(extension));
                RemappingUtil clientRemappingUtil = RemappingUtil.getInstance("deobfuscation", clientMappingUtil.getMap(true));
                clientRemappingUtil.analyzeJar(clientFile);
                clientRemappingUtil.remappingJar(clientFile, clientCleanFile);
            } catch (Exception e) {
                getProject().getLogger().lifecycle(e.getMessage(), e);
            }
        }
        if (extension.getMinecraft().mappingClientFile != null) {

        }
    }
}
