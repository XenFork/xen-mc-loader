package io.github.xenfork.xen.mc.loader.apply.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import io.github.xenfork.xen.mc.loader.apply.side.Minecraft;
import io.github.xenfork.xen.mc.loader.apply.Download;


@AutoService(Process.class)
@SupportedAnnotationTypes({
        "io.github.xenfork.xen.mc.loader.apply.Download",
        "io.github.xenfork.xen.mc.loader.apply.side.Minecraft"
})

public class ApplyProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return true;
    }
}
