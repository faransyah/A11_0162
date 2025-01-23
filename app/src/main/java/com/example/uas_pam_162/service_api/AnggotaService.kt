package com.example.uas_pam_162.service_api

import com.example.uas_pam_162.model.AllAnggotaRespon
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.model.AnggotaDetailRespone
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AnggotaService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )


    // Mengambil semua anggota
    @GET("anggota")
    suspend fun getAllAnggota(): AllAnggotaRespon

    // Mengambil anggota berdasarkan ID
    @GET("anggota/{idAnggota}")
    suspend fun getAnggotabyId(@Path("idAnggota") idAnggota: String): AnggotaDetailRespone

    // Menambahkan anggota baru
    @POST("anggota/store")
    suspend fun insertAnggota(@Body anggota: Anggota)

    // Memperbarui anggota berdasarkan ID
    @PUT("anggota/{idAnggota}")
    suspend fun updateAnggota(@Path("idAnggota") idAnggota: String, @Body anggota: Anggota)

    // Menghapus anggota berdasarkan ID
    @DELETE("anggota/{idAnggota}")
    suspend fun deleteAnggota(@Path("idAnggota") idAnggota: String): retrofit2.Response<Void>

}