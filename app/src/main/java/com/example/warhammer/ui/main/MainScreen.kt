package com.example.warhammer.ui.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.warhammer.data.Fraction
import com.example.warhammer.data.UnitEntity
import com.example.warhammer.viewmodel.AuthViewModel
import com.example.warhammer.viewmodel.UnitViewModel
import com.example.warhammer.viewmodel.ViewModelFactory
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    database: com.example.warhammer.data.AppDatabase,
    factory: ViewModelFactory = ViewModelFactory(database),
    unitViewModel: UnitViewModel = viewModel(factory = factory),
    authViewModel: com.example.warhammer.viewmodel.AuthViewModel = viewModel(factory = factory)
) {
    val filteredUnits by unitViewModel.filteredUnits.collectAsState()
    val selectedFraction by unitViewModel.selectedFraction.collectAsState()
    val searchQuery by unitViewModel.searchQuery.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Юниты", style = MaterialTheme.typography.titleMedium) },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Меню"
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Об авторе") },
                            onClick = {
                                menuExpanded = false
                                navController.navigate("aboutAuthor")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Инструкция пользователю") },
                            onClick = {
                                menuExpanded = false
                                navController.navigate("usageInstructions")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Выход") },
                            onClick = {
                                menuExpanded = false
                                authViewModel.logout()
                                navController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("addUnit")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить")
            }
        },
        bottomBar = {
            FactionSelectionBar(
                selectedFraction = selectedFraction,
                onFractionSelected = { fraction ->
                    unitViewModel.setSelectedFraction(fraction)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query -> unitViewModel.setSearchQuery(query) },
                label = { Text("Поиск по имени") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Поиск"
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (filteredUnits.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет юнитов для отображения", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredUnits, key = { it.id }) { unit ->
                        SwipeableCard(
                            unit = unit,
                            onUnitClick = {
                                navController.navigate("unit/${unit.id}")
                            },
                            onUnitDelete = { unitToDelete ->
                                unitViewModel.deleteUnit(unitToDelete)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Юнит удалён",
                                        actionLabel = "Отменить"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        unitViewModel.addUnit(unitToDelete)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UnitCard(unit: UnitEntity, onUnitClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUnitClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(unit.imageUri),
                contentDescription = unit.name,
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = unit.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SwipeableCard(
    unit: UnitEntity,
    onUnitClick: () -> Unit,
    onUnitDelete: (UnitEntity) -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        if (offsetX.value > 100f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 16.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .clip(RoundedCornerShape(8.dp))
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value > 150f) {
                                    offsetX.animateTo(
                                        targetValue = 1000f,
                                        animationSpec = tween(durationMillis = 300)
                                    )
                                    onUnitDelete(unit)
                                } else {
                                    offsetX.animateTo(
                                        targetValue = 0f,
                                        animationSpec = tween(durationMillis = 300)
                                    )
                                }
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            val newOffset = offsetX.value + dragAmount
                            if (newOffset >= 0f) {
                                coroutineScope.launch {
                                    offsetX.snapTo(newOffset)
                                }
                            }
                        }
                    )
                }
                .clickable { onUnitClick() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(unit.imageUri),
                    contentDescription = unit.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = unit.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun FactionSelectionBar(
    selectedFraction: Fraction?,
    onFractionSelected: (Fraction?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        FactionButton(
            fraction = Fraction.NECROPS,
            isSelected = selectedFraction == Fraction.NECROPS,
            onClick = { onFractionSelected(Fraction.NECROPS) }
        )
        Spacer(modifier = Modifier.width(16.dp))
        FactionButton(
            fraction = Fraction.AELDARI,
            isSelected = selectedFraction == Fraction.AELDARI,
            onClick = { onFractionSelected(Fraction.AELDARI) }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = { onFractionSelected(null) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedFraction == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
            )
        ) {
            Text("Все")
        }
    }
}

@Composable
fun FactionButton(
    fraction: Fraction,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )
    ) {
        Text(fraction.displayName)
    }
}
