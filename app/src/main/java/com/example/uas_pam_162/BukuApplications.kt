package com.example.uas_pam_162

import android.app.Application
import com.example.uas_pam_162.Container.AppContainer
import com.example.uas_pam_162.Container.MainAppContainer


class BukuApplications:Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container=MainAppContainer()
    }
}