package com.example.uas_pam_162.service_api

import com.example.uas_pam_162.model.AllBukuResponse
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.model.BukuDetailRespone
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BukuService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )


    @GET("buku")
    suspend fun getAllBuku(): AllBukuResponse

    @GET("buku/{idBuku}")
    suspend fun getBukubyId(@Path("idBuku")idBuku: String):BukuDetailRespone


    @POST("buku")
    suspend fun insertBuku(@Body buku: Buku)


    @PUT("buku/{idBuku}")
    suspend fun updateBuku(@Path("idBuku") idBuku: String, @Body buku: Buku)


    @DELETE("buku/{idBuku}")
    suspend fun deleteBuku(@Path("idBuku") idBuku: String): retrofit2.Response<Void>

}