package com.firago.serg.mogsptest.data

import junit.framework.Assert.*
import org.junit.Test

class ParserKtTest {
    private val html = """<html xmlns="http://www.w3.org/1999/xhtml">
                    <head>
                        <base href="http://mogsp.by">
                    </head>
                    <html>
                    <body>
                    <div class="news">
                        <h5>06.12.2017</h5>
                        <a href="news/meropriyatiya"/>
                    </div>
                    </body>
                    </html>"""
    @Test
    fun testGetNewsflashUrlReturnDate() {
        val (date, _) = getNewsflashUrl(html)
        assertEquals("06.12.2017", date)
    }
    @Test
    fun testGetNewsflashUrlReturnLink() {
        val (_, link) = getNewsflashUrl(html)
        assertEquals("http://mogsp.by/news/meropriyatiya",link)
    }

    @Test(expected = InvalidHtmlContent::class)
    fun testGetNewsflashUrlWithoutDataThrowException() {
        val html = """<html xmlns="http://www.w3.org/1999/xhtml">
                    <head>
                        <base href="http://mogsp.by">
                    </head>
                    <html>
                    <body>
                    <div class="notnews">
                        <h5>06.12.2017</h5>
                       <a href="news/meropriyatiya"/>
                    </div>
                    </body>
                    </html>"""
            getNewsflashUrl(html)
    }
    @Test(expected = InvalidHtmlContent::class)
    fun testGetNewsflashUrlWithoutLinkThrowException() {
        val html = """<html xmlns="http://www.w3.org/1999/xhtml">
                    <head>
                        <base href="http://mogsp.by">
                    </head>
                    <html>
                    <body>
                    <div class="news">
                        <h5>06.12.2017</h5>
                    </div>
                    </body>
                    </html>"""
            getNewsflashUrl(html)
    }

    @Test
    fun testWithoutRefuse() {
        val html = """<html xmlns="http://www.w3.org/1999/xhtml">
                    <head>
                        <title>HEADPART<title>
                    </head>
                    <html>
                    <body>
                    DELETEPART1
                    <article>
                    ARTICLE
                    </article>
                    DELETEPART2
                    </body>
                    </html>"""
        val newHtml = withoutRefuse(html)
        assertTrue(newHtml.contains("ARTICLE"))
        assertTrue(newHtml.contains("HEADPART"))
        assertFalse(newHtml.contains("DELETEPART1"))
        assertFalse(newHtml.contains("DELETEPART2"))
    }

}