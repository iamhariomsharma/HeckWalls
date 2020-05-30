package com.heckteck.heckwalls.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heckteck.heckwalls.R
import com.heckteck.heckwalls.adapters.WallpaperAdapter
import com.heckteck.heckwalls.viewmodels.WallpaperViewModel
import com.heckteck.heckwalls.models.Wallpaper
import com.heckteck.heckwalls.repository.FirebaseRepository
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), (Wallpaper) -> Unit {

    private val firebaseRepository =
        FirebaseRepository()
    private var navController: NavController? = null
    private var wallpapersList: List<Wallpaper> = ArrayList()
    private var wallpaperAdapter: WallpaperAdapter =
        WallpaperAdapter(wallpapersList, this)
    private var isLoading: Boolean = true
    lateinit var viewModel: WallpaperViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WallpaperViewModel::class.java)

        (activity as AppCompatActivity).setSupportActionBar(main_toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = "Heck Wallpapers"

        navController = Navigation.findNavController(view)

        if (firebaseRepository.getUser() == null) {
            navController?.navigate(R.id.action_homeFragment_to_registerFragment)
        }

//        if (wallpapersList.isNullOrEmpty()) {
//            emptyText.visibility = View.VISIBLE
//            rv_wallpapers.visibility = View.GONE
//        }

        rv_wallpapers.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = wallpaperAdapter
        }

        rv_wallpapers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        viewModel.loadWallpapersData()
                        isLoading = true
                    }
                }
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getWallpapersList().observe(viewLifecycleOwner, Observer {
            wallpapersList = it
            wallpaperAdapter.wallpapersList = wallpapersList
            wallpaperAdapter.notifyDataSetChanged()
            isLoading = false
        })
    }

    override fun invoke(wallpaper: Wallpaper) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            .setWallpaperImage(wallpaper.image)
        navController?.navigate(action)
    }

}

