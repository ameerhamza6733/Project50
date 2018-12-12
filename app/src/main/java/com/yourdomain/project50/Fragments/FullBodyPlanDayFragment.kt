package com.yourdomain.project50.Fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.yourdomain.project50.Activitys.EachPlanExcersizesActivity
import com.yourdomain.project50.Activitys.ExcersizeListActivity
import com.yourdomain.project50.Model.ExcersizePlan
import com.yourdomain.project50.Model.ExerciseDay
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.FragmentFullBodyPlanViewModle
import java.util.*


class FullBodyPlanDayFragment : Fragment() {

    companion object {
        public var refrashRecylerViewIndex = -1;
    }
    private lateinit var recyclerView2: RecyclerView;
    private  var excersizeDaysAdupter: EachExcersizeDayAdupter?=null
    private var mDataSet = ArrayList<ExerciseDay>()

    val TAG = "FullBodyPlanDayFragment"
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

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: refresh the index " + refrashRecylerViewIndex)

            if (refrashRecylerViewIndex != -1) {
                val updatedDay =  ExerciseDay(mDataSet.get(refrashRecylerViewIndex).day,mDataSet.get(refrashRecylerViewIndex).viewType,1,1,"100%")
                mDataSet.set(refrashRecylerViewIndex,updatedDay)
               excersizeDaysAdupter?.notifyItemChanged(refrashRecylerViewIndex)
                excersizeDaysAdupter?.notifyDataSetChanged()
                refrashRecylerViewIndex = -1
            }


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

            if (ExerciseDay.VIEW_TYPE_DAY == p0.itemViewType) {
                p0 as ExcersizeDAYViewHolder
                if (exerciseList[p0.adapterPosition].totaleExcersizes > 0) {
                    p0.progressBar.max = exerciseList[p0.adapterPosition].totaleExcersizes.toInt()
                }
                if (exerciseList[p0.adapterPosition].totaleExcersizes > 0) {
                    p0.progressBar.progress = exerciseList[p0.adapterPosition].doneExcersises.toInt()
                    p0.tvProgress.setText(exerciseList[p0.adapterPosition].progress)
                }
                p0.tvDay.text = "Day " + exerciseList[p0.adapterPosition].day.toString()
            } else if (ExerciseDay.VIEW_TYPE_AD == p0.itemViewType) {
                p0 as AdViewHolderViewHolder
            } else if (ExerciseDay.VIEW_TYPEREST == p0.itemViewType) {
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
            val tvProgress: TextView
            val TAG = "FullBodyPlanDayFragment"

            init {

                itemView.setOnClickListener {
                    Log.d(TAG, "" + exerciseList[adapterPosition].toString())
                    val intent = Intent(itemView.context, ExcersizeListActivity::class.java)
                    intent.putExtra(ExcersizeListActivity.EXTRA_EXCERSIZES_DONE, exerciseList[adapterPosition].doneExcersises.toInt())
                    intent.putExtra(ExcersizeListActivity.EXTRA_PLAN, ExcersizePlan.PLAN_FULL_BODY)
                    intent.putExtra(ExcersizeListActivity.EXTRA_DAY, adapterPosition)
                    itemView.context.startActivity(intent)
                    if (EachPlanExcersizesActivity.mRewardedVideoAd?.isLoaded == true) {
                        EachPlanExcersizesActivity.mRewardedVideoAd?.show()
                    }
                }
                tvDay = itemView.findViewById(R.id.tvDay)
                tvProgress = itemView.findViewById(R.id.tvProgress)
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
            var nativeAd: FrameLayout

            init {

                nativeAd = itemView.findViewById(R.id.adPlaceholder)
            }
        }
    }

    fun intDataSet() {
        val modleDays = ViewModelProviders.of(activity!!).get(FragmentFullBodyPlanViewModle::class.java)
        modleDays.getDays()?.observe(activity!!, Observer {
            it?.let {
                mDataSet = it
                if (excersizeDaysAdupter == null)
                {
                    excersizeDaysAdupter = EachExcersizeDayAdupter(mDataSet);
                }else{
                    Log.d(TAG,"adupter not null: ")
                }
                recyclerView2.setHasFixedSize(false)
                recyclerView2.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                recyclerView2.adapter = excersizeDaysAdupter

            }

        })
    }


}// Required empty public constructor
