package com.example.uas_pam_162.ui.view.pengembalian

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_pam_162.R
import com.example.uas_pam_162.model.Peminjaman
import com.example.uas_pam_162.model.Pengembalian
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.view.anggota.OnError
import com.example.uas_pam_162.ui.view.peminjaman.OnLoading
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.pengembalian.HomePengembalianUiState
import com.example.uas_pam_162.ui.viewmodel.pengembalian.HomePengembalianViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DestinasiHomePengembalian : DestinasiNavigasi {
    override val route = "home pengembalian"
    override val titleRes = "Home Pengembalian"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenPengembalian(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomePengembalianViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = "Pengembalian",
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = { viewModel.getPng() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Pengembalian")
            }
        }
    ) { innerPadding ->
        HomePengembalianStatus(
            homePengembalianUiState = viewModel.pngUIState,
            retryAction = { viewModel.getPng() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { pengembalian ->
                viewModel.deletePng(pengembalian.id_pengembalian.toString())
            }
        )
    }
}

@Composable
fun HomePengembalianStatus(
    homePengembalianUiState: HomePengembalianUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Pengembalian) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when (homePengembalianUiState) {
        is HomePengembalianUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomePengembalianUiState.Success -> {
            if (homePengembalianUiState.pengembalian.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Pengembalian")
                }
            } else {
                LazyColumn(
                    modifier = modifier,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(homePengembalianUiState.pengembalian) { pengembalianItem ->
                        PengembalianCard(
                            pengembalian = pengembalianItem,
                            peminjamanMap = homePengembalianUiState.peminjaman,
                            bukuMap = homePengembalianUiState.buku, // <-- Teruskan Map<Int, String> untuk judul buku
                            anggotaMap = homePengembalianUiState.anggota, // <-- Teruskan Map<Int, String> untuk nama anggota
                            modifier = Modifier.fillMaxWidth(),
                            onDetailClick = { onDetailClick(pengembalianItem.id_pengembalian.toString()) },
                            onDeleteClick = onDeleteClick
                        )
                    }
                }
            }
        }
        is HomePengembalianUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.logo),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun PengembalianCard(
    pengembalian: Pengembalian,
    peminjamanMap: Map<Int, Peminjaman>, // Map<id_peminjaman, Peminjaman>
    bukuMap: Map<Int, String>, // Map<id_buku, judul>
    anggotaMap: Map<Int, String>, // Map<id_anggota, nama>
    modifier: Modifier = Modifier,
    onDetailClick: (Pengembalian) -> Unit,
    onDeleteClick: (Pengembalian) -> Unit = {}
) {
    val peminjaman = peminjamanMap[pengembalian.id_peminjaman] // Cari Peminjaman berdasarkan id_peminjaman

    if (peminjaman != null) {
        // Format tanggal pengembalian
        val formattedTanggalDikembalikan = formatTanggalPengembalian(pengembalian.tanggal_dikembalikan)

        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onDetailClick(pengembalian) }
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // ID Pengembalian dan Tombol Delete
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ID Pengembalian: ${pengembalian.id_pengembalian}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = { onDeleteClick(pengembalian) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

                // Nama Anggota (gunakan anggotaMap)
                Text(
                    text = "Nama Anggota: ${anggotaMap[peminjaman.id_anggota] ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Judul Buku (gunakan bukuMap)
                Text(
                    text = "Judul Buku: ${bukuMap[peminjaman.id_buku] ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Tanggal Dikembalikan (diformat)
                Text(
                    text = "Tanggal Dikembalikan: $formattedTanggalDikembalikan",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    } else {
        // Tampilkan pesan jika data peminjaman tidak ditemukan
        Text(
            text = "Data peminjaman tidak ditemukan untuk ID ${pengembalian.id_peminjaman}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

fun formatTanggalPengembalian(tanggal: String?): String {
    if (tanggal.isNullOrEmpty()) return "Belum Dikembalikan" // Jika tanggal null atau kosong

    return try {
        // Format input (misalnya: "yyyy-MM-dd")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Format output (misalnya: "dd MMMM yyyy")
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        // Parse tanggal dari string ke Date
        val date: Date = inputFormat.parse(tanggal) ?: return "Format Salah"
        // Format Date ke string yang diinginkan
        outputFormat.format(date)
    } catch (e: Exception) {
        "Format Salah" // Jika parsing gagal
    }
}