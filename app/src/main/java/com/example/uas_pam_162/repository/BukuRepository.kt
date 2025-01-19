package com.example.uas_pam_162.repository

import com.example.uas_pam_162.model.AllBukuResponse
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.service_api.BukuService
import okio.IOException

interface BukuRepository{
    suspend fun getBuku(): AllBukuResponse

    suspend fun insertBuku(buku: Buku)

    suspend fun updateBuku(idBuku: String, buku: Buku)

    suspend fun deleteBuku(idBuku: String)

    suspend fun getBukubyId(idBuku: String): Buku
}

class NetworkBukuRepository(
    private val bukuApiService: BukuService
):BukuRepository{
    override suspend fun getBuku(): AllBukuResponse =
        bukuApiService.getAllBuku()

    override suspend fun insertBuku(buku: Buku) {
        bukuApiService.insertBuku(buku)
    }

    override suspend fun updateBuku(idBuku: String, buku: Buku) {
        bukuApiService.updateBuku(idBuku, buku)
    }

    override suspend fun deleteBuku(idBuku: String) {
        try {
            val response = bukuApiService.deleteBuku(idBuku)
            if (!response.isSuccessful){
                throw IOException(
                    "Failed to delete Buku. HTTP Status Code: ${response.code()}"
                )
            }else{
                println(response.message())
            }
        }catch (e: Exception){
            throw e
        }
    }

    override suspend fun getBukubyId(idBuku: String): Buku {
        return bukuApiService.getBukubyId(idBuku).data
    }


}
