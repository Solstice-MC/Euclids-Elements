[![Cloudsmith](https://img.shields.io/badge/release%20hosting%20by-cloudsmith-blue?logo=cloudsmith&style=for-the-badge)](https://www.cloudsmith.com)

# About

**Euclid's Audio API** allows the game to load .opus audio files.

# Usage

`build.gradle`
```groovy
repositories {
	maven { url = "https://maven.maxhenkel.de/repository/public" }
	maven { url = "https://dl.cloudsmith.io/public/solstice-mc/artifacts/maven/" }
}
```

Then add Euclid's Elements as a dependency.

`build.gradle`
```groovy
dependencies {
	modImplementation "org.solstice.euclids-elements:euclids-audio-api:${euclids_audio_version}"
}
```
