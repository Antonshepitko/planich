package com.example.warhammer.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.warhammer.viewmodel.AuthState
import com.example.warhammer.viewmodel.AuthViewModel
import com.example.warhammer.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    database: com.example.warhammer.data.AppDatabase,
    factory: ViewModelFactory = ViewModelFactory(database),
    authViewModel: AuthViewModel = viewModel(factory = factory)
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val registerState by authViewModel.registerState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Регистрация") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (registerState) {
                is AuthState.Error -> {
                    Text(
                        text = (registerState as AuthState.Error).message ?: "Ошибка",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {}
            }
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Имя пользователя") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    authViewModel.register(username, password)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = registerState !is AuthState.Loading
            ) {
                if (registerState is AuthState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Зарегистрироваться")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Уже есть аккаунт? Войти")
            }
        }
    }

    LaunchedEffect(registerState) {
        if (registerState is AuthState.Success) {
            navController.navigate("main") {
                popUpTo("register") { inclusive = true }
            }
        }
    }
}
