package com.firago.serg.mogsptest

import android.app.Application
import com.firago.serg.mogsptest.di.Component
import com.firago.serg.mogsptest.di.DaggerComponent

class App: Application() {
    companion object {
        lateinit var component : Component
        private set
    }
    override fun onCreate() {
        super.onCreate()
        component = DaggerComponent.builder().build()
    }
}