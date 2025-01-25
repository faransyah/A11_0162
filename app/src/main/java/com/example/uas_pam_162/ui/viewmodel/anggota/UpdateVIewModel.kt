package com.example.uas_pam_162.ui.viewmodel.anggota

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.repository.AnggotaRepository
import com.example.uas_pam_162.ui.view.anggota.DestinasiUpdateAnggota
import kotlinx.coroutines.launch

class UpdateAnggotaViewModel(
    savedStateHandle: SavedStateHandle,
    private val agt: AnggotaRepository
) : ViewModel(){

    val idAnggota: String = checkNotNull(savedStateHandle[DestinasiUpdateAnggota.IDANGGOTA])

    var uiStateAnggota = mutableStateOf(InsertUiStateAgt())
        private set

    init {
        ambilAgt()
    }
    private fun ambilAgt(){
        viewModelScope.launch {
            try {
                val anggota = agt.getAnggotabyId(idAnggota.toInt())
                anggota?.let {
                    uiStateAnggota.value = it.toInsertUIEventAgt()
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    fun updateAgt(
        idAnggota: String,
        anggota:Anggota
    ){
        viewModelScope.launch { try {
            agt.updateAnggota(idAnggota, anggota)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        }
    }

    fun updateAgtState(insertUiEventAgt: InsertUiEventAgt){
        uiStateAnggota.value = uiStateAnggota.value.copy(insertUiEventAgt = insertUiEventAgt)
    }
}

fun Anggota.toInsertUIEventAgt(): InsertUiStateAgt = InsertUiStateAgt(
    insertUiEventAgt = this.toDetailUiEventAgt()
)
