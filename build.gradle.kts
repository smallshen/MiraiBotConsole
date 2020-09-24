import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    kotlin("jvm") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "6.0.0"
    `maven-publish`
    maven
}

group = "com.github.smallshen"
version = "2.5.2"

var ps :org.jetbrains.kotlin.konan.properties.Properties? = null

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    jcenter()
}


dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.HyDevelop:HyConfigLib:3.1.52")
    implementation("net.mamoe:mirai-core-qqandroid:1.3.0")
    implementation("org.hydev:HyLogger:2.1.0.378")
}

tasks.withType<Jar> {
    manifest {
        attributes(Pair("Main-Class", "com.github.smallshen.miraibot.BotConsoleKt"))
    }
}


val sourcesJar by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    ps = loadProperties("${projectDir}/private.properties")
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.xiaoshen"
            artifactId = "miraibotconsole"
            version = version
            artifact(tasks["sourcesJar"])
            from(components["java"])

        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.jetbrains.space/wmstudio/p/miraibotconsole/maven")
            credentials {
                username = "${ps!!["username"]}"
                password = "${ps!!["password"]}"
            }
        }
    }
}



artifacts {
    archives(sourcesJar)
}
