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
import java.lang.NumberFormatException

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
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
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