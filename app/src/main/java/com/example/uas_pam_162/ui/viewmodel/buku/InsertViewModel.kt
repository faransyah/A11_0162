package com.example.uas_pam_162.ui.viewmodel.buku

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.repository.BukuRepository
import kotlinx.coroutines.launch

class InsertBukuViewModel(private val bk: BukuRepository): ViewModel(){

   var uiState by mutableStateOf(InsertUiState())
       private set

    fun UpdateInsertBkState(insertUiEvent: InsertUiEvent){
        uiState = InsertUiState(insertUiEvent = insertUiEvent)
    }

    suspend fun insertBk(){
        viewModelScope.launch {
            try {
                bk.insertBuku(uiState.insertUiEvent.toBk())
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}

data class InsertUiState(
    val insertUiEvent: InsertUiEvent = InsertUiEvent()
)

data class InsertUiEvent(
    val id_buku: String="",
    val judul: String="",
    val penulis: String="",
    val kategori: String="",
    val status: String="",
)
fun InsertUiEvent.toBk(): Buku = Buku(
    id_buku = id_buku.toInt(),
    judul = judul,
    penulis = penulis,
    kategori = kategori,
    status = status
)

fun Buku.toUiStateBk(): InsertUiState = InsertUiState(
    insertUiEvent = toInsertUiEvent()
)

fun Buku.toInsertUiEvent(): InsertUiEvent = InsertUiEvent(
    id_buku = id_buku.toString(),
    judul = judul,
    penulis = penulis,
    kategori = kategori,
    status = status
)
