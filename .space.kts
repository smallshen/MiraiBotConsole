job("gradle shadowJar") {
   gradlew("openjdk:8", "clean shadowJar")
}
