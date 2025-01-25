package com.example.uas_pam_162.model

import kotlinx.serialization.Serializable

@Serializable
data class Pengembalian(
    val id_pengembalian: Int,
    val id_peminjaman: Int,
    val tanggal_dikembalikan: String
)

@Serializable
data class AllPengembalianResponse(
    val status: Boolean,
    val message: String,
    val data: List<Pengembalian>
)

@Serializable
data class PengembalianDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Pengembalian
)
