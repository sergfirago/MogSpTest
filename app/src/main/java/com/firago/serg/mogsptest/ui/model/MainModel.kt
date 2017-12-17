package com.firago.serg.mogsptest.ui.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.firago.serg.mogsptest.domain.NewsflashPage
import com.firago.serg.mogsptest.domain.Repository
import io.reactivex.disposables.Disposable

enum class StateActivity{
    OLD, ERROR, LOAD, TODAY
}

interface Today {
    fun date(): String
}


data class ViewData(val htmlArticle: String?,
                    val title: String?,
                    val errorMessage: String?,
                    val state: StateActivity)

class MainModel(val repository: Repository, private val today: Today): ViewModel() {
    val data = MutableLiveData<ViewData>()
    private var disposable: Disposable? = null
    init {
        refresh()
    }

    fun todayNews(htmlResponse: NewsflashPage) = htmlResponse.date == today.date()
    private fun title(htmlResponse: NewsflashPage): String {
        return if (todayNews(htmlResponse)) "Сегодняшняя новость"
        else "Новость за ${htmlResponse.date}"
    }



    fun refresh() {
        data.value = ViewData(null, null, null, StateActivity.LOAD)
        disposable?.dispose()
        disposable = repository.getNewsflashPage()
                .subscribe({ htmlResponse ->
                    val state = if (todayNews(htmlResponse)) StateActivity.TODAY else StateActivity.OLD
                    data.value = ViewData(
                            htmlArticle = htmlResponse.html,
                            title = title(htmlResponse),
                            state = state,
                            errorMessage = null)
                }
                        , { t ->
                    data.value = ViewData(
                            errorMessage = "Error: $t",
                            state = StateActivity.ERROR,
                            htmlArticle = null,
                            title = "Error")
                })
        warningAlreadyShown = false
    }

    var warningAlreadyShown: Boolean = false
}

//fun today():String = DateFormat.format("dd.MM.yyyy", Date()).toString()
