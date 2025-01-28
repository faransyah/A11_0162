package com.example.uas_pam_162.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uas_pam_162.ui.view.DestinasiHomeAwal
import com.example.uas_pam_162.ui.view.HomeMulai
import com.example.uas_pam_162.ui.view.anggota.DestinasiDetailAnggota
import com.example.uas_pam_162.ui.view.anggota.DestinasiEntryAnggota
import com.example.uas_pam_162.ui.view.anggota.DestinasiHomeAnggota
import com.example.uas_pam_162.ui.view.anggota.DestinasiUpdateAnggota
import com.example.uas_pam_162.ui.view.anggota.DetailAgtView
import com.example.uas_pam_162.ui.view.anggota.EntryAnggotatScreen
import com.example.uas_pam_162.ui.view.anggota.HomeScreenAnggota
import com.example.uas_pam_162.ui.view.anggota.UpdateAnggotaView
import com.example.uas_pam_162.ui.view.buku.DestinasiBukuEntry
import com.example.uas_pam_162.ui.view.buku.DestinasiDetailBuku
import com.example.uas_pam_162.ui.view.buku.DestinasiHomeBuku
import com.example.uas_pam_162.ui.view.buku.DestinasiUpdateBuku
import com.example.uas_pam_162.ui.view.buku.DetailBukuView
import com.example.uas_pam_162.ui.view.buku.EntryBukuScreen
import com.example.uas_pam_162.ui.view.buku.HomeScreenBuku
import com.example.uas_pam_162.ui.view.buku.UpdateViewBuku
import com.example.uas_pam_162.ui.view.peminjaman.DestinasiDetailPeminjaman
import com.example.uas_pam_162.ui.view.peminjaman.DestinasiHomePeminjaman
import com.example.uas_pam_162.ui.view.peminjaman.DestinasiPjmEntry
import com.example.uas_pam_162.ui.view.peminjaman.DestinasiUpdatePeminjaman
import com.example.uas_pam_162.ui.view.peminjaman.DetailPeminjamanView
import com.example.uas_pam_162.ui.view.peminjaman.EntryPjmScreen
import com.example.uas_pam_162.ui.view.peminjaman.HomeScreenPeminjaman
import com.example.uas_pam_162.ui.view.peminjaman.UpdatePeminjamanView
import com.example.uas_pam_162.ui.view.pengembalian.DestinasiDetailPengembalian
import com.example.uas_pam_162.ui.view.pengembalian.DestinasiHomePengembalian
import com.example.uas_pam_162.ui.view.pengembalian.DetailPengembalianView
import com.example.uas_pam_162.ui.view.pengembalian.HomeScreenPengembalian

