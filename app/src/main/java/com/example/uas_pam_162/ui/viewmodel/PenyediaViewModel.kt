package com.example.uas_pam_162.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.uas_pam_162.BukuApplications
import com.example.uas_pam_162.ui.viewmodel.anggota.DetailAgtViewModel
import com.example.uas_pam_162.ui.viewmodel.anggota.HomeAnggotaViewModel
import com.example.uas_pam_162.ui.viewmodel.anggota.InsertAnggotaViewModel
import com.example.uas_pam_162.ui.viewmodel.anggota.UpdateAnggotaViewModel
import com.example.uas_pam_162.ui.viewmodel.buku.DetailBukuViewModel
import com.example.uas_pam_162.ui.viewmodel.buku.HomeBukuViewModel
import com.example.uas_pam_162.ui.viewmodel.buku.InsertBukuViewModel
import com.example.uas_pam_162.ui.viewmodel.buku.UpdateBukuViewModel
import com.example.uas_pam_162.ui.viewmodel.peminjaman.DetailPeminjamanViewModel
import com.example.uas_pam_162.ui.viewmodel.peminjaman.HomePeminjamanViewModel
import com.example.uas_pam_162.ui.viewmodel.peminjaman.InsertViewModelPeminjaman
import com.example.uas_pam_162.ui.viewmodel.peminjaman.UpdatePeminjamanViewModel
import com.example.uas_pam_162.ui.viewmodel.pengembalian.DetailPengembalianViewModel
import com.example.uas_pam_162.ui.viewmodel.pengembalian.HomePengembalianViewModel
import com.example.uas_pam_162.ui.viewmodel.pengembalian.InsertViewModelPengembalian


object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer { HomeBukuViewModel(aplikasiKontak().container.bukuRepository) }
        initializer { InsertBukuViewModel(aplikasiKontak().container.bukuRepository) }
        initializer { DetailBukuViewModel(createSavedStateHandle(),aplikasiKontak().container.bukuRepository) }
        initializer { UpdateBukuViewModel(createSavedStateHandle(),aplikasiKontak().container.bukuRepository) }


        initializer { HomeAnggotaViewModel(aplikasiKontak().container.anggotaRepository) }
        initializer { InsertAnggotaViewModel(aplikasiKontak().container.anggotaRepository) }
        initializer { DetailAgtViewModel(createSavedStateHandle(),aplikasiKontak().container.anggotaRepository) }
        initializer { UpdateAnggotaViewModel(createSavedStateHandle(),aplikasiKontak().container.anggotaRepository) }


        initializer { HomePeminjamanViewModel(
            aplikasiKontak().container.peminjamanRepository,
            aplikasiKontak().container.bukuRepository,
            aplikasiKontak().container.anggotaRepository

        ) }
        initializer { InsertViewModelPeminjaman(
            aplikasiKontak().container.peminjamanRepository,
            aplikasiKontak().container.bukuRepository,
            aplikasiKontak().container.anggotaRepository
        )
        }
        initializer { DetailPeminjamanViewModel(
            createSavedStateHandle(),
            aplikasiKontak().container.peminjamanRepository,
            aplikasiKontak().container.bukuRepository,
            aplikasiKontak().container.anggotaRepository

        ) }
        initializer { UpdatePeminjamanViewModel(
            createSavedStateHandle(),
            aplikasiKontak().container.peminjamanRepository,
            aplikasiKontak().container.bukuRepository,
            aplikasiKontak().container.anggotaRepository

        ) }



        initializer { HomePengembalianViewModel(
            aplikasiKontak().container.pengembalianRepository,
            aplikasiKontak().container.peminjamanRepository,
            aplikasiKontak().container.bukuRepository,
            aplikasiKontak().container.anggotaRepository,

        ) }

        initializer {
            DetailPengembalianViewModel(
                savedStateHandle = createSavedStateHandle(),
                pengembalianRepository = aplikasiKontak().container.pengembalianRepository,
                bukuRepository = aplikasiKontak().container.bukuRepository,
                anggotaRepository = aplikasiKontak().container.anggotaRepository,
                peminjamanRepository = aplikasiKontak().container.peminjamanRepository
            )
        }
        initializer { InsertViewModelPengembalian(
            aplikasiKontak().container.pengembalianRepository,
            aplikasiKontak().container.peminjamanRepository,
            aplikasiKontak().container.bukuRepository,
            aplikasiKontak().container.anggotaRepository,

            ) }


    }
}

fun CreationExtras.aplikasiKontak(): BukuApplications =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as BukuApplications)