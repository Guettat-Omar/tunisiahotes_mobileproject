package com.tunisiahotes.ui.home

import MaisonHoteEntity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tunisiahotes.data.database.AppDatabase
import com.tunisiahotes.data.database.MaisonRepository
import kotlinx.coroutines.flow.Flow

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MaisonRepository
    val allMaisons: Flow<List<MaisonHoteEntity>>

    init {
        val maisonDao = AppDatabase.getDatabase(application).maisonHoteDao()
        repository = MaisonRepository(maisonDao)
        allMaisons = repository.getAllMaisons()
    }
}

class HomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}