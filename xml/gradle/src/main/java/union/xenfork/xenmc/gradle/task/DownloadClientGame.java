package union.xenfork.xenmc.gradle.task;

import org.apache.commons.io.FileUtils;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;

import static union.xenfork.xenmc.gradle.util.MinecraftImpl.*;
import static union.xenfork.xenmc.gradle.util.Other.fileVerify;
import static union.xenfork.xenmc.gradle.util.Other.getLocalJar;

public class DownloadClientGame extends CTask {
    @TaskAction
    public void downloadClient() {
        File clientJar = getClientFile(extension);
        try {
            if (!fileVerify(clientJar, getClientJarSha1(extension))) {
                File localJar = getLocalJar(extension.getMinecraft().version);
                if (fileVerify(localJar, getClientJarSha1(extension))) {
                    getLogger().info(String.format("Copy local client.jar form %s", clientJar.getAbsolutePath()));
                    FileUtils.copyFile(localJar, clientJar);
                } else {
                    getLogger().info("Download client.jar");
                    FileUtils.writeByteArrayToFile(clientJar, getClientJar(extension));
                }
            }
        } catch (IOException e) {
            getProject().getLogger().lifecycle(e.getMessage(), e);
        }
    }
}
