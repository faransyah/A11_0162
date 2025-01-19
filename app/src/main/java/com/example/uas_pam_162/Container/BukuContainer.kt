package com.example.uas_pam_162.Container

import com.example.uas_pam_162.repository.BukuRepository
import com.example.uas_pam_162.repository.NetworkBukuRepository
import com.example.uas_pam_162.service_api.BukuService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer{
    val faranRepository: BukuRepository
}

class BukuContainer: AppContainer{
    private val baseUrl = "http://10.0.2.2:3010/api/buku/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl).build()

    private val bukuService: BukuService by lazy {
        retrofit.create(BukuService::class.java)
    }

    override val faranRepository: BukuRepository by lazy {
        NetworkBukuRepository(bukuService)
    }
}

