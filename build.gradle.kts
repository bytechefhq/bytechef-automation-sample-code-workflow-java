plugins {
    id("java")
}

group = "org.sample"
version = "1.0.0"

repositories {
    mavenCentral()

    flatDir {
        dirs("lib")
    }
}

dependencies {
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")

    implementation("com.google.auto.service:auto-service:1.1.1")
    implementation(files("libs/workflow-api-1.0.jar"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveBaseName.set("sample-code-project-java")
}
