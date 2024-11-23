package com.example.warhammer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.warhammer.data.AppDatabase
import com.example.warhammer.ui.about.AboutAuthorScreen
import com.example.warhammer.ui.add.AddUnitScreen
import com.example.warhammer.ui.auth.LoginScreen
import com.example.warhammer.ui.auth.RegisterScreen
import com.example.warhammer.ui.instructions.UsageInstructionsScreen
import com.example.warhammer.ui.main.MainScreen
import com.example.warhammer.ui.unit.UnitDetailScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
    object AddUnit : Screen("addUnit")
    object UnitDetail : Screen("unit/{unitId}") {
        fun createRoute(unitId: Int) = "unit/$unitId"
    }
    object AboutAuthor : Screen("aboutAuthor")
    object UsageInstructions : Screen("usageInstructions")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    database: AppDatabase,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController, database)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController, database)
        }
        composable(Screen.Main.route) {
            MainScreen(navController, database)
        }
        composable(Screen.AddUnit.route) {
            AddUnitScreen(navController, database)
        }
        composable(Screen.UnitDetail.route) { backStackEntry ->
            val unitId = backStackEntry.arguments?.getString("unitId")?.toIntOrNull()
            if (unitId != null) {
                UnitDetailScreen(navController, database, unitId)
            } else {
                navController.popBackStack()
            }
        }
        composable(Screen.AboutAuthor.route) {
            AboutAuthorScreen(navController)
        }
        composable(Screen.UsageInstructions.route) {
            UsageInstructionsScreen(navController)
        }

    }
}
