package com.example.furni.ui.auth

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.furni.R

@Composable
fun LoginXmlScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // لما تسجيل الدخول ينجح، روحي للصفحة الرئيسية
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onLoginSuccess()
    }

    AndroidView(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        factory = { context ->
            val view = LayoutInflater.from(context)
                .inflate(R.layout.screen_login_xml, null, false)

            val etEmail = view.findViewById<EditText>(R.id.etEmail)
            val etPassword = view.findViewById<EditText>(R.id.etPassword)

            view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
                viewModel.login(
                    email = etEmail.text.toString().trim(),
                    password = etPassword.text.toString()
                )
            }
            view.findViewById<TextView>(R.id.tvRegister)
                .setOnClickListener { onRegisterClick() }

            view
        },
        update = { view ->
            // بيتنفذ كل ما الـ state يتغير: يعرض الخطأ ويقفل الزرار أثناء التحميل
            val tvError = view.findViewById<TextView>(R.id.tvError)
            tvError.text = state.error ?: ""
            tvError.visibility = if (state.error != null) View.VISIBLE else View.GONE

            val btnLogin = view.findViewById<Button>(R.id.btnLogin)
            btnLogin.isEnabled = !state.isLoading
            btnLogin.text = if (state.isLoading) "Logging in..." else "Log In"
        }
    )
}