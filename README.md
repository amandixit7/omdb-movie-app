# OMDB Movie Search Application

## Overview

This is an Android application that allows users to search for movies using the OMDB API. The app supports displaying movie results in both list and grid views, with sorting options by release date and rating. It features pagination for handling large sets of results, error handling, and a smooth user interface with animations.

## Features

- **Search Movies**: Search for movies by title using the OMDB API.
- **View Options**: Toggle between list and grid view for displaying search results.
- **Sorting**: Sort results by release date or rating.
- **Pagination**: Load more results as the user scrolls.
- **Error Handling**: Display error messages and loading states.
- **Animations**: Smooth UI transitions and animations for a better user experience.

## Technical Details

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit for API calls
- **Asynchronous Operations**: Coroutines for handling background tasks
- **Data Handling**: LiveData for observing data changes
- **UI Components**: RecyclerView for displaying movie lists with custom animations

### Prerequisites

- Android Studio
- Kotlin
- Retrofit
- Coroutines
