package com.example.uas_pam_162.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Anggota(
    val id_anggota: Int,
    val nama: String,
    val email: String,
    val nomor_telepon: String
)

@Serializable
data class AllAnggotaRespon(
    val status: Boolean,
    val message: String,
    val data: List<Anggota>
)

@Serializable
data class AnggotaDetailRespone(
    val status: Boolean,
    val message: String,
    val data: Anggota
)