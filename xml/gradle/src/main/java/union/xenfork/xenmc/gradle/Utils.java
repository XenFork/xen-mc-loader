package union.xenfork.xenmc.gradle;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.extensions.XenMcExtension;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Utils {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final File tempTranslate = new File(System.getProperty("user.dir"), ".gradle" + File.separator + "t.txt");
    public static void setupMessagePrefix(Project project, MinecraftExtension minecraft) {
        project.getLogger().lifecycle("--------------------------");
        project.getLogger().lifecycle("setup %d:".formatted(minecraft.setup));
        minecraft.setup++;
    }

    public static void setupMessagePrefix(Logger logger, MinecraftExtension minecraft) {
        logger.lifecycle("--------------------------");
        logger.lifecycle("setup %d:".formatted(minecraft.setup));
        minecraft.setup++;
    }

    public static File getFile(String saveDir, String fileName, XenMcExtension xenmc) {
        return new File(xenmc.cacheHome, saveDir + File.separator + fileName);
    }

    /**
     * @apiNote serializable json
     * @param file file
     * @param tempTranslate copy
     * @throws IOException exception
     */
    public static void serializable(File file, File tempTranslate) throws IOException {
        Path resolve = file.toPath().getParent().resolve(file.toPath().getFileName() + ".json");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempTranslate));
        bw.write(gson.toJson(gson.fromJson(new BufferedReader(new FileReader(file)), Object.class)));
        bw.close();
        Files.copy(tempTranslate.toPath(), resolve, REPLACE_EXISTING);
        //noinspection StatementWithEmptyBody
        while (!Files.exists(resolve));
    }

    public static void download(File path, int threadDownloadCount, String url) throws Exception {
        if (!path.exists()) {
            download(path.getParentFile(), threadDownloadCount, url);
        } else {
            RandomAccessFile rwd = new RandomAccessFile(path, "rwd");
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(3000);
            conn.setRequestMethod("HEAD");
            conn.connect();
            if (!(rwd.length() == conn.getContentLengthLong())) {
                rwd.close();
                conn.disconnect();
                download(path.getParentFile(), threadDownloadCount, url);
            }
        }
    }

    public static boolean isSha1(File path, String sha1) {
        if (!path.exists()) {
            return false;
        }
        return DigestUtil.sha1Hex(FileUtil.readBytes(path)).toLowerCase(Locale.ROOT).equals(sha1.toLowerCase(Locale.ROOT));
    }
}
