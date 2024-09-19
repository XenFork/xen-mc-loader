package union.xenfork.xenmc.ext;

import lombok.Getter;
import org.gradle.api.Project;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Getter
public class MinecraftExtension {
    private final ListProperty<String> versions;
    private final Property<Boolean> isRelease;

    public MinecraftExtension(Project target) {
        isRelease = target.getObjects().property(Boolean.class).convention(true);
        versions = target.getObjects().listProperty(String.class).convention(new LinkedList<>());
    }


}
