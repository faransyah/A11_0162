package com.example.uas_pam_162.ui.viewmodel.peminjaman

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.model.Peminjaman
import com.example.uas_pam_162.repository.AnggotaRepository
import com.example.uas_pam_162.repository.BukuRepository
import com.example.uas_pam_162.repository.PeminjamanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class InsertViewModelPeminjaman(
    private val pjm: PeminjamanRepository,
    private val repositoryBk: BukuRepository,
    private val repositoryAgt: AnggotaRepository
) : ViewModel() {

    var _bukuList = mutableStateOf<List<Buku>>(emptyList())
        private set
    var _anggotaList = mutableStateOf<List<Anggota>>(emptyList())
        private set

    var uiStatePjm by mutableStateOf(InsertUiStatePjm())
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val bukuPjm = repositoryBk.getBuku()
                bukuPjm?.data?.let { data ->
                    _bukuList.value = data
                }

                val anggotaPjm = repositoryAgt.getAnggota()
                anggotaPjm?.data?.let { data ->
                    _anggotaList.value = data
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading data: ${e.message}", e)
            }
        }
    }

    fun insertPjm() {
        viewModelScope.launch {
            try {
                val peminjaman = uiStatePjm.insertUiEventPeminjam.toPjm()
                pjm.insertPeminjaman(peminjaman)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error inserting peminjaman: ${e.message}", e)
            }
        }
    }

    fun updateSelectedBuku(idBuku: Int) {
        uiStatePjm = uiStatePjm.copy(
            insertUiEventPeminjam = uiStatePjm.insertUiEventPeminjam.copy(id_buku = idBuku)
        )
    }

    fun updateSelectedAnggota(idAnggota: Int) {
        uiStatePjm = uiStatePjm.copy(
            insertUiEventPeminjam = uiStatePjm.insertUiEventPeminjam.copy(id_anggota = idAnggota)
        )
    }
}

// Data class untuk state UI
data class InsertUiStatePjm(
    val insertUiEventPeminjam: InsertUiEventPeminjam = InsertUiEventPeminjam()
)

// Data class untuk event UI
data class InsertUiEventPeminjam(
    val id_peminjaman: Int = 0,
    val tanggal_peminjaman: String = "",
    val tanggal_pengembalian: String = "",
    val id_buku: Int = 0,
    val id_anggota: Int = 0
)

// Extension function untuk mengkonversi InsertUiEventPeminjam ke Peminjaman
fun InsertUiEventPeminjam.toPjm(): Peminjaman = Peminjaman(
    id_peminjaman = id_peminjaman,
    tanggal_peminjaman = tanggal_peminjaman,
    tanggal_pengembalian = tanggal_pengembalian,
    id_buku = id_buku,
    id_anggota = id_anggota
)