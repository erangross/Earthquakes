package com.gross.earthquakes

import java.util.*

class Earthquake (magnitude: Double, location: String, date : Date, time: Long){

    var mMagnitude = magnitude
    var mLocation = location
    var mDate = date
    var mTime = time


    fun getMagnitude() : Double {

        return mMagnitude
    }

    fun getLocation() : String {
        return mLocation
    }

    fun getDate() : Date {
        return mDate
    }

    fun getTime() : Long {

        return mTime
    }

    fun setMagnitude(magnitude: Double){
        mMagnitude = magnitude
    }

    fun setLocation(location: String){
        mLocation = location
    }

    fun setDate(date: Date){
        mDate = date
    }

    fun setTime(time: Long){
        mTime = time
    }

}