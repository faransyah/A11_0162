package com.example.uas_pam_162.ui.viewmodel.anggota

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.repository.AnggotaRepository
import com.example.uas_pam_162.ui.view.anggota.DestinasiDetailAnggota
import com.example.uas_pam_162.ui.view.buku.DestinasiDetailBuku
import com.example.uas_pam_162.ui.viewmodel.buku.InsertUiEventBuku
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailAnggotaUiState{
    data class Success(val anggota: Anggota): DetailAnggotaUiState()
    object Error: DetailAnggotaUiState()
    object Loading: DetailAnggotaUiState()
}

class DetailAgtViewModel(
    savedStateHandle: SavedStateHandle,
    private val agt: AnggotaRepository
): ViewModel(){
    private val _idAgt: String = checkNotNull(savedStateHandle[DestinasiDetailAnggota.IDAGT])

    private val _detailAnggotaUiState = MutableStateFlow<DetailAnggotaUiState>(DetailAnggotaUiState.Loading)
    val detailAnggotaUiState: StateFlow<DetailAnggotaUiState> = _detailAnggotaUiState

    init {
        getDetailAnggota()
    }

    fun getDetailAnggota(){
        viewModelScope.launch {
            try {

                _detailAnggotaUiState.value = DetailAnggotaUiState.Loading

                val anggota = agt.getAnggotabyId(_idAgt.toInt() )

                if (anggota != null){

                    _detailAnggotaUiState.value = DetailAnggotaUiState.Success(anggota)
                }else{

                    _detailAnggotaUiState.value = DetailAnggotaUiState.Error
                }
            }catch (e: Exception){
                _detailAnggotaUiState.value = DetailAnggotaUiState.Error
            }
        }
    }
}

fun Anggota.toDetailUiEventAgt(): InsertUiEventAgt {
    return InsertUiEventAgt(
        id_anggota = id_anggota,
        nama = nama,
        email = email,
        nomor_telepon = nomor_telepon
    )
}