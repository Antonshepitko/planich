package com.example.warhammer.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromFraction(faction: Fraction): String {
        return faction.name
    }

    @TypeConverter
    fun toFraction(faction: String): Fraction {
        return Fraction.valueOf(faction)
    }
}