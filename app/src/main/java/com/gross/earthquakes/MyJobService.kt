/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gross.earthquakes

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Handler
import android.os.Messenger
import android.util.Log

/**
 * Service to handle callbacks from the JobScheduler. Requests scheduled with the JobScheduler
 * ultimately land on this service's "onStartJob" method. It runs jobs for a specific amount of time
 * and finishes them. It keeps the activity updated with changes via a Messenger.
 */
class MyJobService : JobService() {

    private var activityMessenger: Messenger? = null

    /**
     * When the app's MainActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCallback()"
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        activityMessenger = intent.getParcelableExtra(MESSENGER_INTENT_KEY)
        return Service.START_STICKY
    }

    override fun onStartJob(params: JobParameters): Boolean {
        // The work that this service "does" is simply wait for a certain duration and finish
        // the job (on another thread).

        // Uses a Handler to delay the execution of jobFinished().
        val duration = params.extras.getLong(WORK_DURATION_KEY)
        Handler().postDelayed({
            downloadingFromInternet()
            jobFinished(params, false)
        }, duration)
        Log.i(TAG, "on start job: ${params.jobId}")

        // Return true as there's more work to be done with this job.
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        // Stop tracking these job parameters, as we've 'finished' executing.
        Log.i(TAG, "on stop job: ${params.jobId}")

        // Return false to drop the job.
        return false
    }



    companion object {
        private const val TAG = "MyJobService"
    }

    private fun downloadingFromInternet(){

        Log.d(TAG,"Service Test")
    }
}
