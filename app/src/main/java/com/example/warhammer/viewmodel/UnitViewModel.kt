// app/src/main/java/com/example/warhammer/viewmodel/UnitViewModel.kt
package com.example.warhammer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warhammer.data.AppDatabase
import com.example.warhammer.data.Fraction
import com.example.warhammer.data.UnitEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UnitViewModel(private val database: AppDatabase) : ViewModel() {
    private val unitDao = database.unitDao()

    private val _units: StateFlow<List<UnitEntity>> = unitDao.getAllUnits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val units: StateFlow<List<UnitEntity>> = _units

    private val _selectedFraction = MutableStateFlow<Fraction?>(null)
    val selectedFraction: StateFlow<Fraction?> = _selectedFraction

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredUnits: StateFlow<List<UnitEntity>> = combine(_units, _selectedFraction, _searchQuery) { units, fraction, query ->
        units.filter { unit ->
            val matchesFraction = fraction == null || unit.fraction == fraction
            val matchesQuery = unit.name.contains(query, ignoreCase = true)
            matchesFraction && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addUnit(unit: UnitEntity) {
        viewModelScope.launch {
            unitDao.insertUnit(unit)
        }
    }

    fun updateUnit(unit: UnitEntity) {
        viewModelScope.launch {
            unitDao.updateUnit(unit)
        }
    }

    fun deleteUnit(unit: UnitEntity) {
        viewModelScope.launch {
            unitDao.deleteUnit(unit)
        }
    }

    fun setSelectedFraction(fraction: Fraction?) {
        _selectedFraction.value = fraction
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getUnitById(id: Int): Flow<UnitEntity?> {
        return unitDao.getUnitById(id)
    }
}

