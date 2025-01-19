package com.example.uas_pam_162.ui.viewmodel.buku

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.repository.BukuRepository
import kotlinx.coroutines.launch
import okio.IOException

sealed class HomeBukuUiState{
    data class Success(val buku: List<Buku>): HomeBukuUiState()
    object Error: HomeBukuUiState()
    object Loading: HomeBukuUiState()
}

class HomeBukuViewModel (private val bk: BukuRepository): ViewModel(){
    var bkUIState: HomeBukuUiState by mutableStateOf(HomeBukuUiState.Loading)
        private set

    init {
        getBk()
    }

    fun getBk(){
        viewModelScope.launch {
            bkUIState = HomeBukuUiState.Loading
            bkUIState = try {
                HomeBukuUiState.Success(bk.getBuku().data)
            }catch (e: IOException){
                HomeBukuUiState.Error
            }catch (e: HttpException){
                HomeBukuUiState.Error
            }
        }
    }

    fun deleteBk(idBuku: String){
        viewModelScope.launch {
            try {
                bk.deleteBuku(idBuku)
            }catch (e: IOException){
                HomeBukuUiState.Error
            }catch (e:HttpException){
                HomeBukuUiState.Error
            }
        }
    }
}









