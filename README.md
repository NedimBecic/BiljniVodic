# Biljni vodič #

Biljni vodic is a mobile application developed as part of the course Razvoj mobilnih aplikacija (Mobile Application Development). The project is written in Kotlin and showcases the use of various Kotlin components, Room database, and operations of fetching data using the Trefle.io API.

## Features

- **Kotlin Components**: Demonstrates the use of Kotlin-specific features and components.
- **Room Database**: Integration with Room database for local data storage and retrieval.
- **API Integration**: Fetches data from the Trefle.io API.

## Requirements

- Android Studio
- Kotlin 1.4+
- Internet connection for fetching data from the API

## Installation

1. **Clone the repository**:
    ```sh
    git clone https://github.com/yourusername/biljni-vodic.git
    ```
2. **Open the project in Android Studio**:
    - Open Android Studio
    - Select `File -> Open...`
    - Navigate to the cloned repository folder and select it

3. **Build the project**:
    - Click on the `Build` menu and select `Make Project` or press `Ctrl+F9`

4. **Run the application**:
    - Connect your Android device or start an emulator
    - Click on the `Run` button or press `Shift+F10`

## Usage

1. **Main Screen**:
    - The main screen displays a list of static plants.
    - Users can scroll through the list to view different plants.
    - Users can choose between three modes: medical, culinary and botanical.

2. **Filtering**:
    - Clicking on a plant in the list filters the list based on the specific properties of the current selected mode.

3. **Adding a plant**:
    - Users can add their own plants using a provided form.
    - The added plant is checked for mistakes and corrected, by fetching data from Trefle.io.

4. **Favorites**:
    - Users can mark plants as favorites. Favorited plants are stored locally using the Room database.
    - Users can view their list of favorite plants in a separate section.

## API Key Setup

1. **Get an API key from Trefle.io**:
    - Go to [Trefle.io](https://trefle.io) and sign up to get an API key.

2. **Add the API key to the project**:
    - Open `local.properties` in the root directory of the project.
    - Add the following line with your API key:
        ```properties
        trefleApiKey=YOUR_API_KEY_HERE
        ```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- [Trefle.io](https://trefle.io) for providing the plant data API.
- The course Razvoj mobilnih aplikacija for guidance and support.

---
