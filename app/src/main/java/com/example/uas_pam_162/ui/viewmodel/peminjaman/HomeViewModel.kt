package com.example.uas_pam_162.ui.viewmodel.peminjaman

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.model.Peminjaman
import com.example.uas_pam_162.repository.AnggotaRepository
import com.example.uas_pam_162.repository.BukuRepository
import com.example.uas_pam_162.repository.PeminjamanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException

sealed class HomePeminjamanUiState {
    data class Success(
        val peminjaman: List<Peminjaman>,
        val buku: List<Buku>, // Ganti Map<Int, String> dengan List<Buku>
        val anggota: List<Anggota> // Ganti Map<Int, String> dengan List<Anggota>
    ) : HomePeminjamanUiState()
    object Error : HomePeminjamanUiState()
    object Loading : HomePeminjamanUiState()
}

class HomePeminjamanViewModel(
    private val pjm: PeminjamanRepository,
    private val repositoryBk: BukuRepository,
    private val repositoryAgt: AnggotaRepository
) : ViewModel() {

    var _bukuList by mutableStateOf<List<Buku>>(listOf())
        private set

    var _anggotaList by mutableStateOf<List<Anggota>>(listOf())
        private set

    // Gunakan MutableStateFlow untuk mengelola state
    private val _pjmUIState = MutableStateFlow<HomePeminjamanUiState>(HomePeminjamanUiState.Loading)
    val pjmUIState: StateFlow<HomePeminjamanUiState> get() = _pjmUIState

    init {
        getPjm()
    }

    // Function to fetch data
    fun getPjm() {
        viewModelScope.launch {
            _pjmUIState.value = HomePeminjamanUiState.Loading
            Log.d("HomePeminjamanViewModel", "Memulai pengambilan data peminjaman...")

            try {
                // Ambil data peminjaman
                Log.d("HomePeminjamanViewModel", "Mengambil data peminjaman...")
                val peminjaman = pjm.getPeminjaman().data
                Log.d("HomePeminjamanViewModel", "Data peminjaman berhasil diambil: $peminjaman")

                // Ambil data buku
                Log.d("ViewModel", "Loading data for Buku...")
                val bukuPjm = repositoryBk.getBuku()
                _bukuList = bukuPjm.data
                Log.d("ViewModel", "Loaded Buku: ${_bukuList.size} items")

                // Ambil data anggota
                Log.d("ViewModel", "Loading data for Anggota...")
                val anggotaPjm = repositoryAgt.getAnggota()
                _anggotaList = anggotaPjm.data
                Log.d("ViewModel", "Loaded Anggota: ${_anggotaList.size} items")

                // Update state dengan data peminjaman, buku, dan anggota
                Log.d("HomePeminjamanViewModel", "Memperbarui UI state dengan data yang diambil...")
                _pjmUIState.value = HomePeminjamanUiState.Success(peminjaman, _bukuList, _anggotaList)
                Log.d("HomePeminjamanViewModel", "UI state berhasil diperbarui.")
            } catch (e: IOException) {
                Log.e("HomePeminjamanViewModel", "IO Error: ${e.message}", e)
                _pjmUIState.value = HomePeminjamanUiState.Error
            } catch (e: HttpException) {
                Log.e("HomePeminjamanViewModel", "HTTP Error: ${e.message}", e)
                _pjmUIState.value = HomePeminjamanUiState.Error
            } catch (e: Exception) {
                Log.e("HomePeminjamanViewModel", "Unexpected Error: ${e.message}", e)
                _pjmUIState.value = HomePeminjamanUiState.Error
            }
        }
    }

    // Function to delete a peminjaman
    fun deletePjm(idPeminjaman: String) {
        viewModelScope.launch {
            try {
                pjm.deletePeminjaman(idPeminjaman)
                getPjm()  // Reload the list after deletion
            } catch (e: IOException) {
                _pjmUIState.value = HomePeminjamanUiState.Error  // Update UI state on error
            } catch (e: Exception) {
                _pjmUIState.value = HomePeminjamanUiState.Error  // Handle unexpected errors
            }
        }
    }
}