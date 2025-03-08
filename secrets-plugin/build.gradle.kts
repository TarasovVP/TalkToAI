plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("secretsPlugin") {
            id = "com.vnteam.secrets"
            implementationClass = "com.vnteam.SecretsPlugin"
        }
    }
}

repositories {
    mavenCentral()
}