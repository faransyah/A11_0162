package com.example.uas_pam_162.ui.viewmodel.pengembalian

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam_162.model.Peminjaman
import com.example.uas_pam_162.model.Pengembalian
import com.example.uas_pam_162.repository.AnggotaRepository
import com.example.uas_pam_162.repository.BukuRepository
import com.example.uas_pam_162.repository.PeminjamanRepository
import com.example.uas_pam_162.repository.PengembalianRepository
import com.example.uas_pam_162.ui.view.pengembalian.DestinasiDetailPengembalian
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailPengembalianUiState {
    data class Success(
        val pengembalian: Pengembalian,
        val peminjaman: Peminjaman,
        val judulBuku: String,
        val namaAnggota: String,
        val tanggalDikembalikan: String
    ) : DetailPengembalianUiState()

    object Error : DetailPengembalianUiState()
    object Loading : DetailPengembalianUiState()
}

class DetailPengembalianViewModel(
    savedStateHandle: SavedStateHandle,
    private val pengembalianRepository: PengembalianRepository,
    private val peminjamanRepository: PeminjamanRepository,
    private val bukuRepository: BukuRepository,
    private val anggotaRepository: AnggotaRepository
) : ViewModel() {

    private val TAG = "DetailPengembalianVM"

    // Ambil ID pengembalian dari SavedStateHandle
    private val _idPengembalian: String =
        checkNotNull(savedStateHandle[DestinasiDetailPengembalian.IDPNGN])

    // State untuk detail pengembalian
    private val _detailPengembalianUiState =
        MutableStateFlow<DetailPengembalianUiState>(DetailPengembalianUiState.Loading)
    val detailPengembalianUiState: StateFlow<DetailPengembalianUiState> = _detailPengembalianUiState

    init {
        Log.d(TAG, "Inisialisasi ViewModel dengan ID Pengembalian: $_idPengembalian")
        getDetailPengembalian()
    }

    fun getDetailPengembalian() {
        viewModelScope.launch {
            _detailPengembalianUiState.value = DetailPengembalianUiState.Loading
            try {
                Log.d(TAG, "Mulai mengambil data pengembalian dari repository...")
                val pengembalian = pengembalianRepository.getPengembalianById(_idPengembalian)
                    ?: throw IllegalStateException("Pengembalian tidak ditemukan untuk ID: $_idPengembalian.")

                Log.d(TAG, "Pengembalian ditemukan: $pengembalian")

                // Periksa apakah id_buku dan id_anggota tidak null
                val idBuku = pengembalian.id_buku
                val idAnggota = pengembalian.id_anggota

                if (idBuku == null || idAnggota == null) {
                    Log.e(TAG, "Field id_buku atau id_anggota tidak ditemukan pada pengembalian ID: $_idPengembalian.")
                    _detailPengembalianUiState.value = DetailPengembalianUiState.Error
                    return@launch
                }

                Log.d(TAG, "ID Peminjaman: ${pengembalian.id_peminjaman}, ID Buku: $idBuku, ID Anggota: $idAnggota")

                // Ambil data peminjaman
                val peminjaman = peminjamanRepository.getPeminjamanbyId(pengembalian.id_peminjaman.toString())
                    ?: throw IllegalStateException("Peminjaman tidak ditemukan untuk ID: ${pengembalian.id_peminjaman}.")
                Log.d(TAG, "Peminjaman ditemukan: $peminjaman")

                // Ambil data buku
                val buku = bukuRepository.getBukubyId(idBuku.toString())
                    ?: throw IllegalStateException("Buku tidak ditemukan untuk ID: $idBuku.")
                Log.d(TAG, "Buku ditemukan: $buku")

                // Ambil data anggota
                val anggota = anggotaRepository.getAnggotabyId(idAnggota)
                    ?: throw IllegalStateException("Anggota tidak ditemukan untuk ID: $idAnggota.")
                Log.d(TAG, "Anggota ditemukan: $anggota")

                // Update UI state dengan data yang diambil
                _detailPengembalianUiState.value = DetailPengembalianUiState.Success(
                    pengembalian = pengembalian,
                    peminjaman = peminjaman,
                    judulBuku = buku.judul,
                    namaAnggota = anggota.nama,
                    tanggalDikembalikan = pengembalian.tanggal_dikembalikan
                )
                Log.d(TAG, "Berhasil memuat detail pengembalian.")
            } catch (e: Exception) {
                Log.e(TAG, "Error saat mengambil detail pengembalian", e)
                _detailPengembalianUiState.value = DetailPengembalianUiState.Error
            }
        }
    }
}
