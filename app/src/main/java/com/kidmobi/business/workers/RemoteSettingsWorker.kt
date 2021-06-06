package com.kidmobi.business.workers

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.business.services.RemoteService
import timber.log.Timber


class RemoteSettingsWorker(var context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun doWork(): Result {
        Timber.d("RemoteSettingsWorker is triggered.")
        println("RemoteSettingsWorker is triggered. RemoteService is running: ${RemoteService.isRunning}")

        try {
            if (!RemoteService.isRunning) {
                Intent(applicationContext, RemoteService::class.java).also {
                    ContextCompat.startForegroundService(context, it)
                }
                Timber.d("RemoteService is started via Worker")
                println("RemoteService is started via Worker")
            } else {
                Timber.d("RemoteService is already running.")
                println("RemoteService is already running.")
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        return Result.retry()
    }
}