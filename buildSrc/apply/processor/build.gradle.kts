plugins {
    id("com.diffplug.eclipse.apt") version "4.0.1"
    `java-library`
}

dependencies {
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    implementation("com.squareup:javapoet:1.13.0")
    implementation("cn.hutool:hutool-json:5.8.32")
    implementation(project(":apply"))
}