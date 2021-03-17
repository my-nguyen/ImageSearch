package com.florian_walther.mvvmimagesearch

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.florian_walther.imagesearch.GalleryViewModel
import com.florian_walther.imagesearch.LoadAdapter
import com.florian_walther.imagesearch.R
import com.florian_walther.imagesearch.PhotoAdapter
import com.florian_walther.imagesearch.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), PhotoAdapter.OnItemClickListener {

    val viewModel by viewModels<GalleryViewModel>()
    var _binding: FragmentGalleryBinding? = null
    val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        _binding = FragmentGalleryBinding.bind(view)

        val adapter = PhotoAdapter(this)
        binding.apply {
            recyclerView.setHasFixedSize(true)
            // prevent old data from flashing right before new data is displayed
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadAdapter { adapter.retry() }, footer = LoadAdapter { adapter.retry() }
            )
            btnRetry.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener {
            binding.apply {
                progressBar.isVisible = it.source.refresh is LoadState.Loading
                recyclerView.isVisible = it.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = it.source.refresh is LoadState.Error
                tvError.isVisible = it.source.refresh is LoadState.Error

                // page is not loading AND there's no more data to load AND there's no item in the adapter
                if (it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && adapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    tvEmpty.isVisible = true
                } else {
                    tvEmpty.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(photo: Photo) {
        val action = GalleryFragmentDirections.actionGalleryFragment2ToDetailFragment(photo)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_gallery, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)

                    // viewModel.searchPhotos() updates viewModel.currentQuery which triggers
                    // the switchmap which executes repository.searchPhotos() which updates
                    // viewModel.photos LiveData which this Fragment observes and so will be notified
                    // of updates
                    viewModel.searchPhotos(query)

                    // hide the keyboard
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}