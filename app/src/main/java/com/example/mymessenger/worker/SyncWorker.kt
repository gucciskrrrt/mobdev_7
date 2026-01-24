package com.example.mymessenger.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mymessenger.data.local.AppDatabase
import com.example.mymessenger.data.remote.RetrofitClient
import com.example.mymessenger.data.repository.MessageRepository
import com.example.mymessenger.data.repository.Resource
import com.example.mymessenger.utils.NotificationHelper

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val TAG = "SyncWorker"
        const val WORK_NAME = "sync_messages_work"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting sync work...")

        return try {
            val database = AppDatabase.getDatabase(applicationContext)
            val messageDao = database.messageDao()
            val apiService = RetrofitClient.apiService

            val repository = MessageRepository(messageDao, apiService, applicationContext)

            when (val result = repository.refreshMessages()) {
                is Resource.Success -> {
                    Log.d(TAG, "Sync successful: ${result.data?.size} messages")
                    NotificationHelper.showSyncSuccessNotification(applicationContext)
                    Result.success()
                }
                is Resource.Error -> {
                    Log.e(TAG, "Sync failed: ${result.message}")
                    if (result.data != null && result.data.isNotEmpty()) {
                        Log.d(TAG, "Using cached data: ${result.data.size} messages")
                        Result.success()
                    } else {
                        Result.retry()
                    }
                }
                is Resource.Loading -> {
                    Result.success()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sync error", e)
            Result.retry()
        }
    }
}
