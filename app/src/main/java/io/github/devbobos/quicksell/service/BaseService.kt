package io.github.devbobos.quicksell.service

import android.app.Service
import android.content.Intent
import android.content.res.Resources
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import io.github.devbobos.quicksell.R
import kotlinx.android.synthetic.main.base_toast.view.*
import java.lang.NumberFormatException

open class BaseService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    fun showToast(message:String){
        showToast(message, -1)
    }

    fun showToast(message:String, imageResourceId:Int){
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.base_toast, null, false)
        with(view){
            toast_textView_message.setText(message)
            if(imageResourceId > 0){
                toast_imageView.visibility = View.VISIBLE
                toast_imageView.setImageResource(imageResourceId)
            } else{
                toast_imageView.visibility = View.GONE
            }
        }
        toast.view = view
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER,0, 16.dp)
        toast.show()
    }

    //extensions
    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
    val String.int: Int
        get() = try {
            this.toInt()
        } catch (e: NumberFormatException){
            0
        }
    val String.float: Float
        get() = try {
            this.toFloat()
        } catch (e: NumberFormatException){
            0f
        }
    val String.long: Long
        get() = try {
            this.toLong()
        } catch (e: NumberFormatException){
            0
        }
}