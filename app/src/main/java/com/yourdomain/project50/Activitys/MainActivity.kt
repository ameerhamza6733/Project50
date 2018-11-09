package com.yourdomain.project50.Activitys

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yourdomain.project50.Model.Excersize
import com.yourdomain.project50.R
import com.yourdomain.project50.ViewModle.ExcersizePlansViewModle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
recyclerView=findViewById(R.id.recylerview)
        intiDataSet()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

   private fun intiDataSet(){
        val model = ExcersizePlansViewModle(application)
       var list  = model.getExcersizePlans();
        if (list.size>0) {
            var excersizeAdupter = ExcersizeAdupter(list);
            recyclerView.layoutManager=LinearLayoutManager(this)
            recyclerView.adapter=excersizeAdupter
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



