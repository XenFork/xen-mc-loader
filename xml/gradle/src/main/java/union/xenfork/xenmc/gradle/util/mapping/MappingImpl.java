package union.xenfork.xenmc.gradle.util.mapping;

import union.xenfork.xenmc.gradle.XenMcGradleExtension;

import java.io.File;
import java.io.IOException;

import static union.xenfork.xenmc.gradle.util.DownloadImpl.readString;
import static union.xenfork.xenmc.gradle.util.mc.MinecraftImpl.getDownloadsJson;

public class MappingImpl {

    public static File getMappingDir(XenMcGradleExtension extension) {
        File mapping = new File(extension.getUserCache(), "mapping");
        if (!mapping.exists()) {
            mapping.mkdirs();
        }
        return mapping;
    }
    public static File getClientMappingFile(XenMcGradleExtension extension) {
        File file = new File(getMappingDir(extension), "%s-client.txt".formatted(extension.getMinecraft().version));

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        return file;
    }

    public static String getClientMappingSha1(XenMcGradleExtension extension) {
        return getDownloadsJson(extension).getAsJsonObject().get("client_mappings").getAsJsonObject().get("sha1").getAsString();
    }

    public static String getClientMapping(XenMcGradleExtension extension) {
        return readString(getDownloadsJson(extension).getAsJsonObject().get("client_mappings").getAsJsonObject().get("url").getAsString());
    }
}
