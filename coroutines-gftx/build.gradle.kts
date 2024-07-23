import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.maven.publish)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

mavenPublishing {
    coordinates("com.gft.mobile", "coroutines", "1.0.1")

    pom {
        name.set("Coroutines GFTX")
        description.set("A set of tools, add-ons, and extension functions that enhance capabilities and streamline working with Kotlin Coroutines.")
        inceptionYear.set("2024")
        url.set("https://github.com/gft-mobile-pl/coroutines-gftx")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                name.set("GFT Mobile PL")
            }
        }
        scm {
            url.set("https://github.com/gft-mobile-pl/coroutines-gftx")
            connection.set("scm:git:git://github.com/gft-mobile-pl/coroutines-gftx")
            developerConnection.set("scm:git:ssh://git@github.com/gft-mobile-pl/coroutines-gftx.git")
        }
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()
    }
}

dependencies {
    implementation(libs.coroutines.core)
}
