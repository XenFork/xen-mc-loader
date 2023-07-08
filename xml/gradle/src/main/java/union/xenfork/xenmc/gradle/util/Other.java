package union.xenfork.xenmc.gradle.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.file.PathUtils;
import union.xenfork.xenmc.gradle.XenMcGradleExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import static union.xenfork.xenmc.gradle.util.MinecraftImpl.getMinecraftDir;

public class Other {
    public static boolean fileVerify(File file, String sha1) {
        if (!file.exists()) {
            return false;
        }
        try {

            return DigestUtils.sha1Hex(FileUtils.readFileToByteArray(file)).toLowerCase(Locale.ROOT).equals(sha1.toLowerCase(Locale.ROOT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static File getLocalJar(String version) {
        return new File(getMinecraftDir(), "versions" + File.separator + version + File.separator + version + ".jar");
    }


}
