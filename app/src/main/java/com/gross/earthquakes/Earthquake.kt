package com.gross.earthquakes

class Earthquake(val magnitude: Double, val location: String, val url: String, private val mTimeInMilliSeconds: Long?) {

    val timeInMilliSeconds: Long
        get() = mTimeInMilliSeconds!!
}

