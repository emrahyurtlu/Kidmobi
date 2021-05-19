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



        Intent(applicationContext, RemoteService::class.java).also {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(it)
            else
                context.startService(it)*/

            ContextCompat.startForegroundService(context, it)
        }


        return Result.retry()
    }
}