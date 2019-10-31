/**
 * Created by Jerome Raymond.
 */

package com.appsian.aaproject.ui.searchresults

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appsian.aaproject.R
import com.appsian.aaproject.ui.main.MainFragmentArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SearchResultsFragment: Fragment() {
	// Lazy Inject ViewModel
	val searchResultsViewModel: SearchResultsViewModel by viewModel()
	companion object {
		
		fun newIntent(context: Context): Intent {
		
			// Fill the created intent with the data you want to be passed to this Activity when it's opened.
			return Intent(context, SearchResultsFragment::class.java)
		}
	}
	
	private lateinit var recyclerRecyclerView: RecyclerView

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {

		return inflater.inflate(R.layout.main_view_activity,container,false)
	}
	private fun init() {
		val params = MainFragmentArgs.fromBundle(arguments!!)
		searchResultsViewModel.setQuery(params.query)
		// Configure Recycler component
		recyclerRecyclerView = activity!!.findViewById(R.id.recycler_recycler_view)
		recyclerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		recyclerRecyclerView.adapter = SearchResultsRecyclerViewAdapter()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		this.init()
		initRepoList()
	}

	private fun initRepoList() {

		searchResultsViewModel.repositories.observe(viewLifecycleOwner, Observer { repos ->
			//recyclerRecyclerView.adapter.s
		})
	}
}
