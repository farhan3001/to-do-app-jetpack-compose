# Penjelasan Implementasi
Aplikasi Todo yang dikembangkan menggunakan arsitektur MVVM + Repository Pattern

## 🏗️ Arsitektur & Design Pattern
- MVVM (Model-View-ViewModel): Memisahkan UI logic dari business logic
- Repository Pattern: Single source of truth untuk data management
- Clean Architecture: Separation of concerns dengan layer yang jelas
- Dependency Injection: Menggunakan Hilt untuk loose coupling

## 📱 Fitur Utama

#### A. Local Storage (Room Database):
1. CRUD operations untuk todo items
2. Offline-first approach
3. Reactive data dengan Flow

#### B. Remote API Integration:
1. Retrofit untuk HTTP requests ke Mock API
2. Automatic sync dengan server
3. Error handling

#### C. State Management:
1. StateFlow untuk reactive UI
2. Compose state management
3. Loading dan error states

## 🔧 Teknologi yang Digunakan
- Jetpack Compose: Modern UI toolkit
- Room Database: Local storage
- Retrofit + OkHttp: Network layer
- Hilt: Dependency injection
- Coroutines: Asynchronous programming
- StateFlow: Reactive state management

## 🌐 Mock API Integration
Mock API digunakan untuk:
- Development: Tidak bergantung pada backend
- Testing: Simulasi berbagai skenario
- Sync Strategy: Offline-first dengan background sync

## 📂 Struktur Project
![image](https://github.com/user-attachments/assets/66268801-4ff8-43b4-8b11-f7aad5bf76e5)


## 🚀 Cara Menggunakan
1. Clone project dan sync gradle
2. Jalankan aplikasi di device/emulator
3. Add todo baru dengan FAB button
4. Toggle complete dengan checkbox
5. Delete dengan tombol delete
6. Sync dengan server menggunakan refresh button
