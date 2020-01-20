package com.vocabulary.ui.settings

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.customViews.ThemeColorView
import com.vocabulary.models.CustomTheme
import com.vocabulary.models.ThemeColorModel
import kotlinx.android.synthetic.main.item_theme_color.view.*

class ThemesColorAdapter(val currentTheme: CustomTheme, val arr: ArrayList<ThemeColorModel>, val listener: ThemeColorClickListener) : RecyclerView.Adapter<ThemesColorAdapter.ThemeColorViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeColorViewHolder {
        return ThemeColorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_theme_color, parent, false))
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: ThemeColorViewHolder, position: Int) {
        val item = arr[position]
        holder.themeColorView.setTheme(item)

        holder.themeColorView.listener {
            listener.colorPressed(item)
        }

        if(item.theme == currentTheme) {
            holder.themeColorView.selectTheme()
        } else {
            holder.themeColorView.unselectTheme()
        }
    }

    class ThemeColorViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val themeColorView: ThemeColorView = v.theme_color_view
    }

    interface ThemeColorClickListener {
        fun colorPressed(theme: ThemeColorModel)
    }

}
