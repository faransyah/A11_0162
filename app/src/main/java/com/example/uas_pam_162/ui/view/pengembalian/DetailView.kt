package com.example.uas_pam_162.ui.view.pengembalian

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_pam_162.model.Pengembalian
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.pengembalian.DetailPengembalianUiState
import com.example.uas_pam_162.ui.viewmodel.pengembalian.DetailPengembalianViewModel

object DestinasiDetailPengembalian: DestinasiNavigasi {
    override val route = "detail pngn"
    const val IDPNGN = "id"
    val routesWithArg = "$route/{$IDPNGN}"
    override val titleRes = "Detail Pengembalian"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPengembalianView(
    idPengembalian: String,
    modifier: Modifier = Modifier,
    viewModel: DetailPengembalianViewModel = viewModel(factory = PenyediaViewModel.Factory),
    navigateBack: () -> Unit
) {
    val detailPengembalianUiState by viewModel.detailPengembalianUiState.collectAsState()

    Scaffold(
        topBar = {
            CostumeTopAppBar(
                title = "Detail Pengembalian",
                canNavigateBack = true,
                navigateUp = navigateBack,
                onRefresh = { viewModel.getDetailPengembalian() }
            )
        }
    ) { innerPadding ->
        BodyDetailPengembalian(
            modifier = Modifier.padding(innerPadding),
            detailPengembalianUiState = detailPengembalianUiState,
            retryAction = { viewModel.getDetailPengembalian() }
        )
    }
}

@Composable
fun BodyDetailPengembalian(
    modifier: Modifier = Modifier,
    detailPengembalianUiState: DetailPengembalianUiState,
    retryAction: () -> Unit = {}
) {
    when (detailPengembalianUiState) {
        is DetailPengembalianUiState.Loading -> {
            OnLoading(modifier = modifier.fillMaxSize())
        }
        is DetailPengembalianUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ItemDetailPengembalian(
                    pengembalian = detailPengembalianUiState.pengembalian,
                    judulBuku = detailPengembalianUiState.judulBuku,
                    namaAnggota = detailPengembalianUiState.namaAnggota,
                    tanggalDikembalikan = detailPengembalianUiState.tanggalDikembalikan
                )
            }
        }
        is DetailPengembalianUiState.Error -> {
            OnError(
                retryAction = retryAction,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ItemDetailPengembalian(
    modifier: Modifier = Modifier,
    pengembalian: Pengembalian,
    judulBuku: String,
    namaAnggota: String,
    tanggalDikembalikan: String
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // ID Pengembalian
            ComponentDetailPengembalian(judul = "ID Pengembalian", isinya = pengembalian.id_pengembalian.toString())
            Spacer(modifier = Modifier.padding(4.dp))

            // Judul Buku
            ComponentDetailPengembalian(judul = "Judul Buku", isinya = judulBuku)
            Spacer(modifier = Modifier.padding(4.dp))

            // Nama Anggota
            ComponentDetailPengembalian(judul = "Nama Anggota", isinya = namaAnggota)
            Spacer(modifier = Modifier.padding(4.dp))

            // Tanggal Dikembalikan
            ComponentDetailPengembalian(judul = "Tanggal Dikembalikan", isinya = tanggalDikembalikan)
        }
    }
}

@Composable
fun ComponentDetailPengembalian(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul : ",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = isinya,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}
