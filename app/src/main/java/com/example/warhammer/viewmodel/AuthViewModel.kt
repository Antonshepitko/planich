package com.example.warhammer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warhammer.data.AppDatabase
import com.example.warhammer.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User?) : AuthState()
    data class Error(val message: String?) : AuthState()
}

class AuthViewModel(private val database: AppDatabase) : ViewModel() {
    private val userDao = database.userDao()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    fun register(username: String, password: String) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading
            try {
                val existingUser = userDao.getUserByUsername(username)
                if (existingUser != null) {
                    _registerState.value = AuthState.Error("Пользователь уже существует")
                } else {
                    val newUser = User(username = username, password = password)
                    userDao.insertUser(newUser)
                    _registerState.value = AuthState.Success(newUser)
                }
            } catch (e: Exception) {
                _registerState.value = AuthState.Error(e.message)
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            try {
                val user = userDao.getUserByUsername(username)
                if (user == null) {
                    _loginState.value = AuthState.Error("Пользователь не найден")
                } else if (user.password != password) {
                    _loginState.value = AuthState.Error("Неверный пароль")
                } else {
                    _loginState.value = AuthState.Success(user)
                }
            } catch (e: Exception) {
                _loginState.value = AuthState.Error(e.message)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _loginState.value = AuthState.Idle
        }
    }
}
