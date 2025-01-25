package com.example.uas_pam_162.ui.view.buku

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.buku.UpdateBukuViewModel
import com.example.uas_pam_162.ui.viewmodel.buku.toBk
import kotlinx.coroutines.launch

object DestinasiUpdateBuku : DestinasiNavigasi{
    override val route = "update"
    const val IDBUKU = "id"
    val routesWithArg = "$route/{$IDBUKU}"
    override val titleRes = "Update Buku"

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateViewBuku(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateBukuViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val uiState = viewModel.uiState.value

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiUpdateBuku.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            EntryBodyBuku(
                insertUiState = uiState,
                onBukuValueChange = { updatedValue ->
                    viewModel.updateBukuState(updatedValue)
                },
                onSaveClick = {
                    uiState.insertUiEvent?.let { insertUiEvent ->
                        coroutineScope.launch {
                            viewModel.updateBuku(
                                idBuku = viewModel.idBuku,
                                buku = insertUiEvent.toBk()
                            )
                            navigateBack()
                        }
                    }
                }
            )
        }
    }
}