package com.example.uas_pam_162.ui.viewmodel.anggota

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

sealed class HomeAnggotaUiState{
    data class Success(val anggota: List<Anggota>): HomeAnggotaUiState()
    object Error: HomeAnggotaUiState()
    object Loading: HomeAnggotaUiState()

}

class HomeAnggotaViewModel(private  val agt: AnggotaRepository): ViewModel(){
    var agtUIState: HomeAnggotaUiState by  mutableStateOf(HomeAnggotaUiState.Loading)
        private set

    init {
        getAgt()
    }

    fun getAgt(){
        viewModelScope.launch {
            agtUIState = HomeAnggotaUiState.Loading
            agtUIState = try {
                HomeAnggotaUiState.Success(agt.getAnggota().data)
            }catch (e: IOException){
                HomeAnggotaUiState.Error
            }catch (e: HttpException){
                HomeAnggotaUiState.Error
            }
        }
    }

    fun deleteAgt(idAnggota: String){
        viewModelScope.launch {
            try {
                agt.deleteAnggota(idAnggota)
            }catch (e: IOException){

            }catch (e: HttpException){
                HomeAnggotaUiState.Error
            }
        }
    }

}