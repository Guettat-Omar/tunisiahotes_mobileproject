package com.tunisiahotes.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tunisiahotes.data.database.AppDatabase
import com.tunisiahotes.data.database.MaisonRepository
import com.tunisiahotes.data.entity.MaisonHoteEntity

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MaisonRepository

    init {
        val maisonDao = AppDatabase.getDatabase(application).maisonHoteDao()
        repository = MaisonRepository(maisonDao)
    }

    suspend fun getMaisonById(id: Int): MaisonHoteEntity? {
        return repository.getMaisonById(id)
    }
}

class DetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}