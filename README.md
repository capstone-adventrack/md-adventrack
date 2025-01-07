# Adventrack
## Overview
In this era of technology, we introduce solutions to support the growth of Indonesia's tourism sector. Our app, Adventrack, aims to assist travelers in planning their best trip by providing a recommendation system and essential information. We have observed travelers' experiences and the growing trends in the tourism industry. As such, our app is designed to integrate digital innovation to improve Indonesia's economy through more effective tourism promotion.

Adventrack is an innovative application designed to provide information about tourist destinations. This application utilizes the latest technology to provide the best experience for tourists in planning their trip. With Adventrack, users can find information about tourist attractions. The main purpose of Adventrack is to make it easier for tourists to access the information needed during the trip. This application is expected to be a reliable digital guide and help users plan a pleasant trip. [Application Demo](https://drive.google.com/file/d/1LbIZmr3G8GRSwBOJBdhWYMTYU3sO0Uy0/view?usp=sharing)

![image](https://github.com/user-attachments/assets/6557cd23-6f73-4bc8-9751-366b2696d0f1)


## Features
- **Tourist Destination Information**: Discover comprehensive details about various tourist spots.
- **Recommendation System**: Get personalized recommendations for your trip.
- **User Authentication**: Secure user authentication using Firebase Auth.
- **Data Storage**: Efficient data management with Firestore Database.

## Architecture

Adventrack uses the Clean Architecture MVVM (Model-View-ViewModel) pattern for Android development to ensure a scalable, maintainable, and testable codebase.

### Layers

1. **UI Layer**:
    - Responsible for the UI and user interaction.
    - Uses ViewModel to handle data for UI.
2. **Domain Layer**:
    - Contains business logic.
    - Uses Use Cases/Interactors to coordinate between the Presentation and Data layers.
3. **Data Layer**:
    - Manages data from various sources such as Firebase Firestore and remote APIs.
    - Uses Repositories to abstract the data sources.

## Technologies

- **Kotlin**: The programming language used for Android development.
- **MVVM**: Architecture pattern for separation of concerns.
- **Firebase Auth**: For user authentication.
- **Firestore Database**: For cloud data storage.
- **Kotlin Flow**: For observing data changes.
- **Coroutines**: For asynchronous programming.

## Getting Started

### Prerequisites

- Android Studio
- Firebase account

### Setup

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your-username/adventrack.git
    ```

2. **Open the project** in Android Studio.

3. **Configure Firebase**:
    - Create a Firebase project in the Firebase Console.
    - Add your Android app to the Firebase project.
    - Download the `google-services.json` file and place it in the `app` directory.
    - Add Firebase to your app by following the setup instructions provided by Firebase.

4. **Sync the project** with Gradle files.

### Running the App

1. Connect your Android device or start an emulator.
2. Click on the "Run" button in Android Studio.

## Usage

1. **Sign Up / Sign In**:
    - Use Firebase Auth to sign up or sign in.
2. **Explore Destinations**:
    - Browse through the list of tourist destinations.
    - Get recommendations based on your preferences.
3. **Plan Your Trip**:
    - Use the app to plan your itinerary.
    - Save essential information for offline access.

## Contributing

We welcome contributions from the community. If you wish to contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit them (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a Pull Request.