@Composable
fun PengelolaHalaman(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = DestinasiHomeAwal.route,
        modifier = modifier
    ){

        composable(
            route = DestinasiHomeAwal.route

        ){
            HomeMulai(
                onBkButton = {
                    navController.navigate(DestinasiHomeBuku.route)
                },
                onAgButton = {
                    navController.navigate(DestinasiHomeAnggota.route)
                },
                onPmButton = {
                    navController.navigate(DestinasiHomePeminjaman.route)
                },
                onPkButton = {
                    navController.navigate(DestinasiHomePengembalian.route)
                }
            )
        }
        composable(DestinasiHomeBuku.route){
            HomeScreenBuku(
                navigateToItemEntry = {navController.navigate(DestinasiBukuEntry.route)},
                navigateBack = {
                    navController.navigate(DestinasiHomeAwal.route){
                        popUpTo(DestinasiHomeAwal.route){
                            inclusive = true
                        }
                    }
                },
                onDetailClick = {id ->
                    navController.navigate("${DestinasiDetailBuku.route}/$id"){
                        popUpTo(DestinasiHomeBuku.route){
                            inclusive = true
                        }
                    }

                }

            )
        }
        composable(DestinasiBukuEntry.route){
            EntryBukuScreen(
                navigateBack = {
                    navController.navigate(DestinasiHomeBuku.route){
                        popUpTo(DestinasiHomeBuku.route){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(DestinasiDetailBuku.routesWithArg){backStackEntry ->
            val id =backStackEntry.arguments?.getString(DestinasiDetailBuku.IDBUKU)

            id?.let {
                DetailBukuView(
                    id_buku = it,
                    navigateBack = {
                        navController.navigate(DestinasiHomeBuku.route){
                            popUpTo(DestinasiHomeBuku.route){
                                inclusive = true
                            }
                        }
                    },
                    OnEditClick = {
                        navController.navigate("${DestinasiUpdateBuku.route}/$it")
                    }


                )
            }
        }
        composable(DestinasiUpdateBuku.routesWithArg,
            arguments = listOf(
                navArgument(DestinasiUpdateBuku.IDBUKU){
                    type = NavType.StringType
                }
            )){ backStackEntry ->
            val idBuku = backStackEntry.arguments?.getString(DestinasiUpdateBuku.IDBUKU)
            idBuku?.let {
                UpdateViewBuku(
                    navigateBack = {
                        navController.popBackStack()
                    },
                    modifier = modifier
                )
            }
        }
        composable(DestinasiHomeAnggota.route){
            HomeScreenAnggota(
                navigateToItemEntry = {navController.navigate(DestinasiEntryAnggota.route)},
                onDetailClick = {
                        id ->
                    navController.navigate("${DestinasiDetailAnggota.route}/$id"){
                        popUpTo(DestinasiHomeBuku.route){
                            inclusive = true
                        }
                    }
                    println("PengelolaHalaman: id = $id")

                }

            )
        }


        composable(DestinasiEntryAnggota.route){
            EntryAnggotatScreen(
                navigateBackAgt = {
                    navController.navigate(DestinasiHomeAnggota.route){
                        popUpTo(DestinasiHomeAnggota.route){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(DestinasiDetailAnggota.routesWithArg){backStackEntry ->
            val idAnggota = backStackEntry.arguments?.getString(DestinasiDetailAnggota.IDAGT)


            idAnggota?.let {
                DetailAgtView(
                    id_anggota = it,
                    navigateBack = {
                        navController.navigate(DestinasiHomeAnggota.route){
                            popUpTo(DestinasiHomeAnggota.route){
                                inclusive = true
                            }
                        }
                    },
                    OnEditClick = {
                        navController.navigate("${DestinasiUpdateAnggota.route}/$it")
                    }
                ) 
            }
        }
        composable(DestinasiUpdateAnggota.routesWithArg,
            arguments = listOf(
                navArgument(DestinasiUpdateAnggota.IDANGGOTA){
                    type = NavType.StringType
                }
            )){backStackEntry ->
            val idAnggota = backStackEntry.arguments?.getString(DestinasiUpdateAnggota.IDANGGOTA)
            idAnggota?.let {
                UpdateAnggotaView(
                    navigateBack = {
                        navController.popBackStack()
                    },
                    modifier = modifier
                )
            }
        }


        composable(DestinasiPjmEntry.route){
            EntryPjmScreen(
                navigateBack = {
                    navController.navigate(DestinasiHomePeminjaman.route){
                        popUpTo(DestinasiHomePeminjaman.route){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(DestinasiHomePeminjaman.route){
            HomeScreenPeminjaman(
                navigateToItemEntry = {navController.navigate(DestinasiPjmEntry.route)},
                navigateBack = {
                    navController.navigate(DestinasiHomeAwal.route){
                        popUpTo(DestinasiHomeAwal.route){
                            inclusive = true
                        }
                    }
                },
                onDetailClick = {
                        id ->
                    navController.navigate("${DestinasiDetailPeminjaman.route}/$id"){
                        popUpTo(DestinasiHomePeminjaman.route){
                            inclusive = true
                        }
                    }
                    println("PengelolaHalaman: id = $id")
                }
            )
        }
        composable(DestinasiHomePengembalian.route){
            HomeScreenPengembalian(
                navigateToItemEntry = {navController.navigate(DestinasiHomePengembalian.route)},
                onDetailClick = {
                    id ->
                    navController.navigate("${DestinasiDetailPengembalian.route}/$id"){
                        popUpTo(DestinasiHomePengembalian.route){
                            inclusive = true
                        }
                    }
                    println("PengelolaHalaman: id = $id")
                }

            )
        }
        composable(DestinasiDetailPeminjaman.routesWithArg){backStackEntry ->
            val idPeminjaman = backStackEntry.arguments?.getString(DestinasiDetailPeminjaman.IDPJM)

            idPeminjaman?.let {
                DetailPeminjamanView(
                    idPeminjaman = it,
                    navigateBack = {
                        navController.navigate(DestinasiHomePeminjaman.route){
                            popUpTo(DestinasiHomePeminjaman.route){
                                inclusive = true
                            }
                        }
                    },
                    OnEditClick = {
                        navController.navigate("${DestinasiUpdatePeminjaman.route}/$it")

                    }


                    )
            }
        }
        composable(DestinasiUpdatePeminjaman.routesWithArg,
            arguments = listOf(
                navArgument(DestinasiUpdatePeminjaman.IDPEMINJAMAN){
                    type = NavType.StringType
                }
            )){backStackEntry ->
            val idPeminjaman = backStackEntry.arguments?.getString(DestinasiUpdatePeminjaman.IDPEMINJAMAN)
            idPeminjaman?.let {
                UpdatePeminjamanView(
                    navigateBack = {
                        navController.popBackStack()
                    },
                    modifier = modifier
                )
            }
        }
        composable(DestinasiDetailPengembalian.routesWithArg) { backStackEntry ->
            val idPengembalian = backStackEntry.arguments?.getString(DestinasiDetailPengembalian.IDPNGN)

            idPengembalian?.let {
                DetailPengembalianView(
                    idPengembalian = it, // Sesuaikan dengan parameter di DetailPengembalianView
                    navigateBack = {
                        navController.navigate(DestinasiHomePengembalian.route) {
                            popUpTo(DestinasiHomePengembalian.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }


    }
    }
