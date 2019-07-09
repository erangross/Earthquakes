package com.gross.earthquakes


import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date


class EarthquakeAdapter(context: Context, internal var mEarthquakes: List<Earthquake>) : ArrayAdapter<Earthquake>(context, 0, mEarthquakes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val current = mEarthquakes[position]


        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }


        val tvMag = view!!.findViewById(R.id.mag_text_view) as TextView
        tvMag.text = formatMagnitude(current.magnitude)

        val magnitudeCircle = tvMag.background as GradientDrawable
        magnitudeCircle.setColor(getMagnitudeColor(java.lang.Double.valueOf(current.magnitude)))


        val fullLocation = split(current.location)

        val tvPlace = view.findViewById(R.id.place_text_view) as TextView
        tvPlace.text = fullLocation[1]

        val tvOffset = view.findViewById(R.id.offset_text_view) as TextView
        tvOffset.text = fullLocation[0] + " of"

        val date = Date(current.timeInMilliSeconds)

        val tvDate = view.findViewById(R.id.date_text_view) as TextView
        tvDate.text = formatDate(date)

        val tvTime = view.findViewById(R.id.time_text_view) as TextView
        tvTime.text = formatTime(date)


        return view
    }

    fun formatTime(date: Date): String {
        val dateFormat = SimpleDateFormat("h: mm a")
        return dateFormat.format(date)
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("MM DD, yyyy")
        return dateFormat.format(date)
    }

    private fun split(string: String): Array<String> {
        var array = arrayOf<String>()
        if (string.contains("of")) {
            array = string.split("of".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            return array
        } else {
            array = arrayOf("Near", string)
            return array
        }
    }

    private fun getMagnitudeColor(magnitude: Double): Int {

        val magnitudeColorResourceId: Int
        val magnitudeFloor = Math.floor(magnitude).toInt()
        when (magnitudeFloor) {
            0, 1 -> magnitudeColorResourceId = R.color.magnitude1
            2 -> magnitudeColorResourceId = R.color.magnitude2
            3 -> magnitudeColorResourceId = R.color.magnitude3
            4 -> magnitudeColorResourceId = R.color.magnitude4
            5 -> magnitudeColorResourceId = R.color.magnitude5
            6 -> magnitudeColorResourceId = R.color.magnitude6
            7 -> magnitudeColorResourceId = R.color.magnitude7
            8 -> magnitudeColorResourceId = R.color.magnitude8
            9 -> magnitudeColorResourceId = R.color.magnitude9
            else -> magnitudeColorResourceId = R.color.magnitude10plus
        }
        return ContextCompat.getColor(context, magnitudeColorResourceId)
    }

    private fun formatMagnitude(magnitude: Double): String {
        val magnitudeFormat = DecimalFormat("0.0")
        return magnitudeFormat.format(magnitude)
    }
}
