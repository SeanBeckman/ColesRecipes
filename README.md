# Coles Recipes App

An Android application built with modern architecture to showcase a list of recipes with time-based filtering and detailed views.

## Logical Steps Taken

### 1. Project Infrastructure & Dependencies
*   **Dependency Injection**: Integrated **Hilt** for robust dependency management.
*   **GSON**: Use **GSON** for initial data deserialization though in reality this would be a network request
*   **Navigation**: Set up **Compose Navigation** with a reactive TopAppBar that handles titles and back navigation dynamically.
*   **Image Loading**: Integrated **Coil 3** for efficient, asynchronous image loading.
*   **Testing Suite**: Configured **MockK**, **Truth**, and **Robolectric**.

### 2. Data Layer & Optimization
*   **Domain Models**: Defined clean data structures for `Recipe`, `Ingredient`, and `RecipeDetails`.
*   **Local Persistence (Room)**: Implemented a *sDatabase** with a cache-first strategy.
*   **SQL Optimization**: Filtering logic is performed at the database level using a calculated total time column:
    `SELECT * FROM recipes WHERE (cookTimeAsMinutes + prepTimeAsMinutes) <= :maxTime`
*   **URL Normalization**: Implemented logic in the Use Case to prepend base URLs to relative image paths provided in the source JSON.

### 3. Business Logic (Use Cases)
*   **RecipeUseCase**: Decouples UI from the Repository. Handles image URL formatting and delegates filtering to the persistence layer.

### 4. Presentation Layer
*   **State Management**: Used `StateFlow` and `MutableSharedFlow` (for one-time events like navigation) within Hilt-injected ViewModels.
*   **Responsive UI**: implemented a grid layout that adapts between portrait (1 column) and landscape (2 columns).

### 5. UI, Branding & Accessibility
*   **Theming**: Custom-themed using **Coles Brand Colors** (Coles Red `#E01A22`).
*   **TalkBack Optimization**: 
    *   Merged card descendants in the list to provide concise recipe summaries.
    *   Grouped related details (Servings, Prep, Cook) in the detail view with semantic descriptions.
    *   Explicitly labeled ingredients for screen readers.
*   **Localization**: Extracted hardcoded UI strings into `strings.xml`.

---

## Key Decisions & Trade-offs

### Decision: Database-Level Filtering
*   **Trade-off**: Requires a slightly more complex Room setup (calculated columns/queries) compared to simple Kotlin `filter {}` though in reality the recipe list is likely to never reach a size where these performance optimisations are realised.
*   **Reasoning**: Scalability. As the recipe count grows, filtering in memory becomes inefficient. Offloading to SQLite is faster and consumes less memory. This also is better going forward as it allows for these optimisations to happen at an API level rather than on device.

### Decision: Use Case Layer
*   **Trade-off**: Adds additional boilerplate code.
*   **Reasoning**: Improves testability and follows Clean Architecture. It prevents ViewModels from knowing too much about data transformation logic (like prepending URLs).

---

## Future Improvements

1. **Bug fix**: AsyncImage is not loading the network images, I am not sure if this was an issue with the emulator or something I had not done right with the library but decided it was not worth the time to fix.
2. **UI Improvements**: Collapsible top app bar with title rather than having the title in the page content.
2. **UI Improvements**: FAB Shows the currently filtered time (EG 40 mins) currently the user has no idea how the filter is set after it is done
3. **Architecture**: Gradle modules for feature and core functionality. Currently everything is in one App module. This helps logically split up the layers and prevent cross contamination and anti patterns developing with larger teams/projects

## AI Suggestions
1. **Image Caching Policies**: Implement custom `Cache-Control` headers or a custom OkHttp client for Coil to handle offline image viewing more effectively.
2. **Pagination**: If the dataset becomes large, migrate the `LazyVerticalGrid` to use the **Paging 3** library to load data in chunks from Room.
3. **Search Functionality**: Add a search bar to the list screen leveraging SQLite's `FTS` (Full Text Search) capabilities for fast recipe name/ingredient lookups.
4. **Unit Test Coverage**: Expand tests to include UI tests (Compose Tests) and deeper edge-case testing for the Time Filter Slider.
5. **Multi-Language Support**: Fully translate `strings.xml` to support additional locales.
