package com.example.uas_pam_162.ui.viewmodel.peminjaman

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.model.Peminjaman
import com.example.uas_pam_162.repository.AnggotaRepository
import com.example.uas_pam_162.repository.BukuRepository
import com.example.uas_pam_162.repository.PeminjamanRepository
import com.example.uas_pam_162.ui.view.peminjaman.DestinasiDetailPeminjaman
import com.example.uas_pam_162.ui.viewmodel.buku.InsertUiEventBuku
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailPeminjamanUiState {
    data class Success(
        val peminjaman: Peminjaman,
        val judulBuku: String, // Langsung menyimpan judul buku
        val namaAnggota: String // Langsung menyimpan nama anggota
    ) : DetailPeminjamanUiState()
    object Error : DetailPeminjamanUiState()
    object Loading : DetailPeminjamanUiState()
}


class DetailPeminjamanViewModel(
    savedStateHandle: SavedStateHandle,
    private val pjm: PeminjamanRepository,
    private val repositoryBk: BukuRepository,
    private val repositoryAgt: AnggotaRepository
) : ViewModel() {

    // Ambil ID peminjaman dari savedStateHandle
    private val _idPeminjaman: String = checkNotNull(savedStateHandle[DestinasiDetailPeminjaman.IDPJM])

    // State untuk detail peminjaman
    private val _detailUiState = MutableStateFlow<DetailPeminjamanUiState>(DetailPeminjamanUiState.Loading)
    val detailPeminjamanUiState: StateFlow<DetailPeminjamanUiState> = _detailUiState

    // Ambil detail peminjaman berdasarkan ID
    init {
        println("ID Peminjaman: $_idPeminjaman")
        getDetailPeminjaman()
    }

    fun getDetailPeminjaman() {
        viewModelScope.launch {
            _detailUiState.value = DetailPeminjamanUiState.Loading
            println("Log: Memulai pengambilan detail peminjaman...")

            try {
                // Ambil data peminjaman berdasarkan ID
                println("Log: Mengambil data peminjaman untuk ID: $_idPeminjaman")
                val peminjaman = pjm.getPeminjamanbyId(_idPeminjaman)
                println("Log: Data peminjaman yang diambil: $peminjaman")

                if (peminjaman != null) {
                    // Pastikan id_buku dan id_anggota tidak null
                    val idBuku = peminjaman.id_buku
                        ?: throw IllegalArgumentException("Field id_buku tidak ditemukan di respons.")
                    val idAnggota = peminjaman.id_anggota
                        ?: throw IllegalArgumentException("Field id_anggota tidak ditemukan di respons.")

                    // Ambil data buku berdasarkan id_buku
                    println("Log: Mengambil data buku untuk ID Buku: $idBuku")
                    val buku = repositoryBk.getBukubyId(idBuku.toString())
                    println("Log: Data buku yang diambil: $buku")
                    val judulBuku = buku?.judul
                        ?: throw IllegalStateException("Data buku tidak ditemukan untuk ID: $idBuku")

                    // Ambil data anggota berdasarkan id_anggota
                    println("Log: Mengambil data anggota untuk ID Anggota: $idAnggota")
                    val anggota = repositoryAgt.getAnggotabyId(idAnggota) // idAnggota harus Int
                    println("Log: Data anggota yang diambil: $anggota"); val namaAnggota = anggota?.nama
                        ?: throw IllegalStateException("Data anggota tidak ditemukan untuk ID: $idAnggota")

                    // Update UI state dengan data yang diambil
                    _detailUiState.value = DetailPeminjamanUiState.Success(peminjaman, judulBuku, namaAnggota)
                    println("Log: Berhasil memperbarui UI state dengan data.")
                } else {
                    println("Log: Data peminjaman tidak ditemukan.")
                    _detailUiState.value = DetailPeminjamanUiState.Error
                }
            } catch (e: IllegalArgumentException) {
                // Tangani error jika id_buku atau id_anggota null
                println("Log: Terjadi error saat pengambilan data: ${e.message}")
                _detailUiState.value = DetailPeminjamanUiState.Error
            } catch (e: IllegalStateException) {
                // Tangani error jika data buku atau anggota tidak ditemukan
                println("Log: Terjadi error saat pengambilan data: ${e.message}")
                _detailUiState.value = DetailPeminjamanUiState.Error
            } catch (e: Exception) {
                // Tangani error umum
                println("Log: Terjadi error saat pengambilan data: ${e.message}")
                _detailUiState.value = DetailPeminjamanUiState.Error
            }
        }
    }
}
