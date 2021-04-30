package com.kidmobi.assets.workers

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.assets.services.RemoteSettingsService
import timber.log.Timber


class RemoteSettingsWorker(var context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun doWork(): Result {
        Timber.d("RemoteSettingsWorker is running")

        val intent = Intent(applicationContext, RemoteSettingsService::class.java)

        ContextCompat.startForegroundService(context, intent)

        return Result.success()
    }
}