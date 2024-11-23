package com.example.warhammer.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.warhammer.data.AppDatabase
import com.example.warhammer.navigation.NavGraph

@Composable
fun App(database: AppDatabase) {
    val navController = rememberNavController()
    NavGraph(navController = navController, database = database)
}
