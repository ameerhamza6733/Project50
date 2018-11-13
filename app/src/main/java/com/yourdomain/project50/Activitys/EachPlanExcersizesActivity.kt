package com.yourdomain.project50.Activitys

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yourdomain.project50.Model.ExcersizeDays
import com.yourdomain.project50.Model.ExcersizePlans
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.ExcersizeDayGernaterViewModle
import com.yourdomain.project50.ViewModle.ExcersizePlansViewModle

class EachPlanExcersizesActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_days_excersizes)
        recyclerView = findViewById(R.id.excersizeType)
        recyclerView2 = findViewById(R.id.islandRecyclerView)
        intiDataSet()
        //  navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun intiDataSet() {
        val model = ExcersizePlansViewModle(application)
        var list = model.getExcersizePlans();
        if (list.size > 0) {
            var excersizeAdupter = ExcersizePlansAdupter(list);
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = excersizeAdupter
//            var excersizeDaysAdupter = EachExcersizeDayAdupter(list);
//            recyclerView2.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
//            recyclerView2.adapter=excersizeDaysAdupter
        }


        val modleDays = ExcersizeDayGernaterViewModle()
        val dayList = modleDays.getDays()
        dayList?.let {
            var excersizeDaysAdupter = EachExcersizeDayAdupter(it);
            recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recyclerView2.adapter = excersizeDaysAdupter
        }

    }

    private class ExcersizePlansAdupter(val excersizePlans: MutableList<ExcersizePlans>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ExcersizePlans.TYPE_EXCERSISE -> {
                    ExcersizeViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_excersize_plan_horizental_row, p0, false));
                }
                ExcersizePlans.TYPE_AD -> {
                    AdViewHolderViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.native_adview, p0, false));
                }
                else -> {
                    ExcersizeViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_excersize_plan, p0, false));
                }
            }


        }

        override fun getItemCount(): Int {
            return excersizePlans.size
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

            if (ExcersizePlans.TYPE_EXCERSISE == p0.itemViewType) {
                p0 as ExcersizeViewHolder
                p0.tvtitle.text = excersizePlans[p0.adapterPosition].name
                Glide.with(p0.tvtitle.context).load(excersizePlans[p0.adapterPosition].image).into(p0.image)

            } else if (ExcersizePlans.TYPE_AD == p0.itemViewType) {
                p0 as AdViewHolderViewHolder
            }

        }

        override fun getItemViewType(position: Int): Int {
            return excersizePlans[position].ViewType
        }

        inner class ExcersizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvtitle: TextView
            var image: ImageView
            var tvTotalDays: TextView

            init {
                tvtitle = itemView.findViewById(R.id.excersizeTitle)
                image = itemView.findViewById(R.id.image)
                tvTotalDays = itemView.findViewById(R.id.tvTotalDays)
            }
        }


        inner class AdViewHolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var nativeAd: TextView

            init {

                nativeAd = itemView.findViewById(R.id.nativeAd)
            }
        }
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
                p0.tvDay.text= "Day "+excersizeList[p0.adapterPosition].day.toString()
            } else if (ExcersizeDays.VIEW_TYPE_AD == p0.itemViewType) {
                p0 as AdViewHolderViewHolder
            }

        }

        override fun getItemViewType(position: Int): Int {
            return excersizeList[position].viewType
        }

        inner class ExcersizeDAYViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
             val tvDay: TextView

            init {
                tvDay = itemView.findViewById(R.id.tvDay)
            }
        }


        inner class AdViewHolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var nativeAd: TextView

            init {

                nativeAd = itemView.findViewById(R.id.nativeAd)
            }
        }
    }
}
