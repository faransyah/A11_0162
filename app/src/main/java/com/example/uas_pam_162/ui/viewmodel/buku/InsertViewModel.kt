package com.example.uas_pam_162.ui.viewmodel.buku

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.repository.BukuRepository
import kotlinx.coroutines.launch

class InsertBukuViewModel(private val bk: BukuRepository): ViewModel(){

   var uiStateBuku by mutableStateOf(InsertUiStateBuku())
       private set

    fun UpdateInsertBkState(insertUiEvent: InsertUiEventBuku){
        uiStateBuku = InsertUiStateBuku(insertUiEvent = insertUiEvent)
    }

    fun isFormValid(): Boolean {
        val uiEvent = uiStateBuku.insertUiEvent
        return uiEvent.judul.isNotBlank() && uiEvent.penulis.isNotBlank() &&
                uiEvent.kategori.isNotBlank() && uiEvent.status.isNotBlank()
    }

    suspend fun insertBk(){
        if (!isFormValid()) {
            Log.e("InsertBukuViewModel", "Form tidak lengkap!")
            return
        }

        viewModelScope.launch {
            try {
                // Log data sebelum operasi dilakukan
                Log.d("InsertBukuViewModel", "Starting to insert Buku with data: $uiStateBuku")

                // Proses insert buku
                 // Jika ini fungsi, tambahkan tanda kurung
                bk.insertBuku(uiStateBuku.insertUiEvent.toBk())

                // Log sukses setelah operasi selesai
                Log.d("InsertBukuViewModel", "Successfully inserted Buku: $uiStateBuku")
            } catch (e: Exception) {
                // Log error jika terjadi exception
                Log.e("InsertBukuViewModel", "Error inserting Buku: ${e.message}", e)
                e.printStackTrace()  // Mencetak stack trace ke logcat untuk debugging
            }
        }

    }
}

data class InsertUiStateBuku(
    val insertUiEvent: InsertUiEventBuku = InsertUiEventBuku()
)

data class InsertUiEventBuku(
    val id_buku: Int= 0,
    val judul: String="",
    val penulis: String="",
    val kategori: String="",
    val status: String="",
)
fun InsertUiEventBuku.toBk(): Buku = Buku(
    id_buku = id_buku,
    judul = judul,
    penulis = penulis,
    kategori = kategori,
    status = status
)

fun Buku.toUiStateBk(): InsertUiStateBuku = InsertUiStateBuku(
    insertUiEvent = toInsertUiEvent()
)

fun Buku.toInsertUiEvent(): InsertUiEventBuku = InsertUiEventBuku(
    id_buku = id_buku,
    judul = judul,
    penulis = penulis,
    kategori = kategori,
    status = status
)
