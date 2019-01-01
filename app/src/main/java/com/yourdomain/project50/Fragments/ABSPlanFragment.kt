package com.yourdomain.project50.Fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.yourdomain.project50.Activitys.ExcersizeListActivity
import com.yourdomain.project50.MY_Shared_PREF
import com.yourdomain.project50.Model.CurrentDayandPlan
import com.yourdomain.project50.Model.ExerciseDay
import com.yourdomain.project50.Model.ExcersizePlan

import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.FragmentABSPlanViewModle
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ABSPlanFragment : Fragment() {


    companion object {
        public var refrashRecylerViewIndex = -1;
    }

    private lateinit var recyclerView2: RecyclerView;
    private  var excersizeDaysAdupter: EachExcersizeDayAdupter?=null
    private var mDataSet = ArrayList<ExerciseDay>()
    private var onRefreshCallback:onRefrech?=null
    private  var currentDayandPlan: CurrentDayandPlan?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=   inflater.inflate(R.layout.fragment_absplan, container, false)
        recyclerView2 = view.findViewById(R.id.recylerview)
        intDataSet()
        return view
    }


    override fun onResume() {
        super.onResume()


        if (refrashRecylerViewIndex != -1) {
            onRefreshCallback?.onRefrechAbsCallBack()
            val updatedDay =  ExerciseDay(mDataSet.get(refrashRecylerViewIndex).day,mDataSet.get(refrashRecylerViewIndex).viewType,1,1,"100%")
            mDataSet.set(refrashRecylerViewIndex,updatedDay)
            excersizeDaysAdupter?.notifyItemChanged(refrashRecylerViewIndex)
            excersizeDaysAdupter?.notifyDataSetChanged()
            refrashRecylerViewIndex = -1
        }


    }

    private inner class EachExcersizeDayAdupter(val exerciseList: MutableList<ExerciseDay>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
                ExerciseDay.VIEW_TYPEREST -> {
                    RrestDayViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.each_rest_day, p0, false))
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

            p0.setIsRecyclable(false)
            if (ExerciseDay.VIEW_TYPE_DAY == p0.itemViewType) {
                p0 as ExcersizeDAYViewHolder

                if (exerciseList[p0.adapterPosition].totaleExcersizes > 0) {
                    p0.progressBar.max = exerciseList[p0.adapterPosition].totaleExcersizes.toInt()
                }
                if (exerciseList[p0.adapterPosition].totaleExcersizes > 0) {
                    p0.progressBar.progress = exerciseList[p0.adapterPosition].doneExcersises.toInt()
                    if (exerciseList[p0.adapterPosition].progress.equals("100%")){
                        p0.tvProgress.visibility=View.INVISIBLE
                        p0.progressBar.visibility=View.INVISIBLE
                        p0.tvAllDaysCompleted.visibility=View.VISIBLE

                    }else{
                        p0.tvProgress.visibility=View.VISIBLE
                        p0.progressBar.visibility=View.VISIBLE
                        p0.tvAllDaysCompleted.visibility=View.INVISIBLE
                        p0.tvProgress.setText(exerciseList[p0.adapterPosition].progress)

                    }


                }
                p0.tvDay.text = "Day " + exerciseList[p0.adapterPosition].day.toString()
                if (currentDayandPlan?.day==p0.adapterPosition){
                    p0.rootEExcersizeDay.setCardBackgroundColor(ContextCompat.getColor(p0.progressBar.context,R.color.colorAccent))
                    p0.tvDay.setTextColor(ContextCompat.getColor(p0.progressBar.context, R.color.colorWhite))
                    p0.tvProgress.setTextColor(ContextCompat.getColor(p0.progressBar.context,R.color.colorWhite))
                }
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
            val rootEExcersizeDay: CardView
            val tvAllDaysCompleted: ImageButton
            init {

                itemView.setOnClickListener {
                    currentDayandPlan = CurrentDayandPlan(adapterPosition,ExcersizePlan.PLAN_ABS)
                    MY_Shared_PREF.saveCurrentDayandPlan(activity?.application!!, currentDayandPlan!!)

                    val intent=  Intent(itemView.context, ExcersizeListActivity::class.java)
                    intent.putExtra(ExcersizeListActivity.EXTRA_PLAN,ExcersizePlan.PLAN_ABS)
                    intent.putExtra(ExcersizeListActivity.EXTRA_DAY,adapterPosition)
                    itemView.context.startActivity(intent)
                    notifyDataSetChanged()
                }
                rootEExcersizeDay=itemView.findViewById(R.id.rootEachExcersizeDay)
                tvDay = itemView.findViewById(R.id.tvDay)
                tvProgress=itemView.findViewById(R.id.tvProgress)
                progressBar = itemView.findViewById(R.id.progressBar)
                tvAllDaysCompleted=itemView.findViewById(R.id.tvAllDaysCompleted)
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
                currentDayandPlan=MY_Shared_PREF.getCurrentDayPlan(activity?.application!!,ExcersizePlan.PLAN_ABS)
                mDataSet=it
                 excersizeDaysAdupter = EachExcersizeDayAdupter(it);
                recyclerView2.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                recyclerView2.adapter = excersizeDaysAdupter

            }

        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is onRefrech){
            onRefreshCallback=context
        }
    }
    public interface onRefrech{
        fun onRefrechAbsCallBack()
    }

}// Required empty public constructor
