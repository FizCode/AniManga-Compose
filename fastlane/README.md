fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android test

```sh
[bundle exec] fastlane android test
```

Runs all the tests

### android devRelease

```sh
[bundle exec] fastlane android devRelease
```

Development Release

### android bundle

```sh
[bundle exec] fastlane android bundle
```

Build the signed Play bundle and check that it can be uploaded

### android internal

```sh
[bundle exec] fastlane android internal
```

Upload a new build to the Google Play internal testing track

### android deploy

```sh
[bundle exec] fastlane android deploy
```

Promote the internal testing build to production

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
