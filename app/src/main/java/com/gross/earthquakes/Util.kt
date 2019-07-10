package com.gross.earthquakes

import com.fasterxml.jackson.databind.ObjectMapper

class Util {

    //parse the raw data and return an Array
    fun parseJson(data : String) : ArrayList<Earthquake> {

        val earthquakes = ArrayList<Earthquake>()
        val objectMapper = ObjectMapper()

         val node =   objectMapper.readTree(data)

        val magnitudes = node.findValues("mag")
        val places = node.findValues("place")
        val times = node.findValues("time")
        val urls = node.findValues("url")

        for (index in 0..magnitudes.size -1){

            earthquakes.add(index,
                Earthquake(magnitudes.get(index).asDouble(),
                    places.get(index).asText(),urls.get(index +1).asText().plus("/map"),
                    times.get(index).asLong()))
        }
        return  earthquakes
    }
}