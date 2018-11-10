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
import com.yourdomain.project50.Model.Excersize
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.ExcersizePlansViewModle
import kotlinx.android.synthetic.main.activity_all_excersizes.*

class AllExcersizesActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_all_excersizes)
        recyclerView=findViewById(R.id.excersizeType)
        recyclerView2=findViewById(R.id.islandRecyclerView)
        intiDataSet()
      //  navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
    private fun intiDataSet(){
        val model = ExcersizePlansViewModle(application)
        var list  = model.getExcersizePlans();
        if (list.size>0) {
            var excersizeAdupter = ExcersizeAdupter(list);
            recyclerView.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter=excersizeAdupter
            recyclerView2.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
            recyclerView2.adapter=excersizeAdupter
        }

    }
    private class ExcersizeAdupter(val excersizes: MutableList<Excersize>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                Excersize.TYPE_EXCERSISE -> {
                    ExcersizeViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.singal_excersize, p0, false));
                }
                Excersize.TYPE_AD -> {
                    AdViewHolderViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.native_adview, p0, false));
                }
                else -> {
                    ExcersizeViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.singal_excersize, p0, false));
                }
            }


        }

        override fun getItemCount(): Int {
            return excersizes.size
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

            if (Excersize.TYPE_EXCERSISE == p0.itemViewType) {
                p0 as ExcersizeViewHolder
                p0.tvtitle.text=excersizes[p0.adapterPosition].name
                Glide.with(p0.tvtitle.context).load(excersizes[p0.adapterPosition].image).into(p0.image)

            } else if (Excersize.TYPE_AD == p0.itemViewType) {
                p0 as AdViewHolderViewHolder
            }

        }

        override fun getItemViewType(position: Int): Int {
            return excersizes[position].ViewType
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

}
