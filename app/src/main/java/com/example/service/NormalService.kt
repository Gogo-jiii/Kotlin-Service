package com.example.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class NormalService : Service() {

    private var isDestroyed = false

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Starting service...", Toast.LENGTH_SHORT).show()
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executorService.execute {
            //background task
            try {
                for (i in 0..4) {
                    if (isDestroyed) {
                        break
                    }
                    Log.d("TAG_", i.toString() + " => " + System.currentTimeMillis().toString())
                    showResult(System.currentTimeMillis().toString())
                    Thread.sleep(5000)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            handler.post { //update ui
                showResult("All done")
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun showResult(result: String) {
        val intent = Intent("broadcast")
        intent.putExtra("msg", result)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroyed = true
        Toast.makeText(this, "Stopping service...", Toast.LENGTH_SHORT).show()
    }
}