package com.firago.serg.mogsptest.domain

import io.reactivex.Observable

interface Repository {
    fun getNewsflashPage(): Observable<NewsflashPage>
}