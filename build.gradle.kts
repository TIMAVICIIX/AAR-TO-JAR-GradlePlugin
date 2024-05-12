plugins {
    kotlin("jvm") version "1.9.22"
    `java-gradle-plugin`
    publishing
    `maven-publish`
    id("org.robologic.aar-to-jar") version "0.8.0-beta"
}

group = "org.robologic"
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
    plugins {
        create("hello-gradle") {
            id = "org.robologic.aar-to-jar"
            implementationClass = "org.robologic.download.Boot"
//            implementationClass = "org.robologic.download.CreateConfigures"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        create<MavenPublication>("RoboPublisher") {

            groupId = "org.robologic"
            artifactId = "aar-to-jar"
            version = "0.8.0-beta"

            from(components["java"])
        }
    }
}