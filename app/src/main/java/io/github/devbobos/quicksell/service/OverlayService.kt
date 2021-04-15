package io.github.devbobos.quicksell.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.github.devbobos.quicksell.Base
import io.github.devbobos.quicksell.Base.context
import io.github.devbobos.quicksell.R
import io.github.devbobos.quicksell.api.UpbitAPIService
import io.github.devbobos.quicksell.helper.utils.Utils
import io.github.devbobos.quicksell.view.home.HomeActivity
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue


class OverlayService : BaseService(){
    val notificationId = 1;
    val notificationChannelId = Base.context.packageName+".notificationChannelId"
    val notificationChannelName = "서비스"
    val requestCodeStop = 2;
    val intentActionStop = "STOP"
    val requestCodeMoveActivity = 3;
    val upbitAPIService = UpbitAPIService()
    val bidButtonTag = "${Base.context.packageName}.tag.bid"
    val askButtonTag = "${Base.context.packageName}.tag.ask"
    var overlayBidButtonView:View? = null
    var overlayAskButtonView:View? = null
    var overlayToastView:View? = null
    val toastQueue:ArrayBlockingQueue<String> = ArrayBlockingQueue<String>(5)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(notificationId, getNotification())

        initBidButton()
        initAskButton()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val intentAction = it.action
            when(intentAction){
                intentActionStop -> {
                    stopForeground(true)
                    stopSelf()
                }
            }
        }
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        upbitAPIService.cancelAllRequest()
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        if(Utils.notNull(wm)) {
            if(Utils.notNull(overlayBidButtonView)) {
                wm.removeView(overlayBidButtonView);
                overlayBidButtonView = null
            }
            if(Utils.notNull(overlayAskButtonView)) {
                wm.removeView(overlayAskButtonView);
                overlayAskButtonView = null
            }
            if(Utils.notNull(overlayToastView)) {
                wm.removeView(overlayToastView);
                overlayToastView = null
            }
        }
    }

    fun initBidButton(){
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val params = WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT)
        if(Build.VERSION.SDK_INT >= 26){
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }

        params.gravity = Gravity.BOTTOM or Gravity.RIGHT
        val bidButtonView = getOverlayBidButtonViewWithInit()
        val bidButton = bidButtonView!!.findViewById<ExtendedFloatingActionButton>(R.id.overlay_extendedFloatingActionButton_bid)
        bidButton.setTag(bidButtonTag)
        bidButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                addCustomToastMessage("onClick")
                showCustomToast()
            }
        })
        bidButton.setOnLongClickListener(object: View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                addCustomToastMessage("onLongClick")
                showCustomToast()
                return true
            }

        })
        windowManager.addView(bidButtonView, params)
    }

    fun initAskButton(){
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val params = WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT)
        if(Build.VERSION.SDK_INT >= 26){
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }

        params.gravity = Gravity.BOTTOM or Gravity.LEFT
        val askButtonView = getOverlayAskButtonViewWithInit()
        val askButton = askButtonView!!.findViewById<ExtendedFloatingActionButton>(R.id.overlay_extendedFloatingActionButton_ask)
        askButton.setTag(askButtonTag)
        askButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                addCustomToastMessage("onClick")
                showCustomToast()
            }
        })
        askButton.setOnLongClickListener(object: View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                addCustomToastMessage("onLongClick")
                showCustomToast()
                return true
            }
        })
        windowManager.addView(askButtonView, params)
    }

    fun getNotification(): Notification{
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= 26){
            val notificationChannel = notificationManager.getNotificationChannel(
                    notificationChannelId
            )
            if (Utils.isNull(notificationChannel)) {
                val channel = NotificationChannel(
                        notificationChannelId,
                        notificationChannelName,
                        NotificationManager.IMPORTANCE_NONE
                )
                notificationManager.createNotificationChannel(channel)
            }
        }
        val builder: NotificationCompat.Builder;
        if(Build.VERSION.SDK_INT >= 26){
            builder = NotificationCompat.Builder(Base.context, notificationChannelId)
        }else{
            builder = NotificationCompat.Builder(Base.context)
        }
        val stopIntent = Intent(context, OverlayService::class.java)
        stopIntent.setAction(intentActionStop)
        val stopPendingIntent = PendingIntent.getService(context, requestCodeStop, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val stopNotificationAction = NotificationCompat.Action(R.drawable.ic_baseline_close_24, "종료하기", stopPendingIntent)
        val moveActivityIntent = Intent(context, HomeActivity::class.java)
        val moveActivityPendingIntent = PendingIntent.getActivity(context, requestCodeMoveActivity, moveActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val moveActivityNotificationAction = NotificationCompat.Action(R.drawable.ic_baseline_exit_to_app_24, "앱실행", moveActivityPendingIntent)
        builder
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(Notification.PRIORITY_MAX)
            .setShowWhen(false)
            .setContentTitle("실행중입니다")
            .addAction(moveActivityNotificationAction)
            .addAction(stopNotificationAction)
        return builder.build()
    }

    fun getOverlayBidButtonViewWithInit(): View?{
        if(Utils.isNull(overlayBidButtonView)){
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            overlayBidButtonView = inflater.inflate(R.layout.overlay_bid_layout, null)
        }
        return overlayBidButtonView
    }

    fun getOverlayAskButtonViewWithInit(): View?{
        if(Utils.isNull(overlayAskButtonView)){
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            overlayAskButtonView = inflater.inflate(R.layout.overlay_ask_layout, null)
        }
        return overlayAskButtonView
    }

    fun addCustomToastMessage(message: String){
        toastQueue.add(message)
    }

    fun showCustomToast(){
        val item = toastQueue.take()
        if(Utils.notNull(item)){
            if(Utils.isNull(overlayToastView)){
                val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
                val params = WindowManager.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        PixelFormat.TRANSLUCENT)
                if(Build.VERSION.SDK_INT >= 26){
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                }

                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                params.windowAnimations = R.style.QuickSell_Toast
                params.y = 200
                overlayToastView = inflater.inflate(R.layout.overlay_toast_layout, null)
                val textView = overlayToastView!!.findViewById<TextView>(R.id.toast_textView_message)
                textView.setText(item)
                windowManager.addView(overlayToastView, params)
                GlobalScope.launch(Dispatchers.Default) {
                    delay(1000L)
                    withContext(Dispatchers.Main) {
                        windowManager.removeView(overlayToastView)
                        overlayToastView = null
//                        showCustomToast()
                    }
                }
            }
        }
    }
}