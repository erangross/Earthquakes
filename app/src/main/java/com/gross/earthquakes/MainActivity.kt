package com.gross.earthquakes

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Cache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

     private val liveData =  MutableLiveData<String>()
     private val TAG = "MainActivity"
    lateinit private var serviceComponent: ComponentName
    private var jobId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Adding refresh Swipe
        var mySwipeRefreshLayout = SwipeRefreshLayout(this)
        mySwipeRefreshLayout = findViewById(R.id.refreshLayout)
        serviceComponent = ComponentName(this, MyJobService::class.java)


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
                //Schedual job will run in the background even if the app closed
                 scheduleJob()

    }

    private fun fetchDatafromInternet(){
        // Instantiate the cache
        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

// Instantiate the RequestQueue with the cache and network. Start the queue.
        val requestQueue = RequestQueue(cache as Cache?, network).apply {
            start()
        }


        val url = getString(R.string.url)

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


    private fun scheduleJob() {
        val builder = JobInfo.Builder(jobId++, serviceComponent)
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            builder.setPeriodic(TimeUnit.MINUTES.toMillis(1))

        // Extras, work duration.
        val extras = PersistableBundle()
        var workDuration = "1"
        extras.putLong(WORK_DURATION_KEY, workDuration.toLong() * TimeUnit.SECONDS.toMillis(1))
        // Finish configuring the builder
        builder.run {
            setExtras(extras)
        }

        // Schedule job
        Log.d(TAG, "Scheduling job")
        (getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler).schedule(builder.build())
    }

}
