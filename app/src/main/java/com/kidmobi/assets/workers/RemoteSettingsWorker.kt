package com.kidmobi.assets.workers

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.assets.services.RemoteService
import timber.log.Timber
import java.util.*


class RemoteSettingsWorker(var context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun doWork(): Result {
        val calendar = Calendar.getInstance()
        Timber.d("RemoteSettingsWorker is triggered: ${calendar.time}")

        Intent(applicationContext, RemoteService::class.java).also {
            context.startService(it)
        }


        return Result.retry()
    }
}