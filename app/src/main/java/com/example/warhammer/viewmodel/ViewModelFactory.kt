package com.example.warhammer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.warhammer.data.AppDatabase

class ViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(database) as T
            modelClass.isAssignableFrom(UnitViewModel::class.java) -> UnitViewModel(database) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
