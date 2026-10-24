
package ai.camlink.live

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class FloatingOverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private var overlay: View? = null

    override fun onCreate() {
        super.onCreate()

        if (!Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }

        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                "camlink_overlay",
                "CamLink Live overlay",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val notification = if (Build.VERSION.SDK_INT >= 26) {
            Notification.Builder(this, "camlink_overlay")
                .setContentTitle("CamLink Live")
                .setContentText("Floating window is active")
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
                .setContentTitle("CamLink Live")
                .setContentText("Floating window is active")
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .build()
        }

        startForeground(7, notification)
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(12, 8, 12, 8)
            setBackgroundColor(Color.rgb(30, 30, 30))
        }

        val title = TextView(this).apply {
            text = "CamLink Live"
            setTextColor(Color.WHITE)
            textSize = 16f
            setPadding(4, 8, 4, 8)
        }
        panel.addView(title)

        val message = TextView(this).apply {
            text = "Overlay active\nLive video integration pending"
            setTextColor(Color.WHITE)
            setPadding(4, 12, 4, 12)
        }
        panel.addView(message)

        val controls = LinearLayout(this)
        val resize = Button(this).apply {
            text = "Resize"
            setOnClickListener {
                val params = overlay?.layoutParams as? WindowManager.LayoutParams
                params?.let {
                    it.width = if (it.width < 350) 350 else 240
                    it.height = if (it.height < 280) 280 else 180
                    overlay?.let { view -> windowManager.updateViewLayout(view, it) }
                }
            }
        }
        val close = Button(this).apply {
            text = "Close"
            setOnClickListener { stopSelf() }
        }
        controls.addView(resize)
        controls.addView(close)
        panel.addView(controls)

        val params = WindowManager.LayoutParams(
            280,
            180,
            if (Build.VERSION.SDK_INT >= 26)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 40
            y = 180
        }

        title.setOnTouchListener(object : View.OnTouchListener {
            private var startX = 0
            private var startY = 0
            private var touchX = 0f
            private var touchY = 0f

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = params.x
                        startY = params.y
                        touchX = event.rawX
                        touchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = startX + (event.rawX - touchX).toInt()
                        params.y = startY + (event.rawY - touchY).toInt()
                        overlay?.let {
                            windowManager.updateViewLayout(it, params)
                        }
                        return true
                    }
                }
                return false
            }
        })

        overlay = panel
        windowManager.addView(panel, params)
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int = START_STICKY

    override fun onDestroy() {
        overlay?.let {
            if (it.isAttachedToWindow) windowManager.removeView(it)
        }
        overlay = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
