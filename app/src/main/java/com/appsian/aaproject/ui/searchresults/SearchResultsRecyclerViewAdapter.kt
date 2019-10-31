/**
 * Created by Jerome Raymond.
 */

package com.appsian.aaproject.ui.searchresults


import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.appsian.aaproject.R
import com.appsian.aaproject.db.entities.Repo
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.search_results_view_list_item.view.*


class SearchResultsRecyclerViewAdapter:
    RecyclerView.Adapter<SearchResultsViewHolder>() {
    /**
     * List of Categories List that also updates recycler view
     */
    private var searchResults: MutableList<Repo>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder =
        SearchResultsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.search_results_view_list_item, parent, false)
        )

    override fun onBindViewHolder(viewHolder: SearchResultsViewHolder, position: Int) {
        viewHolder.bind(
            searchResults!![position].avatarUrl,
            searchResults!![position].stars,
            searchResults!![position].name,
            searchResults!![position].description!!
        )
    }


    override fun getItemCount(): Int {
        return searchResults?.size ?: 0
    }


}

class SearchResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(avatar_url: String, stargazers_count: Int, name: String, description: String) {
        Glide.with(itemView.context).load(avatar_url).into(itemView.avatar_url)
        itemView.stargazers_count.text = stargazers_count.toString()
        itemView.name.text = name
        itemView.description.text = description
    }
}