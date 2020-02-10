package com.example.hsexercise.feature

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsexercise.R
import com.example.hsexercise.api.Resource
import com.example.hsexercise.common.BaseActivity
import com.example.hsexercise.feature.database.FeatureModel
import com.google.android.material.snackbar.Snackbar

class FeatureActivity : BaseActivity<FeatureViewModel>() {
    override val viewModelClass = FeatureViewModel::class.java
    override val layoutResId = R.layout.activity_feature

    override fun provideViewModelFactory() = FeatureViewModel.Factory()

    override fun onViewLoad(savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = findViewById(R.id.feature_recycler)
        val emptyView: TextView = findViewById(R.id.empty_view)
        val loadingView: ProgressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this,
            RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        viewModel.getFeaturesList(application)

        val booksObserver = Observer<Resource<List<FeatureModel>>> { list ->
            when (list) {
                is Resource.Success -> {
                    loadingView.visibility = View.GONE
                    emptyView.visibility = View.GONE
                    list.data?.let { recyclerView.adapter = FeatureAdapter(it) }
                }

                is Resource.Loading -> {
                    loadingView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }

                is Resource.Error -> Snackbar.make(recyclerView, getString(R.string.error_text),
                    Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.featuresList.observe(this, booksObserver)
    }
}
