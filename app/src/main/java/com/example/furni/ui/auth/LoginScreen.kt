package com.example.furni.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Furni", style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Welcome back")
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(email, { email = it }, label = { Text("Email") },
            singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(password, { password = it }, label = { Text("Password") },
            singleLine = true, visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))
        Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) { Text("Log In") }
        TextButton(onClick = onRegisterClick) { Text("Don't have an account? Register") }
    }
}