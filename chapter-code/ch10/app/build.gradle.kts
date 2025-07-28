plugins {
    id("java")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    application
}

group = "com.vibevault"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.data.jpa)
    runtimeOnly(libs.postgresql)
    
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
    implementation(libs.spring.boot.starter.security)
    
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
    
    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)
}

application {
    mainClass.set("com.vibevault.VibeVaultApplication")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}