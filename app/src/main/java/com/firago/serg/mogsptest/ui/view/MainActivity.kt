package com.firago.serg.mogsptest.ui.view

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.TextView
import com.firago.serg.mogsptest.App
import com.firago.serg.mogsptest.R
import com.firago.serg.mogsptest.domain.Repository
import com.firago.serg.mogsptest.ui.model.*
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    val LOG_TAG = "MainActivity"

    private lateinit var model: MainModel
    private val textErrorMessage: TextView by lazy{ findViewById<TextView>(R.id.text_error_message)}
    private val webView: WebView by lazy { findViewById<WebView>(R.id.web_view) }
    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    val swipe : SwipeRefreshLayout by lazy {findViewById<SwipeRefreshLayout>(R.id.swipe)}

    @Inject
    lateinit var repository: Repository

    val today = object: Today{
        override fun date(): String = DateFormat.format("dd.MM.yyyy", Date()).toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.inject(this)

        val factory = MainModelFactory(repository, today)
        model = ViewModelProviders.of(this, factory).get(MainModel::class.java)

        swipe.setOnRefreshListener {
            setLoadingState()
            model.refresh()
        }

//        if (hasConnection()) {
        model.data.observe({lifecycle}){

            data -> when(data?.state){
                StateActivity.LOAD ->setLoadingState()
                StateActivity.ERROR -> {
                    setErrorState()
                    textErrorMessage.setText(data.errorMessage)
                    title = data.title
                }
                StateActivity.OLD_NEWS -> {
                    if (!model.warningAlreadyShown) {
                        showWarningOldNews()
                        model.warningAlreadyShown = true
                    }
                    showNews(data)
                }
                StateActivity.TODAY_NEWS ->{
                    showNews(data)
                }
            }
            swipe.isRefreshing = false
        }

    }

    private fun showNews(data: ViewData) {
        setNormalState()
        webView.loadData(data.htmlArticle, "text/html; charset=UTF-8", null)
        title = data.title
        Log.d(LOG_TAG, data.htmlArticle)
    }

    private fun showWarningOldNews() {
        val fragment = supportFragmentManager.findFragmentByTag("no news")
        if (fragment == null) {
            NewsDialog().show(supportFragmentManager, "no news")
        }
    }

    private fun setLoadingState() {
        textErrorMessage.GONE()
        webView.GONE()
        swipe.isRefreshing = false
        progressBar.VISIBLE()
    }

    private fun setErrorState() {
        textErrorMessage.VISIBLE()
        webView.GONE()
        swipe.isRefreshing = false
        progressBar.GONE()
    }

    private fun setNormalState() {
        textErrorMessage.GONE()
        webView.VISIBLE()
        swipe.isRefreshing = false
        progressBar.GONE()

    }
}
fun View.GONE(){
    this.visibility = View.GONE
}
fun View.VISIBLE(){
    this.visibility = View.VISIBLE
}
fun Context.hasConnection():Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo ?: return false
    return networkInfo.isConnectedOrConnecting
}

