package union.xenfork.xenmc.bean.manifest;

import cn.hutool.core.annotation.Alias;
import org.gradle.api.file.Directory;
import org.gradle.api.file.RegularFile;
import union.xenfork.xenmc.XenGradle;
import union.xenfork.xenmc.bean.version.VersionToBean;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class McToBean {
    @Alias("latest")
    public Latest latest;

    @Alias("versions")
    public Version[] versions;


    public Stream<Version> versions() {
        return Arrays.stream(versions).filter(v -> XenGradle.minecraft.getVersions().get().contains(v.id));
    }

    public void versions(List<VersionToBean> versions) {
        versions().forEach(v -> versions.add(v.versions()));
    }

    public void versionsAndSave(List<VersionToBean> versions, Directory dir) {
        versions(versions);
        versions.forEach(version -> version.save(dir));
    }
}
