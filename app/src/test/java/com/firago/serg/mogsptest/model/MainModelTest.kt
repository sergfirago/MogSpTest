package com.firago.serg.mogsptest.model

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.firago.serg.mogsptest.domain.NewsflashPage
import com.firago.serg.mogsptest.domain.Repository
import com.firago.serg.mogsptest.ui.model.MainModel
import com.firago.serg.mogsptest.ui.model.StateActivity
import com.firago.serg.mogsptest.ui.model.Today
import com.firago.serg.mogsptest.ui.model.ViewData
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.stub
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()
    val today  = object:Today {
        override fun date(): String= "today"
    }
    lateinit var repository: Repository
    lateinit var dataObserver: Observer<ViewData>
    @Before
    fun setUp() {
        repository = mock()

        dataObserver = mock()
    }

    @Test
    fun testStateAfterStart() {

        repository.stub {
            on{ getNewsflashPage()} doReturn Observable.empty<NewsflashPage>()
        }
        val model = MainModel(repository, today)
        model.data.observeForever(dataObserver)
        verify(dataObserver).onChanged(ViewData(null, null, null, StateActivity.LOAD ))
    }

    @Test
    fun testNoTodayNews() {
        val data = NewsflashPage("date", "html")
        repository.stub {
            on{getNewsflashPage()} doReturn Observable.just(data)
        }
        val model = MainModel(repository, today)
        val dataObserver = mock<Observer<ViewData>>()
        model.data.observeForever(dataObserver)
        verify(dataObserver).onChanged(
                ViewData(data.html, "Новость за date", null, StateActivity.OLD))
    }

    @Test
    fun testTodayNews() {
        val data = NewsflashPage("today", "html")
        repository.stub {
            on{getNewsflashPage()} doReturn Observable.just(data)
        }
        val model = MainModel(repository, today)
        val dataObserver = mock<Observer<ViewData>>()
        model.data.observeForever(dataObserver)
        verify(dataObserver).onChanged(
                ViewData(data.html, "Сегодняшняя новость", null, StateActivity.TODAY))
    }
}