package com.example.uas_pam_162.ui.view.anggota

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
import com.example.uas_pam_162.ui.viewmodel.anggota.UpdateAnggotaViewModel
import com.example.uas_pam_162.ui.viewmodel.anggota.toAgt
import kotlinx.coroutines.launch

object DestinasiUpdateAnggota : DestinasiNavigasi {
    override val route = "update anggota"
    const val IDANGGOTA = "nim"
    val routesWithArg = "$route/{$IDANGGOTA}"
    override val titleRes = "Update Agt"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAnggotaView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateAnggotaViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var uiStateAgt = viewModel.uiStateAnggota.value

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiUpdateAnggota.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) {  padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            EntryBodyAnggota(
                insertUiStateAgt = uiStateAgt,
                onAnggotaValueChange = { updatedValue ->
                    viewModel.updateAgtState(updatedValue)
                },
                onSaveClickAgt = {
                    uiStateAgt.insertUiEventAgt?.let { insertUiEventAgt ->
                        coroutineScope.launch {
                            viewModel.updateAgt(
                                idAnggota = viewModel.idAnggota,
                                anggota = insertUiEventAgt.toAgt()
                            )
                            navigateBack()
                        }
                    }
                }
            )
        }
    }
}