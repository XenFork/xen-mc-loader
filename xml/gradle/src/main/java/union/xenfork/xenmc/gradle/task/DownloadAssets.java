package union.xenfork.xenmc.gradle.task;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static union.xenfork.xenmc.gradle.util.mc.AssetsImpl.*;
import static union.xenfork.xenmc.gradle.util.DownloadImpl.readFile;
import static union.xenfork.xenmc.gradle.util.Other.fileVerify;

public class DownloadAssets extends CTask {
    @TaskAction
    public void downloadAssets() {
        String asset = getClientAsset(extension);
        File index = getClientIndexFile(extension);

        //download index
        try {
            if (!fileVerify(index, getClientAssetSha1(extension))) {
                FileUtils.write(index, asset, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            getProject().getLogger().lifecycle(e.getMessage(), e);
        }


        //get objects
        HashMap<String, AssetObject> objectHashMap = new HashMap<>();
        for (Map.Entry<String, JsonElement> objects : new Gson().fromJson(asset, JsonObject.class).get("objects").getAsJsonObject().entrySet()) {
            objectHashMap.put(objects.getKey(), new Gson().fromJson(objects.getValue(), AssetObject.class));
        }

        //download object
        objectHashMap.forEach((name, assetObject) -> {
            File local = getLocalClientObjectFile(assetObject.getHash());
            File objectFile = getClientObjectFile(extension, assetObject.getHash());

            try {
                if (!fileVerify(objectFile, assetObject.getHash())) {
                    if (fileVerify(local, assetObject.getHash())) {
                        try {
                            FileUtils.copyFile(local, objectFile);
                        } catch (IOException e) {
                            getProject().getLogger().lifecycle(e.getMessage(), e);
                        }
                    } else {
                        FileUtils.writeByteArrayToFile(objectFile, readFile(extension.getMinecraft().assets + "/" + assetObject.getHash().substring(0, 2) + "/" + assetObject.getHash()));
                    }
                }
            } catch (IOException e) {
                getProject().getLogger().lifecycle(e.getMessage(), e);
            }
        });


        //download native
        File nativeJarDir = getNativeJarDir(extension);
        File nativeFileDir = getNativeFileDir(extension);

        getNatives(extension).forEach(link -> {
            String name = link.substring(link.lastIndexOf("/") + 1);
            File nativeJarFile = new File(nativeJarDir, name);
            if (!nativeJarFile.exists()) {
                try {
                    FileUtils.writeByteArrayToFile(nativeJarFile, readFile(link));
                } catch (IOException e) {
                    getProject().getLogger().lifecycle(e.getMessage(), e);
                }
            }

            try(ZipFile zipFile = new ZipFile(nativeJarFile)) {
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = entries.nextElement();
                    if (zipEntry.isDirectory() || zipEntry.getName().contains("META-INF"))
                        continue;
                    FileUtils.writeByteArrayToFile(new File(nativeFileDir, zipEntry.getName()), IOUtils.toByteArray(zipFile.getInputStream(zipEntry)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static class AssetObject {
        private String hash;
        private long size;

        public String getHash() {
            return this.hash;
        }

        public long getSize() {
            return this.size;
        }
    }
}
