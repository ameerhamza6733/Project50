package com.yourdomain.project50.Fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.yourdomain.project50.Activitys.ExcersizeListActivity
import com.yourdomain.project50.Model.ExcersizeDays
import com.yourdomain.project50.Model.ExcersizePlans

import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.FragmentFullBodyPlanViewModle


class FullBodyPlanDayFragment : Fragment() {

    private lateinit var recyclerView2: RecyclerView;
 val TAG="FullBodyPlanDayFragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_blank, container, false)
        recyclerView2 = view.findViewById(R.id.daysRecyclerView)
        intDataSet()
        return view
    }

    private class EachExcersizeDayAdupter(val excersizeList: MutableList<ExcersizeDays>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ExcersizeDays.VIEW_TYPE_DAY -> {
                    ExcersizeDAYViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_excersize_day, p0, false));

                }
                ExcersizePlans.TYPE_AD -> {
                    AdViewHolderViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.native_adview, p0, false));
                }
                else -> {
                    ExcersizeDAYViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_excersize_day, p0, false));
                }
            }


        }

        override fun getItemCount(): Int {
            return excersizeList.size
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

            if (ExcersizeDays.VIEW_TYPE_DAY == p0.itemViewType) {
                p0 as ExcersizeDAYViewHolder
                if (excersizeList[p0.adapterPosition].totaleExcersizes>0){
                    p0.progressBar.max=excersizeList[p0.adapterPosition].totaleExcersizes.toInt()
                }
                if (excersizeList[p0.adapterPosition].totaleExcersizes>0){
                    p0.progressBar.progress=excersizeList[p0.adapterPosition].doneExcersises.toInt()
                    p0.tvProgress.setText(excersizeList[p0.adapterPosition].progress)
                }
                p0.tvDay.text = "Day " + excersizeList[p0.adapterPosition].day.toString()
            } else if (ExcersizeDays.VIEW_TYPE_AD == p0.itemViewType) {
                p0 as AdViewHolderViewHolder
            }

        }

        override fun getItemViewType(position: Int): Int {
            return excersizeList[position].viewType
        }

        inner class ExcersizeDAYViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvDay: TextView
            val progressBar: ProgressBar
            val tvProgress:TextView
            val TAG="FullBodyPlanDayFragment"

            init {

                itemView.setOnClickListener {
                    Log.d(TAG,""+excersizeList[adapterPosition].toString())
                    val intent = Intent(itemView.context, ExcersizeListActivity::class.java)
                    intent.putExtra(ExcersizeListActivity.EXTRA_EXCERSIZES_DONE,excersizeList[adapterPosition].doneExcersises.toInt())
                    intent.putExtra(ExcersizeListActivity.EXTRA_PLAN,ExcersizePlans.PLAN_FULL_BODY)
                    intent.putExtra(ExcersizeListActivity.EXTRA_DAY, adapterPosition)
                    itemView.context.startActivity(intent)
                }
                tvDay = itemView.findViewById(R.id.tvDay)
                tvProgress=itemView.findViewById(R.id.tvProgress)
                progressBar = itemView.findViewById(R.id.progressBar)
            }
        }


        inner class AdViewHolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var nativeAd: TextView

            init {

                nativeAd = itemView.findViewById(R.id.nativeAd)
            }
        }
    }

    fun intDataSet() {
        val modleDays = ViewModelProviders.of(activity!!).get(FragmentFullBodyPlanViewModle::class.java)
        modleDays.getDays()?.observe(activity!!, Observer {
            it?.let {
                var excersizeDaysAdupter = EachExcersizeDayAdupter(it);
                recyclerView2.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                recyclerView2.adapter = excersizeDaysAdupter

            }

        })
    }


}// Required empty public constructor
