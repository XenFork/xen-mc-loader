package union.xenfork.xenmc.gradle.task;

import org.apache.commons.io.FileUtils;
import org.gradle.api.tasks.TaskAction;
import union.xenfork.xenmc.gradle.util.MappingImpl;
import union.xenfork.xenmc.gradle.util.Other;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static union.xenfork.xenmc.gradle.util.MappingImpl.getClientMapping;
import static union.xenfork.xenmc.gradle.util.MappingImpl.getClientMappingSha1;

public class DownloadMapping extends CTask {
    @TaskAction
    public void downloadMapping() {
        try {
            File clientMappingFile = MappingImpl.getClientMappingFile(extension);
            if (!Other.fileVerify(clientMappingFile, getClientMappingSha1(extension))) {
                getProject().getLogger().lifecycle("Download client.txt");
                FileUtils.write(clientMappingFile, getClientMapping(extension), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            getProject().getLogger().lifecycle(e.getMessage(), e);
        }

    }
}
