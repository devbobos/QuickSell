package io.github.devbobos.quicksell.view.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.orhanobut.logger.Logger
import io.github.devbobos.quicksell.ApplicationCache
import io.github.devbobos.quicksell.BaseActivity
import io.github.devbobos.quicksell.R
import io.github.devbobos.quicksell.api.UpbitAPIService
import io.github.devbobos.quicksell.helper.utils.Utils
import io.github.devbobos.quicksell.service.OverlayViewService
import io.github.devbobos.quicksell.view.SettingActivity
import io.github.devbobos.quicksell.view.findmarket.FindMarketActivity
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity: BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        home_button_setting.setOnClickListener(this)
        home_button_selectMarket.setOnClickListener(this)
        home_button_start.setOnClickListener(this)
        initAccountChart()
    }

    override fun onResume() {
        super.onResume()
        home_progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val service = UpbitAPIService()

            withContext(Dispatchers.Main){
                home_progressBar.visibility = View.GONE
                if(Utils.notNull(ApplicationCache.getInstance().getSelectedMarket())){
                    home_button_selectMarket.setText("변경하기")
                    updateSelectedMarketInfo()
                } else{
                    home_button_selectMarket.setText("선택하기")
                }
            }
        }
        updateAccountChart()
        home_horizontalBarChart_account.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.home_button_setting -> {
                val intent = Intent(applicationContext, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.home_button_selectMarket -> {
                val intent = Intent(applicationContext, FindMarketActivity::class.java)
                startActivity(intent)
            }
            R.id.home_button_start -> {
                if(home_button_start.isEnabled){
                    startOverlayViewService()
                } else{

                }
            }
        }
    }

    private fun initAccountChart(){
        home_horizontalBarChart_account.apply {
            setPinchZoom(false)
            setDrawBarShadow(false)
            setDrawGridBackground(false)
            description.isEnabled = false
            setDrawValueAboveBar(false)
            isHighlightFullBarEnabled = false
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawGridLinesBehindData(false)
            xAxis.setDrawLabels(false)
            xAxis.setDrawAxisLine(false)
            xAxis.position = XAxis.XAxisPosition.BOTH_SIDED
            xAxis.mAxisMaximum = 100f
            xAxis.mAxisMinimum = 0f
            setTouchEnabled(false)
        }
        home_horizontalBarChart_account.legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            setDrawInside(true)
            form = Legend.LegendForm.LINE
        }
    }

    private fun updateAccountChart(){
        val list1 = arrayListOf<BarEntry>(BarEntry(1f, floatArrayOf(20f, 40f, 40f)))
        val barDataSet1 = BarDataSet(list1, "")
        barDataSet1.setColors(Color.RED, Color.BLUE, Color.GREEN)
        barDataSet1.valueTextColor = Color.WHITE
        barDataSet1.stackLabels = arrayOf("1", "2", "3")
        val barData = BarData(barDataSet1)
        barData.setValueTextColor(Color.WHITE)
        home_horizontalBarChart_account.data = barData
    }

    private fun updateSelectedMarketInfo(){
        val accountsList = ApplicationCache.getInstance().accountsList
        if(Utils.isNull(accountsList)){
            Logger.e("accountsList is null")
            return
        }
        val marketInfo = ApplicationCache.getInstance().selectedMarket
        if(Utils.isNull(marketInfo)){
            Logger.e("marketInfo is null")
            return
        }
        val marketOrderInfo = ApplicationCache.getInstance().selectedMarketOrderInfo
        if(Utils.isNull(marketOrderInfo)){
            Logger.e("marketOrderInfo is null")
            return
        }
        home_textView_marketName.setText("${marketInfo.korean_name}(${marketInfo.english_name})")
        home_textView_feeValue.setText("매수 : ${marketOrderInfo.ask_fee.toInt()*100}% 매도 : ${marketOrderInfo.bid_fee.toInt()*100}%")
        var balanceValue: String = "0"
        var avgBuyPriceValue = "0"
        var unitCurrency = ""
        for(account in accountsList){
            val askCurrency = marketOrderInfo.market.ask.currency
            if(account.currency.equals(askCurrency)){
                balanceValue = account.balance
                unitCurrency = account.currency
                avgBuyPriceValue = account.avg_buy_price
            }
        }
        home_textView_balanceValue.setText("${balanceValue} ${unitCurrency}")
        home_textView_avgBuyPriceValue.setText("${avgBuyPriceValue} ${unitCurrency}")
    }

    private fun startOverlayViewService(){
        val intent = Intent(applicationContext, OverlayViewService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else{
            startService(intent)
        }
    }
}