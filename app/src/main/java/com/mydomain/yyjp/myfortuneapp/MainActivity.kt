package com.mydomain.yyjp.myfortuneapp

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.TextView
import com.mydomain.yyjp.myfortuneapp.viewmodel.MainActivityViewModel
import com.mydomain.yyjp.myfortuneapp.viewmodel.MainActivityViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.atomic.AtomicBoolean


class MainActivity : AppCompatActivity() {
    private val TAG : String = MainActivity::class.java.simpleName
    private lateinit var recyclerView : RecyclerView
    private  lateinit var adapter : ItemAdapter
    private lateinit var loadingView : TextView
    private lateinit var viewModel : MainActivityViewModel
    private var countDownTimer: CountDownTimer? = null
    private val isDone = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_main)
        val appToolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(appToolbar)
        val mainContentView : View = findViewById(R.id.main_content_view)
        bindViews(mainContentView)
        initializeViewModel()
        initializeTimeOut()
    }

    private fun initializeViewModel() {
        //observe live data, using Android Live Data/View Model
        val mainActivityViewModelFactory = MainActivityViewModelFactory(applicationContext)
        viewModel = ViewModelProviders
                    .of(this, mainActivityViewModelFactory)
                    .get(MainActivityViewModel::class.java)
        viewModel.itemList.observe(this, fortuneListUpdate)
        viewModel.errorStatus.observe(this, errorUpdate)
        fab.setOnClickListener {
            Log.d(TAG, "Updating Fortune")
            Snackbar.make(it, "Updating Fortune...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            viewModel.getFortune()
        }
    }

    private fun initializeTimeOut() {
        isDone.set(false)
        countDownTimer = object  : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                if(isDone.compareAndSet(true, false)) {
                    cancel()
                    countDownTimer = null
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Timeout Error. Retrying...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    initializeTimeOut()
                    viewModel.getFortune()

                }
                Log.d(TAG, "onFinish")
            }
        }.start()
    }

    private val fortuneListUpdate: Observer<List<String>> = Observer { fortuneItems ->
        Log.d(TAG, "onChanged: fortunes: = [$fortuneItems]")
        countDownTimer?.let {
            countDownTimer!!.cancel()
            countDownTimer = null
        }
        isDone.set(true)
        fortuneItems?.let {
            Log.d(TAG, "fortunes# " + fortuneItems.size)
            updateItemList(it)
        }
    }

    private val errorUpdate: Observer<String> = Observer { error ->
        var unknownError: String = getString(R.string.unknown_error)
        error?.let {
            unknownError = error
        }
        Log.e(TAG, "Error: $unknownError")
        Snackbar.make(findViewById(android.R.id.content), unknownError, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        if(countDownTimer == null && isDone.get() ) {
            showError(unknownError)
        }
    }

    private fun updateItemList(itemList : List<String> ) {
        adapter.update(itemList)
        this.loadingView.visibility = View.GONE
        this.recyclerView.visibility = View.VISIBLE
    }

    private fun showError(error : String? ) {
        this.loadingView.text = error
        this.loadingView.visibility = View.VISIBLE
        this.recyclerView.visibility = View.GONE
    }

    /*
     * bind views
     */
    private fun bindViews(parent: View) {
        loadingView = parent.findViewById(R.id.loading_text)
        recyclerView = parent.findViewById(R.id.recycler_view)
        this.recyclerView.layoutManager = LinearLayoutManager(parent.context, LinearLayoutManager.VERTICAL, false)
        this.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        adapter = ItemAdapter()
        recyclerView.adapter = adapter
        recyclerView.visibility = View.GONE

    }
}
