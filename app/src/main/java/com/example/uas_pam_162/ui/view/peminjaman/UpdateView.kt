package com.example.uas_pam_162.ui.view.peminjaman


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.peminjaman.UpdatePeminjamanViewModel
import kotlinx.coroutines.launch


object DestinasiUpdatePeminjaman : DestinasiNavigasi {
    override val route = "update_peminjaman"
    const val IDPEMINJAMAN = "id"
    val routesWithArg = "$route/{$IDPEMINJAMAN}"
    override val titleRes = "Update Peminjaman"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePeminjamanView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdatePeminjamanViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val bukuList = viewModel._bukuList
    val anggotaList = viewModel._anggotaList
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiUpdatePeminjaman.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryBodyPeminjaman(
            insertUiStatePjm = viewModel.uiStatePjm,
            onPjmValueChange = viewModel::updatePeminjamanState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updatePeminjaman()
                    navigateBack()
                }
            },
            anggotaList = anggotaList,
            bukuList = bukuList,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}