pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            url = uri("https://www.jitpack.io")
        }
        maven {
            url = uri("https://maven.fabricmc.net/")
        }
        mavenCentral()
        jcenter()
        mavenLocal()
    }
}
rootProject.name = "Xenon-Minecraft-Mod-Loader"
//for (final def java_p in java_projects.split(",")) {
//    def java_project = "xml/$java_p";
//    include java_project
//    project(":$java_project").name = java_project.replace("/", "-")
//}
//includeBuild("xml/gradle")
include("minecraft")
include("distributer")

