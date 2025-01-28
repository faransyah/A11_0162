package com.example.uas_pam_162.ui.view.peminjaman

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_pam_162.model.Anggota
import com.example.uas_pam_162.model.Buku
import com.example.uas_pam_162.ui.customwidget.CostumeTopAppBar
import com.example.uas_pam_162.ui.navigation.DestinasiNavigasi
import com.example.uas_pam_162.ui.view.DropdownSelector
import com.example.uas_pam_162.ui.viewmodel.PenyediaViewModel
import com.example.uas_pam_162.ui.viewmodel.peminjaman.InsertUiEventPeminjam
import com.example.uas_pam_162.ui.viewmodel.peminjaman.InsertUiStatePjm
import com.example.uas_pam_162.ui.viewmodel.peminjaman.InsertViewModelPeminjaman
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DestinasiPjmEntry : DestinasiNavigasi {
    override val route = "item_entry peminjaman"
    override val titleRes = "Entry Peminjaman"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPjmScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModelPeminjaman = viewModel(factory = PenyediaViewModel.Factory),
) {
    val bukuList = viewModel._bukuList
    val anggotaList = viewModel._anggotaList
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiPjmEntry.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryBodyPeminjaman(
            insertUiStatePjm = viewModel.uiStatePjm,
            onPjmValueChange = viewModel::updateInsertState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.validateAndInsertPjm()) {
                        navigateBack()
                    }
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
@Composable
fun EntryBodyPeminjaman(
    insertUiStatePjm: InsertUiStatePjm,
    onPjmValueChange: (InsertUiEventPeminjam) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    anggotaList: List<Anggota>,
    bukuList: List<Buku>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputPeminjaman(
            insertUiEventPeminjam = insertUiStatePjm.insertUiEventPeminjam,
            onPeminjamValueChange = onPjmValueChange,
            anggotaList = anggotaList,
            bukuList = bukuList,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputPeminjaman(
    insertUiEventPeminjam: InsertUiEventPeminjam,
    onPeminjamValueChange: (InsertUiEventPeminjam) -> Unit,
    bukuList: List<Buku>,
    anggotaList: List<Anggota>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Format tanggal
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // DatePicker untuk tanggal peminjaman
    val tanggalPeminjamanPicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = dateFormat.format(Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time)
            onPeminjamValueChange(insertUiEventPeminjam.copy(tanggal_peminjaman = selectedDate))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // DatePicker untuk tanggal pengembalian
    val tanggalPengembalianPicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = dateFormat.format(Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time)
            onPeminjamValueChange(insertUiEventPeminjam.copy(tanggal_pengembalian = selectedDate))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Dropdown untuk memilih buku
        DropdownSelector(
            label = "Buku",
            items = bukuList.map { it.judul },
            selectedItem = bukuList.find { it.id_buku == insertUiEventPeminjam.id_buku }?.judul ?: "Pilih Buku",
            onItemSelected = { selected ->
                val selectedBuku = bukuList.find { it.judul == selected }
                selectedBuku?.let {
                    onPeminjamValueChange(insertUiEventPeminjam.copy(id_buku = it.id_buku))
                }
            },
            enabled = enabled
        )

        // Dropdown untuk memilih anggota
        DropdownSelector(
            label = "Anggota",
            items = anggotaList.map { it.nama },
            selectedItem = anggotaList.find { it.id_anggota == insertUiEventPeminjam.id_anggota }?.nama ?: "Pilih Anggota",
            onItemSelected = { selected ->
                val selectedAnggota = anggotaList.find { it.nama == selected }
                selectedAnggota?.let {
                    onPeminjamValueChange(insertUiEventPeminjam.copy(id_anggota = it.id_anggota))
                }
            },
            enabled = enabled
        )

        // Tombol untuk memilih tanggal peminjaman
        Button(
            onClick = { tanggalPeminjamanPicker.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (insertUiEventPeminjam.tanggal_peminjaman.isNotEmpty()) {
                    "Tanggal Peminjaman: ${insertUiEventPeminjam.tanggal_peminjaman}"
                } else {
                    "Pilih Tanggal Peminjaman"
                }
            )
        }

        // Tombol untuk memilih tanggal pengembalian
        Button(
            onClick = { tanggalPengembalianPicker.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (insertUiEventPeminjam.tanggal_pengembalian.isNotEmpty()) {
                    "Tanggal Pengembalian: ${insertUiEventPeminjam.tanggal_pengembalian}"
                } else {
                    "Pilih Tanggal Pengembalian"
                }
            )
        }
        // Validasi pesan error
        if (insertUiEventPeminjam.id_buku == 0) {
            Text("Buku harus dipilih", color = Color.Red)
        }
        if (insertUiEventPeminjam.id_anggota == 0) {
            Text("Anggota harus dipilih", color = Color.Red)
        }
        if (insertUiEventPeminjam.tanggal_peminjaman.isEmpty()) {
            Text("Tanggal peminjaman harus diisi", color = Color.Red)
        }
        if (insertUiEventPeminjam.tanggal_pengembalian.isEmpty()) {
            Text("Tanggal pengembalian harus diisi", color = Color.Red)
        }
    }
}
