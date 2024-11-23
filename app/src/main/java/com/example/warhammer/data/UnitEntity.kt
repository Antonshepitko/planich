package com.example.warhammer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "units")
data class UnitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUri: String,
    val speed: Int,
    val weaponSkill: Int,
    val ballisticSkill: Int,
    val strength: Int,
    val toughness: Int,
    val healthPoints: Int,
    val numberOfAttacks: Int,
    val leadership: Int,
    val saveThrow: Int,
    val fraction: Fraction
)
