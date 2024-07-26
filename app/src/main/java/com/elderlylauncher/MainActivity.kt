package com.elderlylauncher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity(), SensorEventListener {

    private lateinit var timeTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var heartRateTextView: TextView
    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeTextView = findViewById(R.id.timeTextView)
        dateTextView = findViewById(R.id.dateTextView)
        heartRateTextView = findViewById(R.id.heartRateTextView)

        // Setup time and date
        updateTimeAndDate()

        // Setup sensor manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        // Initialize gesture detector
        gestureDetector = GestureDetector(this, GestureListener())
    }

    override fun onResume() {
        super.onResume()
        heartRateSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private fun updateTimeAndDate() {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentTime = timeFormat.format(Date())
        val currentDate = dateFormat.format(Date())

        timeTextView.text = currentTime
        dateTextView.text = currentDate
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_HEART_RATE) {
            val heartRate = event.values[0]
            heartRateTextView.text = heartRate.toInt().toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if necessary
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            e1 ?: return false
            e2 ?: return false

            // Swipe up
            if (e1.y - e2.y > 100) {
                openSettings()
                return true
            }

            // Swipe down
            if (e2.y - e1.y > 100) {
                openTodoList()
                return true
            }
            return false
        }
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun openTodoList() {
        val intent = Intent(this, TodoListActivity::class.java)
        startActivity(intent)
    }
}

