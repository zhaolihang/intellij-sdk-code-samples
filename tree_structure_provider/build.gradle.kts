// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

plugins {
  id("java")
  id("org.jetbrains.intellij") version "1.17.4"
}

group = "org.intellij.sdk"
version = "2.0.0"

repositories {
  mavenCentral()
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
}

// See https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  // version.set("2023.3.7")
//  localPath.set("D:\\wp\\git\\intellij-sdk-code-samples\\tree_structure_provider\\libs\\ideaIC-2023.3.7")
  localPath.set("D:\\wp\\git\\Bookmark-X\\local-repo\\ideaIC-2021.2.2")
  instrumentCode.set(false)
}

tasks {
  buildSearchableOptions {
    enabled = false
  }

  patchPluginXml {
    version.set("${project.version}")
    sinceBuild.set("212")
    untilBuild.set("242.*")
  }
}
