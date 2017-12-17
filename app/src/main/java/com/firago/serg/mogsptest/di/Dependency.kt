package com.firago.serg.mogsptest.di

import com.firago.serg.mogsptest.data.NetClient
import com.firago.serg.mogsptest.data.RepositoryAsyncImpl
import com.firago.serg.mogsptest.domain.Repository
import com.firago.serg.mogsptest.net.NetClientImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton



@Module class RepositoryModule{
    @Singleton
    @Provides
    fun repositoryProvider(client : NetClient, @Named("URL") linksPageUrl: String) : Repository {
        return RepositoryAsyncImpl(client, linksPageUrl)
    }
}

@Module class LinksUrlModule{
    @Singleton
    @Provides
    @Named("URL")
    fun linksPageUrlProvider(): String = "http://mogsp.by/"
}
@Module class NetModule{
    @Singleton
    @Provides
    fun clientProvider(): NetClient = NetClientImpl()
}