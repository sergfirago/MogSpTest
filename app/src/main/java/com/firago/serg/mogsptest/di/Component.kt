package com.firago.serg.mogsptest.di

import com.firago.serg.mogsptest.ui.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetModule::class), (RepositoryModule::class), (LinksUrlModule::class)])
interface Component {
    fun inject(activity: MainActivity)
}