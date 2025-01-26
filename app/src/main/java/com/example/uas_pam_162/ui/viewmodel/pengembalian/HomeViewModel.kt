package com.example.uas_pam_162.ui.viewmodel.pengembalian

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.uas_pam_162.model.Peminjaman
import com.example.uas_pam_162.model.Pengembalian
import com.example.uas_pam_162.repository.AnggotaRepository
import com.example.uas_pam_162.repository.BukuRepository
import com.example.uas_pam_162.repository.PeminjamanRepository
import com.example.uas_pam_162.repository.PengembalianRepository
import kotlinx.coroutines.launch
import okio.IOException

sealed class HomePengembalianUiState {
    data class Success(
        val pengembalian: List<Pengembalian>,
        val peminjaman: Map<Int, Peminjaman>, // Map<id_peminjaman, Peminjaman>
        val buku: Map<Int, String>, // Map<id_buku, judul>
        val anggota: Map<Int, String> // Map<id_anggota, nama>
    ) : HomePengembalianUiState()
    object Error : HomePengembalianUiState()
    object Loading : HomePengembalianUiState()
}


class HomePengembalianViewModel(
    private val png: PengembalianRepository,
    private val pjm: PeminjamanRepository,
    private val repositoryBk: BukuRepository, // Repository buku
    private val repositoryAgt: AnggotaRepository // Repository anggota
) : ViewModel() {
    var pngUIState: HomePengembalianUiState by mutableStateOf(HomePengembalianUiState.Loading)
        private set


    fun getPng() {
        viewModelScope.launch {
            pngUIState = HomePengembalianUiState.Loading
            try {
                // Ambil data pengembalian
                val pengembalian = png.getPengembalian().data

                // Ambil data peminjaman
                val peminjamanList = pjm.getPeminjaman().data
                val peminjamanMap = peminjamanList.associateBy { it.id_peminjaman  }

                // Ambil data buku dan anggota
                val bukuMap = repositoryBk.getBuku().data.associate { it.id_buku to it.judul }
                val anggotaMap = repositoryAgt.getAnggota().data.associate { it.id_anggota to it.nama }

                // Update state dengan data pengembalian, peminjaman, buku, dan anggota
                pngUIState = HomePengembalianUiState.Success(pengembalian, peminjamanMap, bukuMap, anggotaMap)
            } catch (e: IOException) {
                pngUIState = HomePengembalianUiState.Error
            } catch (e: HttpException) {
                pngUIState = HomePengembalianUiState.Error
            }
        }
    }

    // Function to delete a pengembalian
    fun deletePng(idPengembalian: String) {
        viewModelScope.launch {
            try {
                png.deletePengembalian(idPengembalian)
                getPng()  // Reload the list after deletion
            } catch (e: IOException) {
                pngUIState = HomePengembalianUiState.Error
            } catch (e: Exception) {
                pngUIState = HomePengembalianUiState.Error
            }
        }
    }
}