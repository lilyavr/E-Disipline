# UI Layer Documentation

This directory contains the user interface layer of the application, built entirely with **Jetpack Compose**.

## Flow & Architecture
The UI is organized using the **Model-View-ViewModel (MVVM)** architecture pattern.
- **Screens (`screens/`)**: Declarative Compose functions that define the layout and UI logic for specific pages (e.g., Dashboards, Profiles, Login).
- **ViewModels (`screens/`)**: State holders that manage the UI state, handle user interactions, and request data from the repository. They expose `StateFlow`s that the screens collect to recompose.
- **Navigation (`navigation/`)**: Defines the `NavHost` and routing configuration for moving between screens.
- **Components (`components/`)**: Reusable, atomic UI elements (like custom buttons, cards, and charts) used across multiple screens to maintain a consistent design system.
- **Theme (`theme/`)**: Defines the typography, color palette, and shapes for the application.

## Key Flows
- **Authentication**: `SelectRoleScreen` directs the user to either `AdminLoginScreen` or `MahasiswaLoginScreen`. Upon success, the session ID is stored, and the user enters the respective main portal.
- **Admin Portal**: Accessible via `AdminMainScreen`, it features a bottom navigation bar to switch between Dashboard, Data, Notification, and Profile tabs.
- **Mahasiswa Portal**: Accessible via `MahasiswaMainScreen`, it focuses on showing the student's personal violations and points.
