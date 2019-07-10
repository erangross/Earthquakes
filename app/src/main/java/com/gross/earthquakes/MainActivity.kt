package com.gross.earthquakes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

     val liveData =  MutableLiveData<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Fetching data from internet
        fetchDatafromInternet()

        liveData.observeForever { data ->
            // Update the UI

            val util = Util()
            util.parseJson(data)


        }

    }

    fun fetchDatafromInternet(){

        // Instantiate the cache
        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

// Instantiate the RequestQueue with the cache and network. Start the queue.
        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        val url = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson"

// Formulate the request and handle the response.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                liveData.postValue(response)
                println(response)
            },
            Response.ErrorListener { error ->
                // Handle error
                println(error.toString())
            })

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest)

    }
}
