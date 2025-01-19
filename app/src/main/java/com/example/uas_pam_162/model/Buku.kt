package com.example.uas_pam_162.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Buku(
    val id_buku: Int,
    val judul: String,
    val penulis: String,
    val kategori: String,

    @SerialName("status")
    val status: String
)

@Serializable
data class AllBukuResponse(
    val status: Boolean,
    val message: String,
    val data: List<Buku>
)

@Serializable
data class BukuDetailRespone(
    val status: Boolean,
    val message: String,
    val data: Buku
)