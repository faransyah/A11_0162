package com.example.uas_pam_162.repository

import com.example.uas_pam_162.model.AllAnggotaRespon
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.service_api.AnggotaService
import okio.IOException


interface AnggotaRepository {
    suspend fun getAnggota(): AllAnggotaRespon

    suspend fun insertAnggota(anggota: Anggota)

    suspend fun updateAnggota(idAnggota: String, anggota: Anggota)

    suspend fun deleteAnggota(idAnggota: String)

    suspend fun getAnggotabyId(idAnggota: Int): Anggota
}

class NetworkAnggotaRepository(
    private val anggotaApiService: AnggotaService
): AnggotaRepository{
    override suspend fun getAnggota(): AllAnggotaRespon =
        anggotaApiService.getAllAnggota()

    override suspend fun insertAnggota(anggota: Anggota) {
        anggotaApiService.insertAnggota(anggota)
    }

    override suspend fun updateAnggota(idAnggota: String, anggota: Anggota) {
        anggotaApiService.updateAnggota(idAnggota, anggota)
    }

    override suspend fun deleteAnggota(idAnggota: String) {
        try {
            val response = anggotaApiService.deleteAnggota(idAnggota)
            if (!response.isSuccessful){
                throw IOException(
                    "Failed to delete Anggota.HTTP Status Code: ${response.code()} "
                )
            }else{
                println(response.message())
            }
        }catch (e:Exception){
            throw e
        }
    }

    override suspend fun getAnggotabyId(idAnggota: Int): Anggota {
        return anggotaApiService.getAnggotabyId(idAnggota.toString()).data
    }

}
