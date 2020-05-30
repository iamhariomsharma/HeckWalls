package com.heckteck.heckwalls.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.heckteck.heckwalls.R
import com.heckteck.heckwalls.models.Wallpaper
import kotlinx.android.synthetic.main.item_wallpaper.view.*

class WallpaperAdapter(
    var wallpapersList: List<Wallpaper>,
    private val clickListener: (Wallpaper) -> Unit
) : RecyclerView.Adapter<WallpaperAdapter.WallpapersViewHolder>() {

    class WallpapersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(wallpaper: Wallpaper, clickListener: (Wallpaper) -> Unit) {
            Glide.with(itemView.context).load(wallpaper.thumbnail)
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
                        itemView.item_single_progress.visibility = View.GONE
                        return false
                    }
                })
                .into(itemView.item_single_img)

            itemView.setOnClickListener {
                clickListener(wallpaper)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpapersViewHolder {
        return WallpapersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_wallpaper,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return wallpapersList.size
    }

    override fun onBindViewHolder(holder: WallpapersViewHolder, position: Int) {
        holder.bind(wallpapersList[position], clickListener)
    }
}