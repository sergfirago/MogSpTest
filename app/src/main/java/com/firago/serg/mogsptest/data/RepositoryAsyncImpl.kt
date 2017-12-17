package com.firago.serg.mogsptest.data

import com.firago.serg.mogsptest.domain.NewsflashPage
import com.firago.serg.mogsptest.domain.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Repository Async Impl
 *
 *
 * subscribe on  Schedulers.io()
 *
 * observe on AndroidSchedulers.mainThread()
 * @param client  NetClient
 * @param linkPageUrl  url to start page. (page containing links to news)
 *
 */
class RepositoryAsyncImpl(private val client: NetClient, private val linkPageUrl: String): Repository {

    override fun getNewsflashPage(): Observable<NewsflashPage> {

        return Observable
                .fromCallable { client.get(linkPageUrl)}
                .map { page -> getNewsflashUrl(page) }
                .map { newsflashUrl -> NewsflashPage(newsflashUrl.date, client.get(newsflashUrl.url)) }
                .map { newsflashPage -> newsflashPage.copy(html = withoutRefuse(newsflashPage.html)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
data class NewsflashUrl(val date: String, val url: String)