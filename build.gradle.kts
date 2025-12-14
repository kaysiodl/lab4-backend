plugins {
    java
    war
}

group = "ru.kaysiodl"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("jakarta.enterprise:jakarta.enterprise.cdi-api:4.1.0")
    compileOnly("jakarta.ws.rs:jakarta.ws.rs-api:4.0.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    compileOnly("jakarta.transaction:jakarta.transaction-api:2.0.1")
    compileOnly("jakarta.platform:jakarta.jakartaee-api:10.0.0")

    implementation("org.hibernate.orm:hibernate-core:7.1.8.Final")
    compileOnly("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.mindrot:jbcrypt:0.4")

    implementation("jakarta.validation:jakarta.validation-api:4.0.0-M1")
    implementation("org.hibernate:hibernate-core:5.6.15.Final")
    implementation("org.hibernate:hibernate-entitymanager:5.6.15.Final")
    implementation("org.postgresql:postgresql:42.7.7")

    implementation("org.slf4j:slf4j-api:2.0.16")
    compileOnly ("org.projectlombok:lombok:1.18.20")
    annotationProcessor ("org.projectlombok:lombok:1.18.20")

    implementation("com.google.code.gson:gson:2.13.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    archiveFileName.set("web4.war")
}