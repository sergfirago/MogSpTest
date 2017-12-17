package com.firago.serg.mogsptest.data

interface NetClient {
    fun get(url :String) :String
}