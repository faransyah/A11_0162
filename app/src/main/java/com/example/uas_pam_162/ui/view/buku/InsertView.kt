package com.example.uas_pam_162.ui.view.buku

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.buku.InsertBukuViewModel
import com.example.uas_pam_162.ui.viewmodel.buku.InsertUiEventBuku
import com.example.uas_pam_162.ui.viewmodel.buku.InsertUiStateBuku
import kotlinx.coroutines.launch

object DestinasiBukuEntry : DestinasiNavigasi{
    override val route = "item_entry Buku"
    override val titleRes = "Entry Buku"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryBukuScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertBukuViewModel = viewModel(factory = PenyediaViewModel.Factory),

){
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    // Mengambil state UI
    val uiState = viewModel.uiStateBuku
    val isFormValid = viewModel.isFormValid() // Validasi form dari ViewModel


    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CostumeTopAppBar(
                title = DestinasiBukuEntry.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack

            )
        }
        ) { innerPadding ->
        EntryBodyBuku(
            insertUiState = viewModel.uiStateBuku,
            onBukuValueChange = viewModel::UpdateInsertBkState,
            onSaveClick = {
                coroutineScope.launch {
                    if (isFormValid){
                    viewModel.insertBk()
                    navigateBack()
                    }else {
                        Log.e("EntryBukuScreen", "Form tidak lengkap!")
                    }
                }
            },
            isFormValid = isFormValid,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()


        )
    }
}


@Composable
fun EntryBodyBuku(
    insertUiState: InsertUiStateBuku,
    onBukuValueChange: (InsertUiEventBuku) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFormValid: Boolean = true
){
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputBuku(
            insertUiEventBuku = insertUiState.insertUiEvent,
            onValueChange = onBukuValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputBuku(
    insertUiEventBuku: InsertUiEventBuku,
    modifier: Modifier = Modifier,
    onValueChange: (InsertUiEventBuku) -> Unit = {},
    enabled: Boolean = true
){
    val jenisPilihan = listOf("Tersedia", "Dipinjam")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEventBuku.judul,
            onValueChange = { onValueChange(insertUiEventBuku.copy(judul = it)) },
            label = { Text("Judul") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEventBuku.penulis   ,
            onValueChange = { onValueChange(insertUiEventBuku.copy(penulis = it)) },
            label = { Text("Penulis") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEventBuku.kategori   ,
            onValueChange = { onValueChange(insertUiEventBuku.copy(kategori = it)) },
            label = { Text("Kategori") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Status Ketersediaan")
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            jenisPilihan.forEach { pilihan ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = insertUiEventBuku.status == pilihan,
                        onClick = { onValueChange(insertUiEventBuku.copy(status = pilihan)) },
                        enabled = enabled
                    )
                    Text(text = pilihan)
                }
            }
        }
    }
}