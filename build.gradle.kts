plugins {
  id("fabric-loom") version "1.10-SNAPSHOT"
  id("roundalib-gradle") version "1.0-SNAPSHOT"
}

roundalib {
  constants {
    enabled = true
    put("TEST", "Hello world!")

    createVariant("experimental")
  }
}
