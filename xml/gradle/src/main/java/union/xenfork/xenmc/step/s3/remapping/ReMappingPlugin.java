package union.xenfork.xenmc.step.s3.remapping;

import cn.hutool.core.util.XmlUtil;
import org.gradle.api.Project;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.util.Utils;

import java.io.File;

public class ReMappingPlugin implements BootstrappedPluginProject {

    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        Utils.setupMessagePrefix(project, minecraft);
        project.getLogger().lifecycle("remapping minecraft");
        File file = new File(minecraft.xenmc.remapTypesDir, "xenmc.xml");
        Document document = XmlUtil.readXML(file);
//        ClassReader classReader = new ClassReader(MinecraftExtension.clientGame.toPath(), minecraft.xenmc.projectHome.toPath().resolve("src/main/java"));
//        classReader.cfr();
    }
}
