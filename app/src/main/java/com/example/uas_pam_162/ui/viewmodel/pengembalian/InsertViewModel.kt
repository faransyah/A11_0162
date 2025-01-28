package com.example.uas_pam_162.ui.viewmodel.pengembalian

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.model.Peminjaman
import com.example.uas_pam_162.model.Pengembalian
import com.example.uas_pam_162.repository.AnggotaRepository
import com.example.uas_pam_162.repository.BukuRepository
import com.example.uas_pam_162.repository.PeminjamanRepository
import com.example.uas_pam_162.repository.PengembalianRepository
import com.example.uas_pam_162.ui.viewmodel.peminjaman.InsertUiEventPeminjam
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// UI State untuk Pengembalian
sealed class PengembalianUiState {
    object Loading : PengembalianUiState()
    data class Success(val message: String) : PengembalianUiState()
    data class Error(val message: String) : PengembalianUiState()
}

class InsertViewModelPengembalian(
    private val pengembalianRepository: PengembalianRepository,
    private val repositoryPJm: PeminjamanRepository,
    private val repositoryBk: BukuRepository,
    private val repositoryAgt: AnggotaRepository
) : ViewModel() {

    var bukuList by mutableStateOf<List<Buku>>(listOf())
        private set

    var anggotaList by mutableStateOf<List<Anggota>>(listOf())
        private set

    var peminjamanList by mutableStateOf<List<Peminjaman>>(listOf())
        private set

    private val _uiStatePngn = MutableStateFlow<PengembalianUiState>(PengembalianUiState.Loading)
    val uiStatePngn: StateFlow<PengembalianUiState> = _uiStatePngn

    var idBuku by mutableStateOf(0)
    var idPeminjaman by mutableStateOf(0)
    var tanggalPengembalian by mutableStateOf("")

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                // Load Buku
                val bukuPngn = repositoryBk.getBuku()
                bukuList = bukuPngn.data

                // Load Anggota
                val anggotaPngn = repositoryAgt.getAnggota()
                anggotaList = anggotaPngn.data

                // Load Peminjaman
                val peminjamanPngn = repositoryPJm.getPeminjaman()
                peminjamanList = peminjamanPngn.data

                _uiStatePngn.value = PengembalianUiState.Success("Data berhasil dimuat")
            } catch (e: Exception) {
                _uiStatePngn.value = PengembalianUiState.Error("Gagal memuat data: ${e.message}")
            }
        }
    }

    fun validateAndInsertPngn(): Boolean {
        val errorMessage = when {
            idBuku == 0 -> "Buku harus dipilih"
            idPeminjaman == 0 -> "Peminjaman harus dipilih"
            tanggalPengembalian.isEmpty() -> "Tanggal pengembalian harus diisi"
            else -> null
        }

        if (errorMessage != null) {
            _uiStatePngn.value = PengembalianUiState.Error(errorMessage)
            return false
        }

        insertPngn()
        return true
    }

    private fun insertPngn() {
        viewModelScope.launch {
            try {
                val pengembalian = Pengembalian(
                    id_pengembalian = 0,
                    id_peminjaman = idPeminjaman,
                    id_buku = idBuku,
                    tanggal_dikembalikan = tanggalPengembalian
                )

                pengembalianRepository.insertPengembalian(pengembalian)
                _uiStatePngn.value = PengembalianUiState.Success("Pengembalian berhasil ditambahkan")
            } catch (e: Exception) {
                _uiStatePngn.value = PengembalianUiState.Error("Gagal menambahkan pengembalian: ${e.message}")
            }
        }
    }
}
