package io.github.devbobos.quicksell.view.findmarket

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.devbobos.quicksell.BaseActivity
import io.github.devbobos.quicksell.R
import io.github.devbobos.quicksell.api.UpbitAPIService
import io.github.devbobos.quicksell.api.models.MarketInfo
import kotlinx.android.synthetic.main.findmarket_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindMarketActivity: BaseActivity(), View.OnClickListener{
    lateinit var findMarketAdapter: FindMarketAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.findmarket_activity)
        findMarket_recyclerView.layoutManager = LinearLayoutManager(baseContext)
    }

    override fun onResume() {
        super.onResume()
        findMarket_progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val service = UpbitAPIService()
            val list = listOf<MarketInfo>()
            findMarketAdapter = FindMarketAdapter(this@FindMarketActivity, list)
            findMarket_recyclerView.adapter = findMarketAdapter
            withContext(Dispatchers.Main){
                findMarket_progressBar.visibility = View.GONE
                findMarketAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.findMarket_button_submit ->{
                findMarket_progressBar.visibility = View.VISIBLE
                if(findMarket_button_submit.isEnabled){
                    GlobalScope.launch(Dispatchers.IO) {
                        val service = UpbitAPIService()
                        withContext(Dispatchers.Main){
                            findMarket_progressBar.visibility = View.GONE
                            onBackPressed()
                        }
                    }
                } else{
                    showToast("코인을 선택해주세요")
                }
            }
        }
    }

    fun changeSubmitButtonStateChange(enable: Boolean){
        findMarket_button_submit.isEnabled = enable
    }
}