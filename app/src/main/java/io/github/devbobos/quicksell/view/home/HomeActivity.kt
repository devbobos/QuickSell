package io.github.devbobos.quicksell.view.home

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.orhanobut.logger.Logger
import io.github.devbobos.quicksell.ApplicationCache
import io.github.devbobos.quicksell.BaseActivity
import io.github.devbobos.quicksell.R
import io.github.devbobos.quicksell.api.UpbitAPIService
import io.github.devbobos.quicksell.api.models.Accounts
import io.github.devbobos.quicksell.helper.utils.Utils
import io.github.devbobos.quicksell.service.OverlayService
import io.github.devbobos.quicksell.view.SettingActivity
import io.github.devbobos.quicksell.view.findmarket.FindMarketActivity
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat


class HomeActivity: BaseActivity(), View.OnClickListener {
    val requestCodeForRequestOverlayPermission = 3

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
                home_button_start.isEnabled = true
                val list = listOf<Accounts>(
                    Accounts("BTC", "100", "", "1000", false, "BTC"), Accounts(
                        "KRW",
                        "1000",
                        "",
                        "1000",
                        false,
                        "KRW"
                    )
                )
                updateAccountChart(list)
                home_horizontalBarChart_account.visibility = View.VISIBLE
            }
        }
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
                if (home_button_start.isEnabled) {
                    if (checkOverlayPermission()) {
                        startOverlayService()
                    } else {
                        requestOverlayPermission(requestCodeForRequestOverlayPermission)
                    }
                } else {
                    showToast("먼저 매수/매도 대상 코인을 선택해주세요")
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
            setTouchEnabled(false)
//            setViewPortOffsets(0f, 0f, 0f, 0f);
//            setVisibleXRangeMinimum(0f)
//            setVisibleXRangeMaximum(100f)
            xAxis.mAxisMinimum = 0f
            xAxis.mAxisMaximum = 100f
            fitScreen()
        }
        home_horizontalBarChart_account.legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            setDrawInside(true)
            form = Legend.LegendForm.CIRCLE
        }
    }

    private fun updateAccountInfo(accountsList: List<Accounts>){
        var krwBalance: Float = 0f
        var allBalance: Float = 0f
        for(item in accountsList){
            if("KRW".equals(item.currency)){
                var krwBalanceFloat = item.balance.float
                krwBalance = krwBalanceFloat
            } else{
                var balanceFloat = item.balance.float
                var avgBuyPriceFloat = item.avg_buy_price.float
                allBalance += balanceFloat * avgBuyPriceFloat
            }
        }
        allBalance += krwBalance
        val stringFormat = DecimalFormat("###,###,##0.0")
        home_textView_accountKrwBalanceValue.setText("${stringFormat.format(krwBalance)}")
        home_textView_accountAllBalanceValue.setText("${stringFormat.format(allBalance)}")
    }

    private fun updateAccountChart(accountsList: List<Accounts>){
        val valuePercentageList = mutableListOf<Float>()
        val labelList = mutableListOf<String>()
        var valueSum = 0f
        for(item in accountsList){
            var balanceFloatValue = item.balance.float
            valueSum = valueSum + balanceFloatValue
            valuePercentageList.add(balanceFloatValue)
            labelList.add(item.currency)
        }
        var position = 0
        for(item in valuePercentageList){
            if(item == 0f){
                //zero operation
            } else{
                valuePercentageList.set(position, item / valueSum * 100f)
            }
            position++;
        }
        val dataList = arrayListOf<BarEntry>(BarEntry(1f, valuePercentageList.toFloatArray()))
        val barDataSet = BarDataSet(dataList, "")
        barDataSet.setColors(
            getColor(R.color.instagram_1), getColor(R.color.instagram_2), getColor(
                R.color.instagram_3
            ), getColor(R.color.instagram_4), getColor(R.color.instagram_5)
        )
        barDataSet.valueTextColor = Color.WHITE
        barDataSet.stackLabels = labelList.toTypedArray()
        val barData = BarData(barDataSet)
        barData.setValueTextColor(Color.WHITE)
        barData.setValueFormatter(PercentFormatter())
        home_horizontalBarChart_account.data = barData
        home_horizontalBarChart_account.notifyDataSetChanged()
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
        home_textView_feeValue.setText("매수 : ${marketOrderInfo.ask_fee.int * 100}% 매도 : ${marketOrderInfo.bid_fee.int * 100}%")
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

    private fun startOverlayService(){
        val intent = Intent(applicationContext, OverlayService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else{
            startService(intent)
        }
    }

    private fun checkOverlayPermission(): Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if(Settings.canDrawOverlays(this)){
                return true
            } else{
                return false
            }
        }
        return true
    }

    private fun requestOverlayPermission(requestCode: Int){
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.setData(Uri.parse("package:$packageName"))
        startActivityForResult(intent, requestCode)
    }
}