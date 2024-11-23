package com.example.warhammer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.warhammer.data.AppDatabase
import com.example.warhammer.ui.App
import com.example.warhammer.ui.theme.WarHammerTheme

class MainActivity : ComponentActivity() {
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация Room
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "warhammer_db"
        )
            .fallbackToDestructiveMigration()
            .build()

        setContent {
            WarHammerTheme {
                App(database)
            }
        }
    }
}