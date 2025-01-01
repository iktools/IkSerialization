plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    ksp(project(":processor"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}