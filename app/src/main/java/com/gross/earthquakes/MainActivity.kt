package com.gross.earthquakes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest


class MainActivity : AppCompatActivity() {

     private val liveData =  MutableLiveData<String>()
     private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Adding refresh Swipe
        var mySwipeRefreshLayout = SwipeRefreshLayout(this)
        mySwipeRefreshLayout = findViewById(R.id.refreshLayout)

        /*
        * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
        * performs a swipe-to-refresh gesture.
        */
        mySwipeRefreshLayout.setOnRefreshListener{
            Log.i(TAG, "onRefresh called from SwipeRefreshLayout")
            //Check if new data available if it is download it and present it to the user
            fetchDatafromInternet()
            //Clearning the indicator after fetch new data from the internet
            mySwipeRefreshLayout.isRefreshing = false
        }
        //Fetching data from internet
        fetchDatafromInternet()

        liveData.observeForever { data ->

            // Update the UI
            updateUI(data)

        }

    }

    private fun fetchDatafromInternet(){
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
                requestQueue.stop()
                var progressBar = ProgressBar(this)
                progressBar = findViewById(R.id.progressBar)
                progressBar.visibility = View.GONE

            },
            Response.ErrorListener { error ->
                // Handle error
                println(error.toString())
            })

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest)

    }


    private fun updateUI(data : String){

        val util = Util()
        //Getting array with all the earthquakes data
        val arrayEarthquakes = util.parseJson(data)
        val  earthquakeAdapter = EarthquakeAdapter(this, arrayEarthquakes as List<Earthquake>)

        val  earthquakeListView = findViewById<ListView>(R.id.list)

        earthquakeListView.adapter = earthquakeAdapter
    }



}
