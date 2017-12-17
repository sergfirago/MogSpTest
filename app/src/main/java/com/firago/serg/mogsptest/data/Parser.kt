package com.firago.serg.mogsptest.data


import org.jsoup.Jsoup

/**
 *  Return NewsflashUrl (date, link)
 *  <p>
 *
 *  date - date of newsflash
 *
 *  link - link to newsflash page
 *  @param html text
 *  @return NewflashUrl to latest news
 *  @throws InvalidHtmlContent
 */
fun getNewsflashUrl(firstPage: String): NewsflashUrl {
    try {
        val newsElem = Jsoup.parse(firstPage).select("div.news")[0]
        val date = newsElem.selectFirst("h5").text()
        val url = newsElem.selectFirst("a[href]").attr("abs:href")
        return NewsflashUrl(date, url)
    }catch (e :Exception){
        throw InvalidHtmlContent().initCause(e)
    }
}

/**
 * Return html page with only newsflash
 * @param raw html page text
 * @return html
 * @throws InvalidHtmlContent
 */
fun withoutRefuse(html: String): String {

    val document = Jsoup.parse(html)
    val article = document.select("article").html()
    val head = document.select("head").html()
    if (article.isEmpty()) throw InvalidHtmlContent()

    return buildHtmlPage(head, article)
}

private fun buildHtmlPage(head: String, article: String):String{
    val pageTemplate ="""
        |<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        |"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        |<html xmlns="http://www.w3.org/1999/xhtml">
        |   $head
        |   <body class=\"home\">
        |       $article
        |   </body>
        |</html>""".trimMargin()
    return pageTemplate
}

class InvalidHtmlContent : Throwable()