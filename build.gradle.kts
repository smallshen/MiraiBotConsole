import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    kotlin("jvm") version "1.4.21"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    `maven-publish`
    maven
}

val kotlinVersion = "1.4.21"

group = "com.github.smallshen"
version = "3.2.0"

var ps: org.jetbrains.kotlin.konan.properties.Properties? = null

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    jcenter()
    maven("https://maven.pkg.jetbrains.space/wmstudio/p/miraibotconsole/maven")
    maven("https://dl.bintray.com/s1m0nw1/KtsRunner")
}


dependencies {

//    implementation("org.jetbrains.kotlin:kotlin-compiler:1.4.20")

    implementation(kotlin("stdlib-jdk8"))
    implementation( "org.jetbrains.kotlin:kotlin-script-util:$kotlinVersion")
    implementation ("org.jetbrains.kotlin:kotlin-script-runtime:$kotlinVersion")
    implementation ("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")
    implementation ("org.jetbrains.kotlin:kotlin-scripting-jsr223:$kotlinVersion")

    implementation("io.xiaoshen:commandbuilder:0.1.0")
    implementation("com.github.HyDevelop:HyConfigLib:3.1.52")
    implementation("net.mamoe:mirai-core-qqandroid:1.3.3")
    implementation("org.hydev:HyLogger:2.1.0.378")


}


tasks.withType<Jar> {
    manifest {
        attributes("Main-Class" to "com.github.smallshen.miraibot.BotConsoleKt")
    }

}


val sourcesJar by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
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
                kotlin.runCatching {
                    ps = loadProperties("${projectDir}/private.properties")
                }
                username = "${ps?.get("username")}"
                password = "${ps?.get("password")}"
            }
        }
    }
}



artifacts {
    archives(sourcesJar)
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

