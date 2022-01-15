package com.kidmobi.business.workers

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.kidmobi.business.services.RemoteService
import com.kidmobi.business.services.RunningProcessesService
import timber.log.Timber


class RunningProcessesWorker(var context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        Timber.d("RunningProcessesWorker is triggered.")

        try {
            if (!RunningProcessesService.isRunning) {
                Intent(applicationContext, RunningProcessesService::class.java).also {
                    ContextCompat.startForegroundService(context, it)
                }
                println("RunningProcessesService is started.")
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        return Result.retry()
    }
}