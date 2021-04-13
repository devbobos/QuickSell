package io.github.devbobos.quicksell

import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.base_toast.view.*

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("")
        showBackButton(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            else -> {
                return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showSnackBar(message: String) {
        val view = getWindow().getDecorView().findViewById<View>(android.R.id.content)
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showBackButton(isShow: Boolean) {
        actionBar?.let {
            it.setDisplayShowHomeEnabled(isShow)
            it.setDisplayHomeAsUpEnabled(isShow)
        }
        supportActionBar?.let {
            it.setDisplayShowHomeEnabled(isShow)
            it.setDisplayHomeAsUpEnabled(isShow)
        }
    }
    fun showToast(message:String){
        showToast(message, -1)
    }

    fun showToast(message:String, imageResourceId:Int){
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        val view = LayoutInflater.from(applicationContext).inflate(R.layout.base_toast, null, false)
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
}