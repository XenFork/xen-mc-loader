package union.xenfork.xenmc.ext;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class XenMcExtension {
    public final Property<Boolean> isLoader;
    public XenMcExtension(Project project) {
        isLoader = project.getObjects().property(Boolean.class).convention(false);
    }
}
