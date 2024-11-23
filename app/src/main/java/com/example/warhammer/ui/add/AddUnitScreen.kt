package com.example.warhammer.ui.add

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.warhammer.R
import com.example.warhammer.data.UnitEntity
import com.example.warhammer.viewmodel.UnitViewModel
import com.example.warhammer.viewmodel.ViewModelFactory
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import coil3.compose.rememberAsyncImagePainter
import com.example.warhammer.data.Fraction
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUnitScreen(
    navController: NavHostController,
    database: com.example.warhammer.data.AppDatabase,
    factory: ViewModelFactory = ViewModelFactory(database),
    unitViewModel: UnitViewModel = viewModel(factory = factory)
) {
    var name by remember { mutableStateOf("") }
    var speed by remember { mutableStateOf("") }
    var weaponSkill by remember { mutableStateOf("") }
    var ballisticSkill by remember { mutableStateOf("") }
    var strength by remember { mutableStateOf("") }
    var toughness by remember { mutableStateOf("") }
    var healthPoints by remember { mutableStateOf("") }
    var numberOfAttacks by remember { mutableStateOf("") }
    var leadership by remember { mutableStateOf("") }
    var saveThrow by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var expanded by remember { mutableStateOf(false) }
    var selectedFraction by remember { mutableStateOf<Fraction?>(null) }

    val availableImages = listOf(
        R.drawable.unit1,
        R.drawable.unit2,
        R.drawable.unit3,
        R.drawable.unit4,
        R.drawable.unit5
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Добавить юнита") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (name.isBlank() || speed.isBlank() || weaponSkill.isBlank() || ballisticSkill.isBlank() ||
                    strength.isBlank() || toughness.isBlank() || healthPoints.isBlank() || numberOfAttacks.isBlank() ||
                    leadership.isBlank() || saveThrow.isBlank() || selectedFraction == null || selectedImageUri == null
                ) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Пожалуйста, заполните все поля и выберите изображение и фракцию")
                    }
                } else {
                    val newUnit = UnitEntity(
                        name = name,
                        imageUri = selectedImageUri.toString(),
                        speed = speed.toInt(),
                        weaponSkill = weaponSkill.toInt(),
                        ballisticSkill = ballisticSkill.toInt(),
                        strength = strength.toInt(),
                        toughness = toughness.toInt(),
                        healthPoints = healthPoints.toInt(),
                        numberOfAttacks = numberOfAttacks.toInt(),
                        leadership = leadership.toInt(),
                        saveThrow = saveThrow.toInt(),
                        fraction = selectedFraction!!
                    )
                    unitViewModel.addUnit(newUnit)
                    navController.popBackStack()
                }
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Сохранить")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Имя юнита") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = speed,
                onValueChange = { speed = it },
                label = { Text("Скорость") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = weaponSkill,
                onValueChange = { weaponSkill = it },
                label = { Text("Навык ближнего боя") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = ballisticSkill,
                onValueChange = { ballisticSkill = it },
                label = { Text("Навык дальнего боя") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = strength,
                onValueChange = { strength = it },
                label = { Text("Сила") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = toughness,
                onValueChange = { toughness = it },
                label = { Text("Стойкость") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = healthPoints,
                onValueChange = { healthPoints = it },
                label = { Text("Очки здоровья") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = numberOfAttacks,
                onValueChange = { numberOfAttacks = it },
                label = { Text("Кол-во атак") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = leadership,
                onValueChange = { leadership = it },
                label = { Text("Лидерство") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = saveThrow,
                onValueChange = { saveThrow = it },
                label = { Text("Спас бросок") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedFraction?.displayName ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Фракция") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Fraction.values().forEach { fraction ->
                        DropdownMenuItem(
                            text = { Text(fraction.displayName) },
                            onClick = {
                                selectedFraction = fraction
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Выберите изображение юнита:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { imagePickerLauncher.launch("image/*") }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "Выбранное изображение",
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.unit2),
                            contentDescription = "Добавить фотографию",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
