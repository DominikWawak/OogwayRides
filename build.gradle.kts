import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
    id("org.openjfx.javafxplugin") version "0.0.8"

}

group = "org.oogwayrides.console.main"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}


javafx {
    modules("javafx.controls", "javafx.fxml")
}


dependencies {
    testImplementation(kotlin("test"))
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation ("io.github.microutils:kotlin-logging:2.1.23")
    implementation("org.litote.kmongo:kmongo:4.7.1")
    implementation("no.tornado:tornadofx:2.0.0-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}