package com.example.uas_pam_162.ui.viewmodel.anggota

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.repository.AnggotaRepository
import kotlinx.coroutines.launch
import okio.IOException

class InsertAnggotaViewModel(private val agt: AnggotaRepository): ViewModel(){

    var uiStateAgt by mutableStateOf(InsertUiStateAgt())
        private set

    fun UpdateInsertAgtState(insertUiEventAgt: InsertUiEventAgt){
        uiStateAgt = InsertUiStateAgt(insertUiEventAgt = insertUiEventAgt)
    }

    suspend fun insertAgt() {
        viewModelScope.launch {
            try {
                // Log data sebelum melakukan insert
                Log.d("InsertAnggotaViewModel", "Starting to insert Anggota with data: ${uiStateAgt.insertUiEventAgt}")

                // Proses insert anggota
                agt.insertAnggota(uiStateAgt.insertUiEventAgt.toAgt())

                // Log sukses setelah proses selesai
                Log.d("InsertAnggotaViewModel", "Successfully inserted Anggota: ${uiStateAgt.insertUiEventAgt}")
            } catch (e: HttpException) {
                // Log jika terjadi error HTTP (misalnya, server tidak merespons dengan baik)
                Log.e("InsertAnggotaViewModel", "HTTP Error: ${e.message}", e)
            } catch (e: IOException) {
                // Log jika terjadi error koneksi jaringan
                Log.e("InsertAnggotaViewModel", "Network Error: ${e.message}", e)
            } catch (e: Exception) {
                // Log jika terjadi error lainnya
                Log.e("InsertAnggotaViewModel", "Error inserting Anggota: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }


}

data class InsertUiStateAgt(
    val insertUiEventAgt: InsertUiEventAgt = InsertUiEventAgt()
)

data class InsertUiEventAgt(
    val id_anggota: Int = 0,
    val nama: String="",
    val email: String="",
    val nomor_telepon: String=""
)

fun InsertUiEventAgt.toAgt(): Anggota = Anggota(
    id_anggota = id_anggota,
    nama = nama,
    email = email,
    nomor_telepon = nomor_telepon
)

fun Anggota.toUiStateAgt(): InsertUiStateAgt = InsertUiStateAgt(
    insertUiEventAgt = toInsertUiEvent()
)

fun Anggota.toInsertUiEvent(): InsertUiEventAgt = InsertUiEventAgt(
    id_anggota = id_anggota,
    nama = nama ,
    email = email,
    nomor_telepon = nomor_telepon
)














