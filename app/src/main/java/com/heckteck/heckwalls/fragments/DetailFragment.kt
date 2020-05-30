package com.heckteck.heckwalls.fragments

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.heckteck.heckwalls.R
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private var image: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image = DetailFragmentArgs.fromBundle(requireArguments()).wallpaperImage

        set_btn.setOnClickListener {
            set_btn.isEnabled = false
            set_btn.text = "Wallpaper Set"
            set_btn.setTextColor(resources.getColor(R.color.colorDark, null))
            val bitmap: Bitmap = detail_image.drawable.toBitmap()
            val task = SetWallpaperTask(requireContext(), bitmap)
            task.execute(true)
        }
    }

    companion object {
        class SetWallpaperTask internal constructor(private val context: Context, private val bitmap: Bitmap) :
            AsyncTask<Boolean, String, String>() {

            override fun doInBackground(vararg params: Boolean?): String {
                val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)
                wallpaperManager.setBitmap(bitmap)
                return "Wallpaper Set"
            }

        }
    }

    override fun onStart() {
        super.onStart()
        if (image != null) {
            Glide.with(requireContext()).load(image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        set_btn.visibility = View.VISIBLE
                        detail_wallpaper_progress.visibility = View.GONE
                        return false
                    }
                })
                .into(detail_image)
        }
    }

    override fun onStop() {
        super.onStop()
        Glide.with(requireContext()).clear(detail_image)
    }

}
