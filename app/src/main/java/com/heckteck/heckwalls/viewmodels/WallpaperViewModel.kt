package com.heckteck.heckwalls.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.heckteck.heckwalls.models.Wallpaper
import com.heckteck.heckwalls.repository.FirebaseRepository

class WallpaperViewModel : ViewModel() {

    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
    private val wallpapersList: MutableLiveData<List<Wallpaper>> by lazy {
        MutableLiveData<List<Wallpaper>>().also {
            loadWallpapersData()
        }
    }

    fun getWallpapersList(): LiveData<List<Wallpaper>> {
        return wallpapersList
    }

    fun loadWallpapersData() {
        firebaseRepository.queryWallpapers().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result
                if (result!!.isEmpty) {
//                    No more results to load, reached at the bottom of the page
                } else {
                    if (wallpapersList.value == null){
                        wallpapersList.value = result.toObjects(Wallpaper::class.java)
                    }else{
                        wallpapersList.value = wallpapersList.value!!.plus(result.toObjects(Wallpaper::class.java))
                    }

                    val lastItem = result.documents[result.size() - 1]
                    firebaseRepository.lastVisible = lastItem
                }
            } else {
                Log.e("VIEW_MODEL_LOG", "Error: ${it.exception?.message}")
            }
        }
    }

}