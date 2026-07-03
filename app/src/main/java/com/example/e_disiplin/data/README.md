# Data Layer Documentation

This directory contains the data layer of the application, which is responsible for managing data retrieval, storage, and synchronization with external sources.

## Flow & Architecture
The application follows the **Repository Pattern**. 
- The `repository/FirebaseRepository.kt` acts as the single source of truth for the application's data.
- It abstracts away the complex Firebase Firestore queries (getting, adding, updating, and deleting documents).
- ViewModels from the UI layer interact exclusively with this repository rather than calling Firebase directly.

## Key Components
- **`FirebaseRepository.kt`**: Contains methods mapped to specific collections in Firestore (`admin`, `mahasiswa`, `pelanggaran`, `kategori_pelanggaran`). It provides asynchronous functions (`suspend fun`) and real-time streams (`Flow`) to listen for data updates.
