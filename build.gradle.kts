plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.22"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
    id("org.jetbrains.kotlin.kapt") version "1.9.25"
}

group = "won"
version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.microsoft.playwright:playwright:1.44.0")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")

    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")

    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.rest-assured:rest-assured:5.5.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<ProcessResources>("processResources") {
    dependsOn("initSetting")
}

tasks.register("initSetting") {
    group = "custom tasks"
    description = "Execute both copyHooks and copySecret tasks."

    dependsOn("copyHooks", "copySecret")
}

tasks.register<Copy>("copySecret") {
    from("./techlog-env/back")
    include("*.yml")
    into("src/main/resources")
    println("Secret files이 성공적으로 복사되었습니다.")
}

tasks.register("copyHooks") {
    group = "git hooks"
    description = "Copy pre-commit and pre-push git hooks from .githooks to .git/hooks folder."

    doLast {
        copy {
            from("$rootDir/.githooks/pre-commit")
            into("$rootDir/.git/hooks")
        }
        copy {
            from("$rootDir/.githooks/pre-push")
            into("$rootDir/.git/hooks")
        }
        file("$rootDir/.git/hooks/pre-push").setExecutable(true)
        println("Git pre-commit 및 pre-push hook이 성공적으로 등록되었습니다.")
    }
}

kapt {
    correctErrorTypes = true
}

tasks.named("compileKotlin") {
    dependsOn("kaptKotlin")
}

ktlint {
    filter {
        exclude { it.file.path.contains("build/generated") }
    }
}
