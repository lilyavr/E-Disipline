# Domain Layer Documentation

This directory contains the domain layer of the application, defining the core business models and entities.

## Flow & Architecture
This layer is entirely independent of the UI and external frameworks (like Firebase) as much as possible. It defines the structure of the data that the application operates on. 
- The `model` package contains Kotlin data classes.
- These data classes are used across the entire application to represent the state and pass data between the repository and the UI.

## Key Components
- **`Admin.kt`**: Represents an administrator account (credentials and personal info).
- **`Mahasiswa.kt`**: Represents a student's profile, including their NIM, points, and academic details.
- **`Pelanggaran.kt`**: Represents a specific violation instance committed by a student, linking the student ID, the violation category, and status.
- **`KategoriPelanggaran.kt`**: Represents a predefined type of violation, outlining its severity (ringan, sedang, berat) and associated point penalty.
