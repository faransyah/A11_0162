package com.example.uas_pam_162.ui.viewmodel.buku

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.repository.BukuRepository
import com.example.uas_pam_162.ui.view.buku.DestinasiUpdateBuku
import kotlinx.coroutines.launch

class UpdateBukuViewModel(
    savedStateHandle: SavedStateHandle,
    private val bk: BukuRepository

): ViewModel(){

    val idBuku: String = checkNotNull(savedStateHandle[DestinasiUpdateBuku.IDBUKU])

    var uiState = mutableStateOf(InsertUiStateBuku())
        private set

    init {
        ambilBuku()
    }

    private fun ambilBuku(){
        viewModelScope.launch {
            try {
                val buku = bk.getBukubyId(idBuku)
                buku?.let {
                    uiState.value = it.toInsertUIEvent()
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

    }

    fun updateBuku(
        idBuku: String,
        buku: Buku
    ){
        viewModelScope.launch {
            try {
                bk.updateBuku(idBuku, buku)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

    }


    fun updateBukuState(insertUiEvent: InsertUiEventBuku){
        uiState.value = uiState.value.copy(insertUiEvent = insertUiEvent)
    }

}

fun Buku.toInsertUIEvent(): InsertUiStateBuku = InsertUiStateBuku(
    insertUiEvent = this.toDetailUiEvent()
)