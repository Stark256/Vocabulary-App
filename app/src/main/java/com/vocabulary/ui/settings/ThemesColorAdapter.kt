package com.vocabulary.ui.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.customViews.ThemeColorView
import com.vocabulary.models.theme_models.CustomTheme
import com.vocabulary.models.theme_models.ThemeColorModel
import kotlinx.android.synthetic.main.item_theme_color.view.*

class ThemesColorAdapter(val currentTheme: CustomTheme,
                         val arr: ArrayList<ThemeColorModel>,
                         val listener: ThemeColorClickListener)
    : RecyclerView.Adapter<ThemesColorAdapter.ThemeColorViewHolder>() {


    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeColorViewHolder {
        this.context = parent.context
        return ThemeColorViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_theme_color, parent, false))
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: ThemeColorViewHolder, position: Int) {
        val item = arr[position]
        holder.themeColorView.setTheme(item)
        holder.themeColorText.text = context.getString(item.nameRes)

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
        val themeColorText: TextView = v.tv_theme_name
    }

    interface ThemeColorClickListener {
        fun colorPressed(theme: ThemeColorModel)
    }

}
