package com.ahladeveloper.showingnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIF_ID = 0

    private val mHandler = android.os.Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            val text = findViewById<EditText>(R.id.plain_text_input)
            val second = findViewById<EditText>(R.id.plain_second_input)

            // Panggil method untuk menampilkan notifikasi secara berkala
            second.text.toString().toIntOrNull()
                ?.let { it1 -> startRepeatingNotification(text.text.toString(), it1) }
        }
    }
    private fun startRepeatingNotification(text: String, second:Int=5) {
        mHandler.postDelayed({
            cancelNotification()
            showNotification(text)
            // Memanggil method ini kembali setelah jeda waktu tertentu
            startRepeatingNotification(text, second)
        }, second.toLong()) // Mengatur interval waktu notifikasi (dalam milidetik), contoh: 5000ms = 5 detik
    }
    private fun showNotification(text: String) {
        val channelId = "default_channel_id"
        val channelName = "Default Channel"
        val description = "This is the default channel for notifications"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Membuat notification channel (Hanya diperlukan untuk Android Oreo ke atas)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }

        // Intent untuk menavigasi ke activity tertentu saat notifikasi diklik
        val resultIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Membuat notifikasi
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_announcement_24) // Icon notifikasi
            .setContentTitle("Pengingat") // Judul notifikasi
            .setContentText(text) // Isi notifikasi
            .setContentIntent(pendingIntent) // Intent yang akan dijalankan saat notifikasi diklik
            .setAutoCancel(true) // Menghapus notifikasi saat diklik

        // Menampilkan notifikasi
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun cancelNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)
    }
}