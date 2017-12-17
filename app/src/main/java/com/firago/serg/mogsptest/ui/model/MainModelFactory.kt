package com.firago.serg.mogsptest.ui.model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.firago.serg.mogsptest.domain.Repository

/**
 * Factory for inject repository and today Function in MainModel
 */
class MainModelFactory(private val repository: Repository, private val today: Today): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainModel::class.java)){
            return MainModel(repository, today) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
