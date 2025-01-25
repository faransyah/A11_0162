package com.example.uas_pam_162.repository

import com.example.uas_pam_162.model.AllPengembalianResponse
import com.example.uas_pam_162.model.Pengembalian
import com.example.uas_pam_162.service_api.PengembalianService
import okio.IOException

interface PengembalianRepository {
    suspend fun getPengembalian(): AllPengembalianResponse
    suspend fun insertPengembalian(pengembalian: Pengembalian)
    suspend fun updatePengembalian(idPengembalian: String, pengembalian: Pengembalian)
    suspend fun deletePengembalian(idPengembalian: String)
    suspend fun getPengembalianById(idPengembalian: String): Pengembalian
}

class NetworkPengembalianRepository(
    private val pengembalianApiService: PengembalianService
) : PengembalianRepository {

    override suspend fun getPengembalian(): AllPengembalianResponse =
        pengembalianApiService.getAllPengembalian()  // Mengambil semua data pengembalian

    override suspend fun insertPengembalian(pengembalian: Pengembalian) {
        pengembalianApiService.insertPengembalian(pengembalian)  // Memasukkan data pengembalian baru
    }

    override suspend fun updatePengembalian(idPengembalian: String, pengembalian: Pengembalian) {
        pengembalianApiService.updatePengembalian(idPengembalian, pengembalian)  // Memperbarui data pengembalian
    }

    override suspend fun deletePengembalian(idPengembalian: String) {
        try {
            val response = pengembalianApiService.deletePengembalian(idPengembalian)
            if (!response.isSuccessful) {
                throw IOException(
                    "Failed to delete Pengembalian. HTTP Status Code: ${response.code()}"
                )
            } else {
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getPengembalianById(idPengembalian: String): Pengembalian {
        return pengembalianApiService.getPengembalianById(idPengembalian).data  // Mendapatkan data pengembalian berdasarkan ID
    }
}
