package com.example.uas_pam_162.ui.view.peminjaman


import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uas_pam_162.model.Peminjaman
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_pam_162.R
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.peminjaman.HomePeminjamanUiState
import com.example.uas_pam_162.ui.viewmodel.peminjaman.HomePeminjamanViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object DestinasiHomePeminjaman: DestinasiNavigasi {
    override val route = "home peminjaman"
    override val titleRes = "Home Peminjaman"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenPeminjaman(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    navigateBack: () -> Unit,
    viewModel: HomePeminjamanViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Collect UI state from ViewModel dengan lifecycle-aware
    val homePeminjamanUiState by viewModel.pjmUIState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = "Peminjaman",
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack,
                onRefresh = {
                    viewModel.getPjm()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Peminjaman"
                )
            }
        }
    ) { innerPadding ->
        HomePeminjamanStatus(
            homePeminjamanUiState = homePeminjamanUiState,
            retryAction = { viewModel.getPjm() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { peminjaman ->
                viewModel.deletePjm(peminjaman.id_peminjaman.toString())
                viewModel.getPjm()
            }
        )
    }
}

@Composable
fun HomePeminjamanStatus(
    homePeminjamanUiState: HomePeminjamanUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Peminjaman) -> Unit = {}
) {
    when (homePeminjamanUiState) {
        is HomePeminjamanUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomePeminjamanUiState.Success -> {
            Column(modifier = modifier.fillMaxSize()) {
                // Tampilkan daftar peminjaman aktif
                Text(
                    text = "Peminjaman Aktif",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                if (homePeminjamanUiState.peminjamanAktif.isEmpty()) {
                    Text(
                        text = "Tidak ada peminjaman aktif",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    PeminjamanLayout(
                        peminjaman = homePeminjamanUiState.peminjamanAktif,
                        buku = homePeminjamanUiState.buku,
                        anggota = homePeminjamanUiState.anggota,
                        modifier = Modifier.fillMaxWidth(),
                        onDetailClick = { peminjaman ->
                            onDetailClick(peminjaman.id_peminjaman.toString())
                        },
                        onDeleteClick = { peminjaman ->
                            onDeleteClick(peminjaman)
                        }
                    )
                }

                // Tampilkan daftar semua peminjaman
                Text(
                    text = "Semua Peminjaman",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                if (homePeminjamanUiState.peminjaman.isEmpty()) {
                    Text(
                        text = "Tidak ada data Peminjaman",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    PeminjamanLayout(
                        peminjaman = homePeminjamanUiState.peminjaman,
                        buku = homePeminjamanUiState.buku,
                        anggota = homePeminjamanUiState.anggota,
                        modifier = Modifier.fillMaxWidth(),
                        onDetailClick = { peminjaman ->
                            onDetailClick(peminjaman.id_peminjaman.toString())
                        },
                        onDeleteClick = { peminjaman ->
                            onDeleteClick(peminjaman)
                        }
                    )
                }
            }
        }
        is HomePeminjamanUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}


@Composable
fun PeminjamanLayout(
    peminjaman: List<Peminjaman>,
    buku: List<Buku>, // Mengubah dari Map<Int, String> menjadi List<Buku>
    anggota: List<Anggota>, // Mengubah dari Map<Int, String> menjadi List<Anggota>
    modifier: Modifier = Modifier,
    onDetailClick: (Peminjaman) -> Unit,
    onDeleteClick: (Peminjaman) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(peminjaman) { peminjamanItem ->
            PeminjamanCard(
                peminjaman = peminjamanItem,
                buku = buku,
                anggota = anggota,
                modifier = Modifier.fillMaxWidth(),
                onDetailClick = { onDetailClick(peminjamanItem) },
                onDeleteClick = { onDeleteClick(peminjamanItem) }
            )
        }
    }
}



@Composable
fun OnLoading(modifier: Modifier = Modifier)
{
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.logo),
        contentDescription = stringResource(R.string.loading)
    )
}


@Composable
fun OnError(retryAction: () -> Unit,modifier: Modifier = Modifier){
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
fun PeminjamanCard(
    peminjaman: Peminjaman,
    buku: List<Buku>, // List<Buku> untuk mencari judul dan status buku
    anggota: List<Anggota>, // List<Anggota> untuk mencari nama anggota
    modifier: Modifier = Modifier,
    onDetailClick: (Peminjaman) -> Unit,
    onDeleteClick: (Peminjaman) -> Unit = {},
    onKembalikanClick: (Peminjaman) -> Unit = {} // Tambahkan parameter ini
) {
    // Cari judul dan status buku berdasarkan id_buku
    val bukuInfo = buku.find { it.id_buku == peminjaman.id_buku }
    val judulBuku = bukuInfo?.judul ?: "Unknown"
    val statusBuku = bukuInfo?.status ?: "Unknown"

    // Cari nama anggota berdasarkan id_anggota
    val namaAnggota = anggota.find { it.id_anggota == peminjaman.id_anggota }?.nama ?: "Unknown"

    // Format tanggal peminjaman dan pengembalian
    val formattedTanggalPeminjaman = formatTanggal(peminjaman.tanggal_peminjaman)
    val formattedTanggalPengembalian = formatTanggal(peminjaman.tanggal_pengembalian)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDetailClick(peminjaman) }
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ID Peminjaman dan Tombol Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID Peminjaman: ${peminjaman.id_peminjaman}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = { onDeleteClick(peminjaman) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Judul Buku
            Text(
                text = "Judul Buku: $judulBuku",
                style = MaterialTheme.typography.bodyMedium
            )

            // Status Buku
            Text(
                text = "Status Buku: $statusBuku",
                style = MaterialTheme.typography.bodyMedium
            )

            // Nama Anggota
            Text(
                text = "Nama Anggota: $namaAnggota",
                style = MaterialTheme.typography.bodyMedium
            )

            // Tanggal Peminjaman (diformat)
            Text(
                text = "Tanggal Peminjaman: $formattedTanggalPeminjaman",
                style = MaterialTheme.typography.bodyMedium
            )

            // Tanggal Pengembalian (diformat)
            Text(
                text = "Tanggal Pengembalian: $formattedTanggalPengembalian",
                style = MaterialTheme.typography.bodyMedium
            )

            // Tombol Kembalikan Buku
            Button(
                onClick = { onKembalikanClick(peminjaman) }, // Panggil fungsi onKembalikanClick
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembalikan Buku")
            }
        }
    }
}

fun formatTanggal(tanggal: String?): String {
    if (tanggal.isNullOrEmpty()) return "Belum Kembali" // Jika tanggal null atau kosong

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