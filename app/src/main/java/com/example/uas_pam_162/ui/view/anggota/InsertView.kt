package com.example.uas_pam_162.ui.view.anggota

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.view.buku.FormInputBuku
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.anggota.InsertAnggotaViewModel
import com.example.uas_pam_162.ui.viewmodel.anggota.InsertUiEventAgt
import com.example.uas_pam_162.ui.viewmodel.anggota.InsertUiStateAgt
import com.example.uas_pam_162.ui.viewmodel.buku.InsertUiEventBuku
import kotlinx.coroutines.launch


object DestinasiEntryAnggota: DestinasiNavigasi{
    override val route = "item_entry_Agt"
    override val titleRes = "Entry Agt"

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryAnggotatScreen(
    navigateBackAgt: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertAnggotaViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold( modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CostumeTopAppBar(
                title = DestinasiEntryAnggota.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBackAgt
            )
        }

    ) { innerPadding ->
        EntryBodyAnggota(
            insertUiStateAgt = viewModel.uiStateAgt,
            onAnggotaValueChange = viewModel::UpdateInsertAgtState,
            onSaveClickAgt = {
                coroutineScope.launch {
                    viewModel.insertAgt()
                    navigateBackAgt()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()

        )
    }

}



@Composable
fun EntryBodyAnggota(
    insertUiStateAgt: InsertUiStateAgt,
    onAnggotaValueChange: (InsertUiEventAgt) -> Unit,
    onSaveClickAgt: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputAnggota(
            insertUiEventAgt = insertUiStateAgt.insertUiEventAgt,
            onValueChangeAgt = onAnggotaValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClickAgt,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}



@Composable
fun FormInputAnggota(
  insertUiEventAgt: InsertUiEventAgt,
  modifier: Modifier = Modifier,
  onValueChangeAgt: (InsertUiEventAgt) -> Unit = {},
  enabled: Boolean = true
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        OutlinedTextField(
            value = insertUiEventAgt.nama,
            onValueChange = { onValueChangeAgt(insertUiEventAgt.copy(nama = it)) },
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEventAgt.email,
            onValueChange = { onValueChangeAgt(insertUiEventAgt.copy(email = it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEventAgt.nomor_telepon,
            onValueChange = { onValueChangeAgt(insertUiEventAgt.copy(nomor_telepon = it)) },
            label = { Text("Nomor Telepon") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}