package com.example.uas_pam_162.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.uas_pam_162.BukuApplications
import com.example.uas_pam_162.ui.viewmodel.buku.HomeBukuViewModel
import com.example.uas_pam_162.ui.viewmodel.buku.InsertBukuViewModel

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer { HomeBukuViewModel(aplikasiKontak().container.faranRepository) }
        initializer { InsertBukuViewModel(aplikasiKontak().container.faranRepository) }
    }
}

fun CreationExtras.aplikasiKontak(): BukuApplications =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as BukuApplications)