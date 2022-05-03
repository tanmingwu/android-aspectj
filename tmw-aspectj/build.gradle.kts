plugins {
    id("groovy")
    id("java-gradle-plugin")
    kotlin("jvm")
    kotlin("kapt")
    id("maven")
}

group = "com.tmw.plugin"
version = "1.1.0"

tasks {
    "uploadArchives"(Upload::class) {
        repositories {
            withConvention(MavenRepositoryHandlerConvention::class) {
                mavenDeployer {
                    withGroovyBuilder {
                        "repository"("url" to uri("../repo"))
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
    implementation(gradleKotlinDsl())
    implementation("com.android.tools.build:gradle:4.1.1")
    implementation("org.aspectj:aspectjtools:1.9.6")
    implementation("org.aspectj:aspectjrt:1.9.6")
}

sourceSets {
    main {
        java {
            srcDir("src/main/java")
        }

        resources {
            srcDir("src/main/resources")
        }
    }
}

repositories {
    mavenCentral()
}