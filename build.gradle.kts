plugins {
  id("fabric-loom") version "1.10-SNAPSHOT"
  id("roundalib-gradle") version "1.0-SNAPSHOT"
}

tasks.shadeJar {
  treeShakeShadedClasses = false
}

roundalib {
  constants.put("TEST", "Hello world!")
  variants {
    create("experimental")
    buildAndPublishAll()
  }
}
