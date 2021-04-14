package io.github.devbobos.quicksell.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import androidx.core.app.NotificationCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.github.devbobos.quicksell.Base
import io.github.devbobos.quicksell.Base.context
import io.github.devbobos.quicksell.R
import io.github.devbobos.quicksell.api.UpbitAPIService
import io.github.devbobos.quicksell.helper.utils.Utils
import io.github.devbobos.quicksell.view.home.HomeActivity


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
        }
    }

    fun initBidButton(){
        val inflate = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val params = WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT)
        if(Build.VERSION.SDK_INT >= 26){
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }

        params.gravity = Gravity.RIGHT
        params.x = 0
        params.y = 0
        val bidButtonView = getOverlayBidButtonViewWithInit()
        val bidButton = bidButtonView!!.findViewById<ExtendedFloatingActionButton>(R.id.overlay_extendedFloatingActionButton_bid)
        bidButton.setTag(bidButtonTag)
        bidButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                showToast("onClick")
            }
        })
        bidButton.setOnLongClickListener(object: View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                showToast("onLongClick")
                return true
            }

        })
        windowManager.addView(bidButtonView, params)
    }

    fun initAskButton(){
        val inflate = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val params = WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT)
        if(Build.VERSION.SDK_INT >= 26){
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }

        params.gravity = Gravity.LEFT
        params.x = 0
        params.y = 0
        val askButtonView = getOverlayAskButtonViewWithInit()
        val askButton = askButtonView!!.findViewById<ExtendedFloatingActionButton>(R.id.overlay_extendedFloatingActionButton_ask)
        askButton.setTag(askButtonTag)
        askButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                showToast("onClick")
            }
        })
        askButton.setOnLongClickListener(object: View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                showToast("onLongClick")
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
}