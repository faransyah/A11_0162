package com.example.uas_pam_162.ui.viewmodel.peminjaman

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.model.Peminjaman
import com.example.uas_pam_162.repository.AnggotaRepository
import com.example.uas_pam_162.repository.BukuRepository
import com.example.uas_pam_162.repository.PeminjamanRepository
import com.example.uas_pam_162.ui.view.peminjaman.DestinasiUpdatePeminjaman
import kotlinx.coroutines.launch



class UpdatePeminjamanViewModel(
    savedStateHandle: SavedStateHandle,
    private val pjm: PeminjamanRepository,
    private val bk: BukuRepository,
    private val agt: AnggotaRepository
) : ViewModel() {

    // Ambil ID peminjaman dari savedStateHandle
    val idPeminjaman: String = checkNotNull(savedStateHandle[DestinasiUpdatePeminjaman.IDPEMINJAMAN])

    // State untuk daftar buku dan anggota
    var _bukuList by mutableStateOf<List<Buku>>(listOf())
        private set

    var _anggotaList by mutableStateOf<List<Anggota>>(listOf())
        private set

    // State untuk UI peminjaman
    var uiStatePjm by mutableStateOf(InsertUiStatePjm())
        private set

    init {
        ambilPeminjaman()
        loadData()
    }

    // Fungsi untuk mengambil data peminjaman berdasarkan ID
    private fun ambilPeminjaman() {
        viewModelScope.launch {
            try {
                val peminjaman = pjm.getPeminjamanbyId(idPeminjaman)
                peminjaman?.let {
                    uiStatePjm = it.toInsertPjmUIEvent()
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error mengambil data peminjaman: ${e.message}", e)
            }
        }
    }

    // Fungsi untuk memuat data buku dan anggota
    private fun loadData() {
        viewModelScope.launch {
            try {
                _bukuList = bk.getBuku().data // Ambil daftar buku
                _anggotaList = agt.getAnggota().data // Ambil daftar anggota
            } catch (e: Exception) {
                Log.e("ViewModel", "Error memuat data: ${e.message}", e)
            }
        }
    }

    // Fungsi untuk memperbarui state UI peminjaman
    fun updatePeminjamanState(insertUiEventPeminjam: InsertUiEventPeminjam) {
        uiStatePjm = uiStatePjm.copy(insertUiEventPeminjam = insertUiEventPeminjam)
    }

    // Fungsi untuk memperbarui data peminjaman
    fun updatePeminjaman() {
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "Memperbarui data peminjaman: ${uiStatePjm.insertUiEventPeminjam}")
                pjm.updatePeminjaman(idPeminjaman, uiStatePjm.insertUiEventPeminjam.toPjm())
                Log.d("ViewModel", "Peminjaman berhasil diperbarui")
            } catch (e: Exception) {
                Log.e("ViewModel", "Error memperbarui peminjaman: ${e.message}", e)
            }
        }
    }
}

// Extension function untuk mengkonversi Peminjaman ke InsertUiStatePjm
fun Peminjaman.toInsertPjmUIEvent(): InsertUiStatePjm = InsertUiStatePjm(
    insertUiEventPeminjam = InsertUiEventPeminjam(
        id_peminjaman = this.id_peminjaman,
        tanggal_peminjaman = this.tanggal_peminjaman,
        tanggal_pengembalian = this.tanggal_pengembalian ?: "",
        id_buku = this.id_buku ?: 0, // Pastikan tipe data id_buku adalah Int
        id_anggota = this.id_anggota ?: 0 // Pastikan tipe data id_anggota adalah Int
    )
)