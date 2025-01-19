package com.example.uas_pam_162.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uas_pam_162.ui.view.buku.DestinasiBukuEntry
import com.example.uas_pam_162.ui.view.buku.DestinasiHomeBuku
import com.example.uas_pam_162.ui.view.buku.EntryBukuScreen
import com.example.uas_pam_162.ui.view.buku.HomeScreenBuku

@Composable
fun PengelolaHalaman(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = DestinasiHomeBuku.route,
        modifier = modifier
    ){
        composable(DestinasiHomeBuku.route){
            HomeScreenBuku(
                navigateToItemEntry = {navController.navigate(DestinasiBukuEntry.route)},

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

    }
    }
