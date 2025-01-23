package com.example.uas_pam_162.ui.view.anggota

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
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.anggota.HomeAnggotaUiState
import com.example.uas_pam_162.ui.viewmodel.anggota.HomeAnggotaViewModel

object DestinasiHomeAnggota: DestinasiNavigasi{
    override val route = "home anggota"
    override val titleRes = "Home Anggota"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenAnggota(
    navigateToItemEntry: ()-> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeAnggotaViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar ={
            CostumeTopAppBar(
                title = DestinasiHomeAnggota.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getAgt()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Kontak")
            }
        }
    ) { innerPadding ->
        HomeAnggotaStatus(
            homeAnggotaUiState = viewModel.agtUIState,
            retryAction = {viewModel.getAgt() }, modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = {
                viewModel.deleteAgt(it.id_anggota.toString())
                viewModel.getAgt()
            }
        )
    }

}


@Composable
fun HomeAnggotaStatus(
    homeAnggotaUiState: HomeAnggotaUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Anggota) -> Unit = {},
    onDetailClick: (String) -> Unit
){
    when(homeAnggotaUiState){
        is HomeAnggotaUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeAnggotaUiState.Success ->
            if (homeAnggotaUiState.anggota.isEmpty()){
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){

                    Text(text = "Tidak ada data kontak")
                }

            }else{
                AnggotaLayout(
                    anggota = homeAnggotaUiState.anggota, modifier = modifier.fillMaxWidth(),
                    onDetailClick ={
                        onDetailClick(it.id_anggota.toString())
                    },
                    onDeleteClick = {
                        onDeleteClick(it)
                    }
                )
            }
        is HomeAnggotaUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
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
fun AnggotaLayout(
    anggota: List<Anggota>,
    modifier: Modifier = Modifier,
    onDetailClick: (Anggota) -> Unit,
    onDeleteClick: (Anggota) -> Unit = {}
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
                    text = "ID Anggota",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Nama",
                    modifier = Modifier.weight(3f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Email",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Nomor Telepon",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        items(anggota) { anggotaItem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(anggotaItem) }
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = anggotaItem.id_anggota.toString(),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = anggotaItem.nama,
                    modifier = Modifier.weight(3f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = anggotaItem.email,
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = anggotaItem.nomor_telepon,
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = { onDeleteClick(anggotaItem) }) {
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
fun AngotaCard(
    anggota: Anggota,
    modifier: Modifier = Modifier,
    onDeleteClick: (Anggota) -> Unit =  {}
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
                    text = anggota.nama,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {onDeleteClick(anggota)}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
            }

            Text(
                text = anggota.email,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

}