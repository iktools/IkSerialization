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
    //testImplementation("junit:junit:4.13.2")
    //testImplementation("org.assertj:assertj-core:3.25.3")
}

tasks.test {
    useJUnitPlatform()
}
/*kotlin {
    jvmToolchain(17)
}*/