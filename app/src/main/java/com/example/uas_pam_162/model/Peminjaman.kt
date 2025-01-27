package com.example.uas_pam_162.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Peminjaman(
    val id_peminjaman: Int,
    val id_buku: Int? = null, // Field nullable
    val id_anggota: Int? = null, // Field nullable
    val tanggal_peminjaman: String,
    val tanggal_pengembalian: String?
)

@Serializable
data class AllPeminjamanRespon(
    val status: Boolean,
    val message: String,
    val data: List<Peminjaman>
)

@Serializable
data class PeminjamanDetailRespone(
    val status: Boolean,
    val message: String,
    val data: Peminjaman
)