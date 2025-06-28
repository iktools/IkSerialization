plugins {
    kotlin("jvm")
    alias(libs.plugins.vanniktech)
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    implementation(project(":core"))
    implementation(libs.ksp)
    implementation(libs.bundles.kotlinpoet)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(group.toString(), "serialization-processor", version.toString())

    pom {
        name = "serialization-processor"
        description = "Serialization library that handles circular references and more with just annotations."
        inceptionYear = "2025"
        url = "https://github.com/iktools/IkSerialization"
        licenses {
            license {
                name = "MIT License"
                url = "https://mit-license.org/"
                distribution = "https://mit-license.org/"
            }
        }
        developers {
            developer {
                id = "iktools"
                name = "IK Tools"
                url = "https://github.com/iktools/"
            }
        }
        scm {
            url = "https://github.com/iktools/IkSerialization"
            connection = "scm:git:git://github.com/iktools/IkSerialization.git"
            developerConnection = "scm:git:ssh://git@github.com/iktools/IkSerialization.git"
        }
    }
}