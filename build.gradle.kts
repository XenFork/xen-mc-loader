import union.xenfork.xenmc.XenGradle
import union.xenfork.xenmc.ext.MinecraftExtension
import union.xenfork.xenmc.ext.XenMcExtension

plugins {
    `java-library`
    `maven-publish`
    idea
}

//configure<MinecraftExtension> {
//    this.versions = listOf("1.20.1")
//}

apply {
    plugin("xml-gradle")
}

configure<XenMcExtension> {
    isLoader = true
}

configure<MinecraftExtension> {
    versions = listOf("1.21", "1.21.1")
}

val mavenGroup:String by rootProject

subprojects {
    apply {
        plugin("java-library")
        plugin("maven-publish")
        plugin("idea")
//        plugin("xml-gradle")
    }
}

allprojects {
    group = mavenGroup
    java {
        targetCompatibility = JavaVersion.VERSION_21
        sourceCompatibility = JavaVersion.VERSION_21
    }
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://www.jitpack.io")
        }
        maven {
            url = uri("https://maven.fabricmc.net/")
        }
    }

}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        create<MavenPublication>("a") {
            from(components["java"])
        }
    }
}