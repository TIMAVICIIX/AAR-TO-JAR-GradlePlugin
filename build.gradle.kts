import com.vanniktech.maven.publish.SonatypeHost

buildscript {
    //So when you want to use a library in the build script itself, you must add this library on the script classpath using buildScript:
    dependencies {
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.28.0")
    }
}

plugins {
    kotlin("jvm") version "1.9.22"
    `java-gradle-plugin`
    publishing
    `maven-publish`
    id("com.vanniktech.maven.publish") version "0.28.0"
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "io.github.timaviciix"
version = "0.8.0-beta"

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.aliyun.com/repository/releases") }
    maven { url = uri("https://maven.aliyun.com/repository/google") }
    maven { url = uri("https://maven.aliyun.com/repository/central") }
    maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
    maven { url = uri("https://maven.aliyun.com/repository/public") }
}

dependencies {
    val kotlinVersion = "1.9.22"


    implementation("org.slf4j:slf4j-api:2.0.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    implementation("org.jetbrains.kotlin:kotlin-script-runtime:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    implementation(kotlin("script-runtime"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0+")

//    implementation("io.github.microutils:kotlin-logging-jvm:2.0.6")


}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

tasks.register("printMavenLocalDir") {
    doLast {
        println("本地Maven仓库位置: " + repositories.mavenLocal().url)
    }
}

gradlePlugin {
    website = "https://github.com/TIMAVICIIX/AAR-TO-JAR-GradlePlugin"
    vcsUrl = "https://github.com/TIMAVICIIX/AAR-TO-JAR-GradlePlugin.git"
    plugins {
        create("aar-to-jar-tool") {
            id = "io.github.timaviciix.robotools.aar-to-jar-tool"
            displayName = "Aar To Jar Tool"
            description = "It is an AAR file to JAR file extraction tool."
            tags = listOf("AAR", "JAR", "AAR-JAR","Gradle Building","Android-Kotlin","Kotlin")
            implementationClass = "org.robologic.download.Boot"
        }
    }
}



mavenPublishing{
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL,false)

    signAllPublications()

    pom{
        name.set("io.github.timaviciix")
        description.set("This is an AAR to JAR dump tool plugin (Gradle).")
        url.set("https://github.com/TIMAVICIIX/AAR-TO-JAR-GradlePlugin")

        licenses{
            license{
                name.set("The Apache Software License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers{
            developer {
                id.set("Robo Logic.robo")
                name.set("Robo Timaviciix")
                email.set("timaviciix@outlook.com")
                roles.set(listOf("Project Manager","Architect","Robo Tools Original Developer"))
            }
        }

        scm{
            connection.set("https://github.com/TIMAVICIIX/AAR-TO-JAR-GradlePlugin.git")
            developerConnection.set("sdm:git:ssh://git@github.com:TIMAVICIIX/TIMAVICIIX.git")
            url.set("https://github.com/TIMAVICIIX/AAR-TO-JAR-GradlePlugin.git")
        }
    }

}

//publishing{
//    publications{
//        register("Robo",MavenPublication::class.java){
//            groupId="io.github.timaviciix"
//            artifactId="robo-tools_aar-to-jar"
//            version="0.8.0-beta"
//
//            from(components["java"])
//
//            pom{
//                name.set("io.github.timaviciix")
//                description.set("This is an AAR to JAR dump tool plugin (Gradle).")
//                url.set("https://github.com/TIMAVICIIX/AAR-TO-JAR-GradlePlugin")
//
//                licenses{
//                    license{
//                        name.set("The Apache Software License, Version 2.0")
//                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
//                    }
//                }
//
//                developers{
//                    developer {
//                        id.set("Robo Logic.robo")
//                        name.set("Robo Timaviciix")
//                        email.set("timaviciix@outlook.com")
//                        roles.set(listOf("Project Manager","Architect","Robo Tools Original Developer"))
//                    }
//                }
//
//                scm{
//                    connection.set("https://github.com/TIMAVICIIX/AAR-TO-JAR-GradlePlugin.git")
//                    developerConnection.set("sdm:git:ssh://git@github.com:TIMAVICIIX/TIMAVICIIX.git")
//                    url.set("https://github.com/TIMAVICIIX/AAR-TO-JAR-GradlePlugin.git")
//                }
//            }
//        }
//    }
//}


//tasks.register<Jar>("sourceJar"){
//    archiveClassifier.set("source")
//    from(sourceSets["main"].kotlin)
//}
//
//publishing{
//    publications.getByName("Robo",MavenPublication::class).apply {
//        artifact(tasks.getByName("sourceJar"))
//    }
//    repositories{
//        maven{
//            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
//            credentials{
//                username = project.findProperty("mavenCentralUsername") as String? ?: ""
//                password=project.findProperty("mavenCentralPassword") as String? ?: ""
//            }
//
//        }
//    }
//}

//publishing {
//    repositories {
//        mavenLocal()
//    }
//    publications {
//        create<MavenPublication>("RoboPublisher") {
//
//            groupId = "org.robologic"
//            artifactId = "aar-to-jar"
//            version = "0.8.0-beta"
//
//            from(components["java"])
//        }
//    }
//}