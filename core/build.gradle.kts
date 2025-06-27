import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    `maven-publish`
    id("com.vanniktech.maven.publish") version "0.30.0"
}

kotlin {
    jvmToolchain(8)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "serialization-core", version.toString())

    pom {
        name = "serialization-core"
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