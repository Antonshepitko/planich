package com.example.warhammer.ui.instructions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsageInstructionsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Инструкция по использованию") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Инструкция по использованию приложения",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "1. Чтобы добавить нового юнита, нажмите на кнопку \"+\" в нижнем правом углу экрана.\n\n" +
                        "2. Заполните все поля в форме добавления, включая выбор фракции и изображения.\n\n" +
                        "3. После сохранения вы вернётесь на главный экран, где сможете увидеть добавленного юнита.\n\n" +
                        "4. Для фильтрации юнитов по фракциям используйте кнопки в нижней части экрана.\n\n" +
                        "5. Для поиска юнитов по имени воспользуйтесь полем поиска в верхней части экрана.\n\n" +
                        "6. Для просмотра детальной информации о юните нажмите на его карточку.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}