package com.chexup.test.todoapp.presentation.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class untuk aplikasi todo
 * Menggunakan Hilt untuk dependency injection di seluruh aplikasi
 */
@HiltAndroidApp
class TodoApplication : Application()