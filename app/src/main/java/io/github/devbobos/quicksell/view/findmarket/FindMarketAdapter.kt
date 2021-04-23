package io.github.devbobos.quicksell.view.findmarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.devbobos.quicksell.R
import io.github.devbobos.quicksell.api.models.MarketInfo
import io.github.devbobos.quicksell.helper.utils.Utils
import kotlinx.android.synthetic.main.findmarket_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindMarketAdapter: RecyclerView.Adapter<FindMarketAdapter.FindMarketViewHolder>{
    lateinit var activity: FindMarketActivity
    lateinit var list: List<MarketInfo>
    var selectedPosition = -1

    constructor(activity: FindMarketActivity, list: List<MarketInfo>) : super() {
        this.activity = activity
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindMarketViewHolder {
        val viewHolder = FindMarketViewHolder(parent)
        return viewHolder
    }

    override fun onBindViewHolder(holder: FindMarketViewHolder, position: Int) {
        val item = list.get(position)
        with(holder.itemView){
            findMarket_item_textView_title.setText(item.korean_name)
            findMarket_item_textView_body.setText(item.english_name)
            if(selectedPosition == -1){
                activity.changeSubmitButtonStateChange(false)
                findMarket_item_checkBox.isChecked = false
            } else{
                activity.changeSubmitButtonStateChange(true)
                if(position == selectedPosition){
                    findMarket_item_checkBox.isChecked = true
                } else{
                    findMarket_item_checkBox.isChecked = false
                }
            }

            findMarket_item_checkBox.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {
                    GlobalScope.launch(Dispatchers.Default) {
                        if(findMarket_item_checkBox.isChecked){
                            selectedPosition = position
                        } else{
                            selectedPosition = -1
                        }
                        withContext(Dispatchers.Main){
                            notifyDataSetChanged()
                        }
                    }
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getSelectedItem(): MarketInfo{
        return list.get(selectedPosition)
    }

    inner class FindMarketViewHolder(parent:ViewGroup): RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.findmarket_item, parent, false)
    )
}