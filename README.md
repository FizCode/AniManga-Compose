# AniManga Info

[![AniManga Info CI](https://github.com/FizCode/AniManga/actions/workflows/fastlane_ci.yml/badge.svg)](https://github.com/FizCode/AniManga/actions/workflows/fastlane_ci.yml)

An Android app to browse anime and manga, built with Jetpack Compose. All data comes from
[MyAnimeList](https://myanimelist.net) and [AniList](https://anilist.co). The app is a programming
study project by FizCode.

> AniManga Info is not affiliated with, endorsed by or sponsored by MyAnimeList or AniList. All
> names, logos and images belong to their owners.

## Features

- **Anime home** with the current season, top airing and top ranking, and search.
- **Seasonal** anime by season and year, with a title search.
- **Manga** ranking by type and sort, with a title search.
- **Top Airing and Top Ranking** full lists with search and highlighted matches.
- **Details** for anime and manga: synopsis, information, statistics, related entries, songs, and
  the full cast, staff and characters loaded page by page.
- **Bookmarks** saved on the device, grouped by type, with search.
- **Share** a media with a link that opens it in the app.
- **Settings**
  - theme: follow the system, light or dark;
  - titling style: default, synonym, Japanese or English;
  - NSFW filter: safe, grey or not safe.
- **Onboarding** shown once, on the first launch.

Deep links look like `https://animanga.fizcode.dev/details/{mediaType}/{mediaId}`.

## Architecture

The app is split into feature modules, each with its own layers, and a few shared core modules.
Dependencies only point downwards: `app` → features → core.

```
app                      entry point, navigation host, dependency graph
feature/
  anime, seasonal,       the dashboard screens, one module each
  manga, bookmark
  ranking                top airing and top ranking lists
  search                 anime search
  mediadetails           anime and manga details, cast, staff, characters
  settings               theme, titling style, NSFW filter
  onboarding             first launch introduction
  dashboard              the bottom bar and the list of top-level tabs
core/
  common                 shared types and helpers (UiState, settings values, mappers)
  designsystem           theme, components and icons
  navigation             navigation state and the navigator
  network                HTTP clients
  mal                    MyAnimeList API: services and response classes
  database               Room: bookmarks, behind BookmarkLocalDataSource
  datastore              preferences: onboarding, theme, titling style, NSFW filter
```

Most features are split into `api` (the routes other modules navigate to) and `impl` (the
implementation). Inside an `impl` module the layers are:

| Layer | Contents |
|---|---|
| `presentation` | Compose screens, ViewModels (state exposed as `StateFlow`), UI models and mappers |
| `domain` | use cases, repository interfaces and domain models, with no Android or network types |
| `data` | repository implementations, mapping from API responses and local records to domain models |

Features never import a response class or a Room entity outside their `data` layer.

## Tech stack

- Kotlin, Jetpack Compose, Material 3
- Navigation 3
- Koin for dependency injection
- Ktor (OkHttp engine) with kotlinx.serialization
- Room, and DataStore for preferences
- Coil for images
- Chucker for inspecting network calls in debug builds
- Detekt for static analysis
- Fastlane and GitHub Actions for builds

Android: `minSdk` 24, `compileSdk` and `targetSdk` 37. JDK 21 is used on CI.

## Getting started

1. Install the latest Android Studio and JDK 21.
2. Create a MyAnimeList API client id (see the
   [MyAnimeList API documentation](https://myanimelist.net/apiconfig)).
3. Create `local.properties` in the root of the project and add the SDK path and the client id:

   ```properties
   sdk.dir=/path/to/Android/sdk
   X_MAL_CLIENT_ID=your-client-id
   ```

   `local.properties` is not committed. The client id can also be given as the `X_MAL_CLIENT_ID`
   environment variable. A debug build still builds without it, but the MyAnimeList requests fail. A
   release build refuses to be made without it.
4. Open the project in Android Studio and run the `app` configuration.

### Build variants

There are two flavors, `dev` and `prod`, and the usual `debug` and `release` build types.

| Variant | Application id |
|---|---|
| `prodRelease` (Google Play) | `dev.fizcode.animanga` |
| `prodDebug` | `dev.fizcode.animanga.debug` |
| `devRelease` | `dev.fizcode.animanga.development` |
| `devDebug` | `dev.fizcode.animanga.development.debug` |

## Tests

```bash
./gradlew testDevDebugUnitTest
```

Unit tests cover the mappers, use cases, ViewModels, the local data source and the response parsing.
`:app` also has a test that checks the whole Koin graph can be built.

## Release

The Google Play build is the `prodRelease` bundle:

```bash
./gradlew :app:bundleProdRelease
```

### Fastlane

| Lane | What it does |
|---|---|
| `test` | runs the unit tests |
| `devRelease` | builds the `dev` release APK |
| `bundle` | checks that signing is set up and builds the signed `prodRelease` bundle |
| `internal` | builds the bundle and uploads it to the internal testing track |
| `deploy` | promotes the internal testing build to production |

Uploading needs a Google Play service account key. Give its path in `PLAY_STORE_JSON_KEY_FILE`, see
the [fastlane supply setup](https://docs.fastlane.tools/actions/supply/#setup).

### Continuous integration

`.github/workflows/fastlane_ci.yml` builds the `dev` release on pushes and pull requests to
`release/*/master`. It needs the `X_MAL_CLIENT_ID` repository secret.

## Data and attribution

- Anime and manga information, lists and images come from the
  [MyAnimeList API](https://myanimelist.net/apiconfig) and the
  [AniList API](https://docs.anilist.co). Please read and follow their terms of use.
- The NSFW filter limits what the lists show, using the content rating MyAnimeList gives each media.
  It does not verify the age of the user.

## Contributing

- Branches are named `feature/FEAT-<number>`.
- Commit messages start with the type and the ticket, for example
  `[FEAT][FEAT-12] Add full cast and staff screens`. The types in use are `FEAT`, `FIX`, `REFACTOR`
  and `CHORE`.
- Run the unit tests before opening a pull request.

## License

No license has been chosen yet. Until one is added, all rights are reserved by the author.

Copyright © FizCode
