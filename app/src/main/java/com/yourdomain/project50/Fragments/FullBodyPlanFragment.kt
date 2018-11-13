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
import android.widget.TextView
import com.yourdomain.project50.Activitys.ExcersizeListActivity
import com.yourdomain.project50.Model.ExcersizeDays
import com.yourdomain.project50.Model.ExcersizePlans

import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.ExcersizeDayGernaterViewModle


/**
 * A simple [Fragment] subclass.
 * Use the [FullBodyPlanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FullBodyPlanFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private lateinit var recyclerView2: RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
     var view=   inflater.inflate(R.layout.fragment_blank, container, false)
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

            init {

                itemView.setOnClickListener {
                  val intent=  Intent(itemView.context,ExcersizeListActivity::class.java)
                    intent.putExtra(ExcersizeListActivity.EXTRA_DAY,adapterPosition)
                    itemView.context.startActivity(intent) }
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

    fun intDataSet() {
        val modleDays = ViewModelProviders.of(this).get(ExcersizeDayGernaterViewModle::class.java)
      modleDays.getDays()?.observe(this, Observer {
          it?.let {
              var excersizeDaysAdupter = EachExcersizeDayAdupter(it);
              recyclerView2.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
              recyclerView2.adapter = excersizeDaysAdupter

          }

      })
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FullBodyPlanFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): FullBodyPlanFragment {
            val fragment = FullBodyPlanFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
