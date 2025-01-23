package com.example.uas_pam_162.ui.viewmodel.buku

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.repository.BukuRepository
import com.example.uas_pam_162.ui.view.buku.DestinasiDetailBuku
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailBukuUiState{
    data class Succes(val buku: Buku): DetailBukuUiState()
    object Error: DetailBukuUiState()
    object Loading: DetailBukuUiState()
}

class DetailBukuViewModel(
    savedStateHandle: SavedStateHandle,
    private val bk: BukuRepository
): ViewModel(){
    private val _idBuku: String = checkNotNull(savedStateHandle[DestinasiDetailBuku.IDBUKU])

    private val _detailUiState = MutableStateFlow<DetailBukuUiState>(DetailBukuUiState.Loading)
    val detailBukuUiState: StateFlow<DetailBukuUiState> = _detailUiState

    init {
        getDetailBuku()
    }

    fun getDetailBuku(){
        viewModelScope.launch {
            try {
                _detailUiState.value = DetailBukuUiState.Loading

                val buku = bk.getBukubyId(_idBuku)

                if (buku != null){

                    _detailUiState.value = DetailBukuUiState.Succes(buku)

                }else{
                    _detailUiState.value = DetailBukuUiState.Error
                }
            }catch (e: Exception){

                _detailUiState.value = DetailBukuUiState.Error
            }
        }
    }
}




fun Buku.toDetailUiEvent(): InsertUiEventBuku{
    return InsertUiEventBuku(
        id_buku = id_buku,
        judul = judul,
        penulis = penulis,
        kategori = kategori,
        status = status
    )
}