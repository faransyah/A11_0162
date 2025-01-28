package com.example.uas_pam_162.ui.view.buku

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
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_pam_162.R
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.buku.HomeBukuUiState
import com.example.uas_pam_162.ui.viewmodel.buku.HomeBukuViewModel


object DestinasiHomeBuku: DestinasiNavigasi{
    override val route = "home buku"
    override val titleRes = "Home Buku"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenBuku(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeBukuViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // State untuk menampung teks pencarian
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiHomeBuku.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack,
                onRefresh = {
                    // Kirimkan searchQuery saat refresh
                    viewModel.getBk(searchQuery)  // Refresh buku dengan query yang ada
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Buku")
            }
        }
    ) { innerPadding ->
        // Layout utama Home Screen
        Column(modifier = Modifier.padding(innerPadding)) {

            // TextField untuk pencarian
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Buku") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Pastikan ada ruang
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.getBk(searchQuery) // Fungsi untuk filter buku berdasarkan pencarian
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Buku")
                    }
                }
            )

            // Menampilkan daftar buku berdasarkan hasil pencarian
            HomeBukuStatus(
                homeBukuUiState = viewModel.bkUIState,
                retryAction = { viewModel.getBk(searchQuery) },
                modifier = Modifier.padding(innerPadding),
                onDetailClick = onDetailClick,
                onDeleteClick = {
                    viewModel.deleteBk(it.id_buku.toString())
                    viewModel.getBk(searchQuery)  // Refresh setelah hapus
                }
            )
        }
    }
}


@Composable
fun HomeBukuStatus(
    homeBukuUiState: HomeBukuUiState,
    retryAction: ()-> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Buku) -> Unit = {},
    onDetailClick: (String) -> Unit
){
    when(homeBukuUiState){
        is HomeBukuUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeBukuUiState.Success ->
            if(homeBukuUiState.buku.isEmpty()){
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "Tidak ada data Kontak")
                }
            }else{
                BukuLayout(
                    buku = homeBukuUiState.buku, modifier = modifier.fillMaxWidth(),
                    onDetailClick = {
                        onDetailClick(it.id_buku.toString())
                    },
                    onDeleteClick = {
                        onDeleteClick(it)
                    }
                )
            }
        is HomeBukuUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
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
fun BukuLayout(
    buku: List<Buku>,
    modifier: Modifier = Modifier,
    onDetailClick: (Buku) -> Unit,
    onDeleteClick: (Buku) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            // Table Header
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ID Buku",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Judul",
                    modifier = Modifier.weight(3f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Penulis",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Kategori",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Status",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        items(buku) { bukuItem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(bukuItem) }
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = bukuItem.id_buku.toString(),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = bukuItem.judul,
                    modifier = Modifier.weight(3f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = bukuItem.penulis,
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = bukuItem.kategori,
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = bukuItem.status,
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = { onDeleteClick(bukuItem) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}



@Composable
fun BukuCard(
    buku: Buku,
    modifier: Modifier = Modifier,
    onDeleteClick: (Buku) -> Unit = {}
){
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buku.judul,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {onDeleteClick(buku)}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
                Text(
                    text = buku.id_buku.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = buku.penulis,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = buku.kategori,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

}