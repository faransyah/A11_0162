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
import kotlinx.coroutines.launch


class InsertViewModelPeminjaman(
    private val pjm: PeminjamanRepository,
    private val repositoryBk: BukuRepository,
    private val repositoryAgt: AnggotaRepository
) : ViewModel() {

   var _bukuList by mutableStateOf<List<Buku>>(listOf())
       private set

    var _anggotaList by mutableStateOf<List<Anggota>>(listOf())
        private set

    var uiStatePjm by mutableStateOf(InsertUiStatePjm())
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "Loading data for Buku...")
                val bukuPjm = repositoryBk.getBuku()
                _bukuList = bukuPjm.data
                Log.d("ViewModel", "Loaded Buku: ${_bukuList.size} items")

                Log.d("ViewModel", "Loading data for Anggota...")
                val anggotaPjm = repositoryAgt.getAnggota()
                _anggotaList = anggotaPjm.data
                Log.d("ViewModel", "Loaded Anggota: ${_anggotaList.size} items")

            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading data: ${e.message}", e)
            }
        }
    }
    fun updateInsertState(insertUiEventPeminjam: InsertUiEventPeminjam){
        uiStatePjm = uiStatePjm.copy(insertUiEventPeminjam = insertUiEventPeminjam)
    }
    fun validateAndInsertPjm(): Boolean {
        val errorMessage = when {
            uiStatePjm.insertUiEventPeminjam.id_buku == 0 -> "Buku harus dipilih"
            uiStatePjm.insertUiEventPeminjam.id_anggota == 0 -> "Anggota harus dipilih"
            uiStatePjm.insertUiEventPeminjam.tanggal_peminjaman.isEmpty() -> "Tanggal peminjaman harus diisi"
            uiStatePjm.insertUiEventPeminjam.tanggal_pengembalian.isEmpty() -> "Tanggal pengembalian harus diisi"
            else -> null
        }
        if (errorMessage != null) {
            Log.e("InsertViewModelPeminjaman", "Validation failed: $errorMessage")
            return false
        }
        insertPjm()  // Call insert if validation passes
        return true
    }


        fun insertPjm() {
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "Sedang memasukkan data Peminjaman: $uiStatePjm")
                pjm.insertPeminjaman(uiStatePjm.insertUiEventPeminjam.toPjm())
                Log.d("ViewModel", "Peminjaman berhasil dimasukkan")

            } catch (e: Exception) {
                when (e) {
                    is retrofit2.HttpException -> {
                        val response = e.response()
                        val errorBody = response?.errorBody()?.string()
                        Log.e("ViewModel", "Kesalahan HTTP: ${e.code()} - ${e.message}")
                        Log.e("ViewModel", "Isi respons kesalahan: $errorBody")
                    }
                    else -> {
                        Log.e("ViewModel", "Terjadi kesalahan saat memasukkan peminjaman: ${e.message}", e)
                    }
                }
            }
        }
    }
}

// Data class untuk state UI
data class InsertUiStatePjm(
    val insertUiEventPeminjam: InsertUiEventPeminjam = InsertUiEventPeminjam(),
    val judulBukuList: List<Buku> = emptyList(),
    val namaAnggotaList: List<Anggota> = emptyList()
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

