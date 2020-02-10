package com.example.hsexercise.feature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hsexercise.R
import com.example.hsexercise.feature.database.FeatureModel
import kotlinx.android.synthetic.main.feature_item.view.*

class FeatureAdapter(private var features: List<FeatureModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.feature_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FeatureViewHolder).bind(features[position])
    }

    override fun getItemCount(): Int {
        return features.size
    }

    class FeatureViewHolder (private var view: View) : RecyclerView.ViewHolder(view) {
        private val featureImage = view.feature_image
        private val featureAuthor = view.feature_author
        private val featureDimensions = view.feature_dimensions

        fun bind(feature : FeatureModel) {
            featureAuthor.text = feature.author
            featureDimensions.text = view.context
                .getString(R.string.feature_desc, feature.width.toString(), feature.height.toString())
            Glide.with(view).load(feature.download_url).into(featureImage)
        }
    }
}

