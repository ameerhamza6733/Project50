package com.yourdomain.project50.Fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.yourdomain.project50.Activitys.ExcersizeListActivity
import com.yourdomain.project50.Model.ExerciseDay
import com.yourdomain.project50.Model.ExcersizePlan

import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.FragmentABSPlanViewModle


/**
 * A simple [Fragment] subclass.
 */
class ABSPlanDayFragment : Fragment() {

    private lateinit var recyclerView2: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=   inflater.inflate(R.layout.fragment_absplan, container, false)
        recyclerView2 = view.findViewById(R.id.recylerview)
        intDataSet()
        return view
    }

    private class EachExcersizeDayAdupter(val exerciseList: MutableList<ExerciseDay>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ExerciseDay.VIEW_TYPE_DAY -> {
                    ExcersizeDAYViewHolder(LayoutInflater.from(p0.context)
                            .inflate(R.layout.each_excersize_day, p0, false));

                }
                ExcersizePlan.TYPE_AD -> {
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
            return exerciseList.size
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

            if (ExerciseDay.VIEW_TYPE_DAY == p0.itemViewType) {
                p0 as ExcersizeDAYViewHolder
                if (exerciseList[p0.adapterPosition].totaleExcersizes>0){
                    p0.progressBar.max=exerciseList[p0.adapterPosition].totaleExcersizes.toInt()
                }
                if (exerciseList[p0.adapterPosition].totaleExcersizes>0){
                    p0.progressBar.progress=exerciseList[p0.adapterPosition].doneExcersises.toInt()
                    p0.tvProgress.setText(exerciseList[p0.adapterPosition].progress)
                }
                p0.tvDay.text = "Day " + exerciseList[p0.adapterPosition].day.toString()
            } else if (ExerciseDay.VIEW_TYPE_AD == p0.itemViewType) {
                p0 as AdViewHolderViewHolder
            }else if (ExerciseDay.VIEW_TYPEREST == p0.itemViewType) {
                p0 as RrestDayViewHolder
                p0.tvRestDay.text = "Day " + exerciseList[p0.adapterPosition].day.toString()
            }

        }

        override fun getItemViewType(position: Int): Int {
            return exerciseList[position].viewType
        }

        inner class ExcersizeDAYViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvDay: TextView
            val progressBar: ProgressBar
            val tvProgress:TextView

            init {

                itemView.setOnClickListener {
                    val intent=  Intent(itemView.context, ExcersizeListActivity::class.java)
                    intent.putExtra(ExcersizeListActivity.EXTRA_PLAN,ExcersizePlan.PLAN_ABS)
                    intent.putExtra(ExcersizeListActivity.EXTRA_DAY,adapterPosition)
                    itemView.context.startActivity(intent)
                }
                tvDay = itemView.findViewById(R.id.tvDay)
                tvProgress=itemView.findViewById(R.id.tvProgress)
                progressBar = itemView.findViewById(R.id.progressBar)
            }
        }

        inner class RrestDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvRestDay: TextView

            init {
                tvRestDay = itemView.findViewById(R.id.tvRestDay)
            }
        }
        inner class AdViewHolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
           // var nativeAd: TextView

            init {

               // nativeAd = itemView.findViewById(R.id.native_Adview)
            }
        }
    }

    fun intDataSet() {
        val modleDays = ViewModelProviders.of(this!!).get(FragmentABSPlanViewModle::class.java)
        modleDays.getDays()?.observe(this!!, Observer {
            it?.let {
                var excersizeDaysAdupter = EachExcersizeDayAdupter(it);
                recyclerView2.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                recyclerView2.adapter = excersizeDaysAdupter

            }

        })
    }


}// Required empty public constructor
