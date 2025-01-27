package com.example.uas_pam_162.repository


import com.example.uas_pam_162.model.AllPeminjamanRespon
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.model.Peminjaman
import com.example.uas_pam_162.service_api.PeminjamanService
import okio.IOException

interface PeminjamanRepository {
    suspend fun getPeminjaman(): AllPeminjamanRespon
    suspend fun insertPeminjaman(peminjaman: Peminjaman)
    suspend fun updatePeminjaman(idPeminjam: String, peminjaman: Peminjaman)
    suspend fun deletePeminjaman(idPeminjam: String)
    suspend fun getPeminjamanbyId(idPeminjam: String): Peminjaman // Tipe diubah ke String jika menggunakan UUID atau format ID lainnya


}

class NetworkPeminjamanRepository(
    private val peminjamanApiService: PeminjamanService
) : PeminjamanRepository {
    override suspend fun getPeminjaman(): AllPeminjamanRespon =
        peminjamanApiService.getAllPeminjaman()  // Mengambil daftar peminjaman

    override suspend fun insertPeminjaman(peminjaman: Peminjaman) {
        peminjamanApiService.insertPeminjaman(peminjaman)  // Memasukkan peminjaman baru
    }

    override suspend fun updatePeminjaman(idPeminjam: String, peminjaman: Peminjaman) {
        peminjamanApiService.updatePeminjaman(idPeminjam, peminjaman)  // Memperbarui peminjaman
    }

    override suspend fun deletePeminjaman(idPeminjam: String) {
        try {
            val response = peminjamanApiService.deletePeminjaman(idPeminjam)
            if (!response.isSuccessful) {
                throw IOException(
                    "Failed to delete Peminjaman. HTTP Status Code: ${response.code()}"
                )
            } else {
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getPeminjamanbyId(idPeminjam: String): Peminjaman {
        return peminjamanApiService.getPeminjamanbyId(idPeminjam).data  // Memanggil API untuk mendapatkan peminjaman berdasarkan ID
    }
}