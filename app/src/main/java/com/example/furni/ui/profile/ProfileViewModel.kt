package com.example.furni.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furni.data.database.FurniDatabase
import com.example.furni.data.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val database: FurniDatabase,
    private val context: Context
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUser() {
        viewModelScope.launch {
            val prefs = context.getSharedPreferences("furni_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getLong("logged_in_user_id", -1L)

            if (userId != -1L) {
                _user.value = database.userDao().getUserById(userId)
            }
        }
    }

    fun updateProfile(
        newFirstName: String,
        newLastName: String,
        newEmail: String,
        newImagePath: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val currentUser = _user.value ?: return@launch

            val updatedUser = currentUser.copy(
                firstName = if (newFirstName.isBlank()) currentUser.firstName else newFirstName,
                lastName = if (newLastName.isBlank()) currentUser.lastName else newLastName,
                email = if (newEmail.isBlank()) currentUser.email else newEmail,
                profilePicture = newImagePath ?: currentUser.profilePicture
            )

            database.userDao().updateUser(updatedUser)

            _user.value = updatedUser

            onSuccess()
        }
    }

    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentUser = _user.value ?: return@launch
            database.userDao().deleteUser(currentUser)
            context.getSharedPreferences("furni_prefs", Context.MODE_PRIVATE).edit().clear().apply()
            _user.value = null
            onSuccess()
        }
    }
}