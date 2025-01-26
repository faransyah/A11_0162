package com.example.uas_pam_162.ui.view.peminjaman

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_pam_162.model.Peminjaman
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.peminjaman.DetailPeminjamanUiState
import com.example.uas_pam_162.ui.viewmodel.peminjaman.DetailPeminjamanViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DestinasiDetailPeminjaman: DestinasiNavigasi {
    override val route = "detail Pjm"
    const val IDPJM = "id"
    val routesWithArg = "$route/{$IDPJM}"
    override val titleRes = "Detail Peminjaman"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPeminjamanView(
    idPeminjaman: String,
    modifier: Modifier = Modifier,
    viewModel: DetailPeminjamanViewModel = viewModel(factory = PenyediaViewModel.Factory),
    navigateBack: () -> Unit,
    OnEditClick: (String) -> Unit = {},
) {
    // Collect UI state from ViewModel
    val detailPeminjamanUiState by viewModel.detailPeminjamanUiState.collectAsState()

    Scaffold(
        topBar = {
            CostumeTopAppBar(
                title = "Detail Peminjaman",
                canNavigateBack = true,
                navigateUp = navigateBack,
                onRefresh = { viewModel.getDetailPeminjaman() } // Trigger refresh action
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {OnEditClick(idPeminjaman)},
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit BUku"
                )
            }
        }
    ) { innerPadding ->
        BodyDetailPeminjaman(
            modifier = Modifier.padding(innerPadding),
            detailPeminjamanUiState = detailPeminjamanUiState,
            retryAction = { viewModel.getDetailPeminjaman() }
        )
    }
}

@Composable
fun BodyDetailPeminjaman(
    modifier: Modifier = Modifier,
    detailPeminjamanUiState: DetailPeminjamanUiState,
    retryAction: () -> Unit = {}
) {
    when (detailPeminjamanUiState) {
        is DetailPeminjamanUiState.Loading -> {
            OnLoading(modifier = modifier.fillMaxSize())
        }
        is DetailPeminjamanUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ItemDetailPeminjaman(
                    peminjaman = detailPeminjamanUiState.peminjaman,
                    judulBuku = detailPeminjamanUiState.judulBuku,
                    namaAnggota = detailPeminjamanUiState.namaAnggota
                )
            }
        }
        is DetailPeminjamanUiState.Error -> {
            OnError(
                retryAction = retryAction,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ItemDetailPeminjaman(
    modifier: Modifier = Modifier,
    peminjaman: Peminjaman,
    judulBuku: String,
    namaAnggota: String
) {
    // Format tanggal peminjaman dan pengembalian
    val formattedTanggalPeminjaman = formatTanggal(peminjaman.tanggal_peminjaman)
    val formattedTanggalPengembalian = formatTanggal(peminjaman.tanggal_pengembalian)

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
            // ID Peminjaman
            ComponentDetailPeminjaman(judul = "ID Peminjaman", isinya = peminjaman.id_peminjaman.toString())
            Spacer(modifier = Modifier.padding(4.dp))

            // Judul Buku
            ComponentDetailPeminjaman(judul = "Judul Buku", isinya = judulBuku)
            Spacer(modifier = Modifier.padding(4.dp))

            // Nama Anggota
            ComponentDetailPeminjaman(judul = "Nama Anggota", isinya = namaAnggota)
            Spacer(modifier = Modifier.padding(4.dp))

            // Tanggal Peminjaman (diformat)
            ComponentDetailPeminjaman(judul = "Tanggal Peminjaman", isinya = formattedTanggalPeminjaman)
            Spacer(modifier = Modifier.padding(4.dp))

            // Tanggal Pengembalian (diformat)
            ComponentDetailPeminjaman(
                judul = "Tanggal Pengembalian",
                isinya = formattedTanggalPengembalian
            )
        }
    }
}

@Composable
fun ComponentDetailPeminjaman(
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


