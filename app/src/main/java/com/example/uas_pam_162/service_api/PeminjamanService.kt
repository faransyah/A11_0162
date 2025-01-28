package com.example.uas_pam_162.service_api

import com.example.uas_pam_162.model.AllPeminjamanRespon
import com.example.uas_pam_162.model.Peminjaman
import com.example.uas_pam_162.model.PeminjamanDetailRespone
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PeminjamanService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )


    @GET("peminjaman")
    suspend fun getAllPeminjaman(): AllPeminjamanRespon

    @GET("peminjaman/{idPeminjaman}")
    suspend fun getPeminjamanbyId(@Path("idPeminjaman")idPeminjaman: String): PeminjamanDetailRespone


    @POST("peminjaman/store")
    suspend fun insertPeminjaman(@Body peminjaman: Peminjaman)


    @PUT("peminjaman/{idPeminjaman}")
    suspend fun updatePeminjaman(@Path("idPeminjaman") idPeminjaman: String, @Body peminjaman: Peminjaman)


    @DELETE("peminjaman/{idPeminjaman}")
    suspend fun deletePeminjaman(@Path("idPeminjaman") idPeminjaman: String): retrofit2.Response<Void>

}