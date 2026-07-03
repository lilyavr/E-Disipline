# Util Layer Documentation

This directory contains utility classes and helper functions that provide common, cross-cutting functionality for the rest of the application.

## Flow & Architecture
Utilities are designed to be stateless and reusable. They are imported by ViewModels or UI components to perform specific, isolated tasks that don't belong in the core business logic.

## Key Components
- **`ExportManager.kt`**: A robust utility for exporting application data (like student violation statistics) into downloadable formats (PDF and Excel `.xlsx`). It uses Java IO, Android's `PdfDocument`, and `org.apache.poi` for Excel generation.
