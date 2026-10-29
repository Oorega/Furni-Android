package com.example.furni.ui.auth

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furni.data.database.FurniDatabase
import com.example.furni.data.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val database: FurniDatabase,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    private fun saveSession(userId: Long) {
        val prefs = context.getSharedPreferences("furni_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("logged_in_user_id", userId).apply()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)

            try {
                if (email.isBlank() || password.isBlank()) {
                    throw Exception("Email and password are required")
                }

                val user = database.userDao().login(email, password)

                if (user != null) {
                    saveSession(userId = user.userId!!.toLong())
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                } else {
                    throw Exception("Invalid email or password")
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Login failed"
                )
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)

            try {
                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                    throw Exception("All fields are required")
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    throw Exception("Please enter a valid email address")
                }

                val nameParts = name.trim().split(" ", limit = 2)
                val firstName = nameParts[0]
                val lastName = if (nameParts.size > 1) nameParts[1] else ""

                val newUser = User(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password
                )

                val newUserId = database.userDao().insertUser(newUser)

                saveSession(newUserId)

                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Registration failed. Email may already be in use."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null, isSuccess = false)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)