package com.example.uas_pam_162.ui.view.anggota

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
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.anggota.DetailAgtViewModel
import com.example.uas_pam_162.ui.viewmodel.anggota.DetailAnggotaUiState

object DestinasiDetailAnggota: DestinasiNavigasi {
    override val route = "detail anggota"
    const val IDAGT = "id"
    val routesWithArg = "$route/{$IDAGT}"
    override val titleRes = "Detail Anggota"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailAgtView(
    id_anggota: String,
    modifier: Modifier = Modifier,
    viewModel: DetailAgtViewModel = viewModel(factory = PenyediaViewModel.Factory),
    OnEditClick: (String) -> Unit = {},
    navigateBack: () -> Unit
){
    Scaffold(
        topBar = {
            CostumeTopAppBar(
                title = DestinasiDetailAnggota.titleRes,
                canNavigateBack = true,

                navigateUp = navigateBack,
                onRefresh = {viewModel.getDetailAnggota()}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {OnEditClick(id_anggota)},
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Anggota"

                )
            }
        }
    ) { innerPadding ->
        val detailAnggotaUiState by viewModel.detailAnggotaUiState.collectAsState()

        BodyDetailAnggota(
            modifier = Modifier.padding(innerPadding),
            detailAnggotaUiState = detailAnggotaUiState,
            retryAction = {viewModel.getDetailAnggota()}

        )
    }
}

@Composable
fun BodyDetailAnggota(
    modifier: Modifier = Modifier,
    detailAnggotaUiState: DetailAnggotaUiState,
    retryAction: () -> Unit = {}
){
    when(detailAnggotaUiState){
        is DetailAnggotaUiState.Loading ->{
            OnLoading(modifier = modifier.fillMaxSize())
        }
        is DetailAnggotaUiState.Success ->{
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {  ItemDetailAnggota(anggota = detailAnggotaUiState.anggota)}
        }
        is DetailAnggotaUiState.Error -> {
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
fun ItemDetailAnggota(
    modifier: Modifier =Modifier,
    anggota: Anggota
){
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ComponentDetailAnggota(judul = "ID", isinya = anggota.id_anggota.toString())
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailAnggota(judul = "Nama", isinya = anggota.nama)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailAnggota(judul = "Email", isinya = anggota.email)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailAnggota(judul = "No Telepon", isinya = anggota.nomor_telepon)
            Spacer(modifier = Modifier.padding(4.dp))
        }

    }
}


@Composable
fun ComponentDetailAnggota(
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

