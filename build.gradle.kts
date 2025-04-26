plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	kotlin("plugin.jpa") version "1.9.22"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "won"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Jsoup
	implementation("org.jsoup:jsoup:1.17.2")

	// RestAssured
	testImplementation("io.rest-assured:rest-assured:5.5.0")

	// H2
	runtimeOnly("com.h2database:h2")

	// JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// MySQL
	runtimeOnly("com.mysql:mysql-connector-j")

	// logging
	implementation ("io.github.oshai:kotlin-logging-jvm:7.0.3")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
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
	include("env-common.yml")
	into("src/main/resources")
}

tasks.register("copyHooks") {
	group = "git hooks"
	description = "Copy pre-commit and pre-push git hooks from .githooks to .git/hooks folder."

	doLast {
		// pre-commit hook 복사
		copy {
			from("$rootDir/.githooks/pre-commit")
			into("$rootDir/.git/hooks")
		}
		// pre-push hook 복사
		copy {
			from("$rootDir/.githooks/pre-push")
			into("$rootDir/.git/hooks")
		}
		// pre-push hook에 실행 권한 부여
		file("$rootDir/.git/hooks/pre-push").setExecutable(true)
		println("Git pre-commit 및 pre-push hook이 성공적으로 등록되었습니다.")
	}
}
