package com.vocabulary.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.theme_models.ThemeColorModel
import com.vocabulary.ui.main.MainActivity
import com.vocabulary.ui.common.BaseFragment
import com.vocabulary.ui.common.ProgressDialog
import com.vocabulary.utils.JsonUtils
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.layout_theme_screens.*

class SettingsFragment : BaseFragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var themeAdapter: ThemesColorAdapter
    private var isInitiated = false
    private lateinit var progressDialog: ProgressDialog
    private var isDialogShowed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        this.progressDialog = ProgressDialog()

        this.themeAdapter = ThemesColorAdapter(Injector.themeManager.currentTheme, Injector.themeManager.getThemes(),
            object : ThemesColorAdapter.ThemeColorClickListener {
                override fun colorPressed(theme: ThemeColorModel) {
                    Injector.themeManager.setTheme(theme.theme, activity as MainActivity)
                }
            })
        rv_themes.layoutManager = LinearLayoutManager(contextMain, RecyclerView.HORIZONTAL, false)
        rv_themes.adapter = themeAdapter

        btn_load_json.setOnClickListener {
            jsonLoadPressed()
        }
    }

    private fun jsonLoadPressed() {
        if(!isDialogShowed) {
            isDialogShowed = true
            progressDialog.show(contextMain.supportFragmentManager, progressDialog.tag)
            viewModel.loadJson(JsonUtils.loadJson(contextMain)) {
                isDialogShowed = false
                progressDialog.dismiss()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        psv_settings.stop()
    }

    override fun onResume() {
        super.onResume()
      if(isInitiated) {
          psv_settings.start()
      } else {
          isInitiated = true
      }
    }
}