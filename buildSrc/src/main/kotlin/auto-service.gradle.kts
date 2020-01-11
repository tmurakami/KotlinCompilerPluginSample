plugins {
    id("kotlin-jvm-convention")
    `kotlin-kapt`
}

dependencies {
    "com.google.auto.service:auto-service:1.0-rc6".let {
        compileOnly(it)
        kapt(it)
    }
}

kapt.includeCompileClasspath = false
