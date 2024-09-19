

plugins {
    groovy
    id("java-gradle-plugin")
    `java-library`
    idea
    `maven-publish`
}
val gradleVersion: String by rootProject
val asmVersion: String by rootProject
val mavenGroup: String by rootProject
version = gradleVersion
group = mavenGroup

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://www.jitpack.io/")
        }
        maven {
            url = uri("https://maven.fabricmc.net/")
        }
    }
}

dependencies {
    implementation(gradleApi())

    implementation("cn.hutool:hutool-all:5.8.+")
    implementation("commons-codec:commons-codec:1.15")
    implementation("commons-io:commons-io:2.12.0")
    implementation("com.google.code.gson:gson:2.11.0")

    implementation("com.squareup:javapoet:1.13.0")

    implementation("org.apache.commons:commons-lang3:3.12.0")

    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
//    implementation(project(":apply"))
//    annotationProcessor(project(":apply:processor"))

    implementation("org.jboss.windup.decompiler:decompiler-procyon:6.3.8.Final") {
        exclude(group = "org.apache.commons", module = "commons-collections4")
    }
    implementation("org.apache.commons:commons-collections4:4.5.0-M2")
    implementation("org.ow2.asm:asm:$asmVersion")
    implementation("org.ow2.asm:asm-analysis:$asmVersion")
    implementation("org.ow2.asm:asm-commons:$asmVersion")
    implementation("org.ow2.asm:asm-tree:$asmVersion")
    implementation("org.ow2.asm:asm-util:$asmVersion")
}

gradlePlugin {
    plugins {
        create("xmlGradle") {
            id = "xml-gradle"
            implementationClass = "union.xenfork.xenmc.XenGradle"
        }

    }

}
publishing {
    repositories {
        mavenLocal()
    }
    publications {
        create<MavenPublication>("mavenPlugin") {
            from(components["java"])

            groupId = mavenGroup
            artifactId = "xml-gd"
            version = gradleVersion
        }
    }
}


