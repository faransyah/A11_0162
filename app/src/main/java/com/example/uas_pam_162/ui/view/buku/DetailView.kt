package com.example.uas_pam_162.ui.view.buku

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
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.buku.DetailBukuUiState
import com.example.uas_pam_162.ui.viewmodel.buku.DetailBukuViewModel
import retrofit2.http.Body

object DestinasiDetailBuku: DestinasiNavigasi{
    override val route = "detail"
    const val IDBUKU = "id"
    val routesWithArg = "$route/{$IDBUKU}"
    override val titleRes = "Detail Buku"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBukuView(
    id_buku: String,
    modifier: Modifier =Modifier,
    viewModel: DetailBukuViewModel = viewModel(factory = PenyediaViewModel.Factory),
    OnEditClick: (String) -> Unit = {},
    navigateBack: () -> Unit
){
    Scaffold(
        topBar = {
            CostumeTopAppBar(
                title = DestinasiDetailBuku.titleRes,
                canNavigateBack = true,

                navigateUp = navigateBack,
                onRefresh = { viewModel.getDetailBuku() } // Trigger refresh action on refresh
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {OnEditClick(id_buku)},
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
        val detailBukuUiState by viewModel.detailBukuUiState.collectAsState()

        BodyDetailBuku(
            modifier = Modifier.padding(innerPadding),
            detailBukuUiState = detailBukuUiState,
            retryAction = {viewModel.getDetailBuku()}
        )
    }

    }

@Composable
fun BodyDetailBuku(
    modifier: Modifier = Modifier,
    detailBukuUiState: DetailBukuUiState,
    retryAction: () -> Unit = {}
){
    when(detailBukuUiState){
        is DetailBukuUiState.Loading ->{
            OnLoading(modifier = modifier.fillMaxSize())
        }
        is DetailBukuUiState.Succes -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) { ItemDetailBuku(buku = detailBukuUiState.buku) }
        }
        is DetailBukuUiState.Error ->{
            OnError(
                retryAction = retryAction,
                modifier = modifier.fillMaxSize()
            )
        }
        else -> {
            Text("Unexpected state encountered")
        }
    }
}

@Composable
fun ItemDetailBuku(
    modifier: Modifier = Modifier,
    buku: Buku
){
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
            ComponentDetailBuku(judul = "Id Buku", isinya = buku.id_buku.toString())
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailBuku(judul = "Nama", isinya = buku.judul)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailBuku(judul = "Penulis", isinya = buku.penulis)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailBuku(judul = "Kategori", isinya = buku.kategori)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailBuku(judul = "Status", isinya = buku.status)
            Spacer(modifier = Modifier.padding(4.dp))
        }

    }
}


@Composable
fun ComponentDetailBuku(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String
){
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul : ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = isinya, fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
