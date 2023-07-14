package union.xenfork.xenmc.step.s2;

import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import org.gradle.api.Project;
import union.xenfork.xenmc.extensions.MinecraftExtension;
import union.xenfork.xenmc.gradle.BootstrappedPluginProject;
import union.xenfork.xenmc.gradle.Utils;

import java.io.File;

public class TableRWD implements BootstrappedPluginProject {
    @Override
    public void apply(Project project, MinecraftExtension minecraft) throws Exception {
        Utils.setupMessagePrefix(project, minecraft);
        if (minecraft.xenmc.remapTypesOf.equals("csv")) {
            CsvWriter writer = CsvUtil.getWriter(new File(minecraft.xenmc.remapTypesDir, "1.csv"), CharsetUtil.CHARSET_UTF_8);
            writer.write(
                    new String[] {
                            "a1",
                            "b1",
                            "c1"
                    },
                    new String[] {
                            "a2",
                            "b2",
                            "c2"
                    }
            );
        }

    }
}
