// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.google.devtools.ksp) apply false
  alias(libs.plugins.roborazzi) apply false
  alias(libs.plugins.secrets) apply false
  alias(libs.plugins.google.services) apply false
}

// Automatically ensure the port 3000 preview server (server.js) is running when Gradle runs in AI Studio
try {
  val serverScript = file("${rootDir}/server.js")
  if (serverScript.exists()) {
    val socketOpen = try {
      java.net.Socket("127.0.0.1", 3000).use { true }
    } catch (_: Throwable) {
      false
    }
    if (!socketOpen) {
      ProcessBuilder("node", serverScript.absolutePath)
        .directory(rootDir)
        .redirectOutput(java.io.File("/tmp/devserver.log"))
        .redirectError(java.io.File("/tmp/devserver.log"))
        .start()
    }
  }
} catch (_: Throwable) {
  // Ignore on CI or environments without Node
}
