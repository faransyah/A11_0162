package com.example.uas_pam_162.service_api

import com.example.uas_pam_162.model.AllPengembalianResponse
import com.example.uas_pam_162.model.Pengembalian
import com.example.uas_pam_162.model.PengembalianDetailResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PengembalianService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )

    @GET("pengembalian")
    suspend fun getAllPengembalian(): AllPengembalianResponse

    @GET("pengembalian/{idPengembalian}")
    suspend fun getPengembalianById(@Path("idPengembalian") idPengembalian: String): PengembalianDetailResponse

    @POST("pengembalian")
    suspend fun insertPengembalian(@Body pengembalian: Pengembalian)

    @PUT("pengembalian/{idPengembalian}")
    suspend fun updatePengembalian(@Path("idPengembalian") idPengembalian: String, @Body pengembalian: Pengembalian)

    @DELETE("pengembalian/{idPengembalian}")
    suspend fun deletePengembalian(@Path("idPengembalian") idPengembalian: String): retrofit2.Response<Void>

}