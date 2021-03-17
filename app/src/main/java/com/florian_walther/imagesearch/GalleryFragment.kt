package com.florian_walther.mvvmimagesearch

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.florian_walther.imagesearch.GalleryViewModel
import com.florian_walther.imagesearch.LoadAdapter
import com.florian_walther.imagesearch.R
import com.florian_walther.imagesearch.UnsplashAdapter
import com.florian_walther.imagesearch.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    val viewModel by viewModels<GalleryViewModel>()
    var _binding: FragmentGalleryBinding? = null
    val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        _binding = FragmentGalleryBinding.bind(view)

        val adapter = UnsplashAdapter()
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadAdapter { adapter.retry() }, footer = LoadAdapter { adapter.retry() }
            )
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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