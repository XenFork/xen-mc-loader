package io.github.xenfork.xen.mc.loader.apply;

import io.github.xenfork.xen.mc.loader.apply.side.Environment;

public @interface Download {
    Environment value() default Environment.ALLENV;
}
