package com.vocabulary.ui.language

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vocabulary.R
import com.vocabulary.customViews.EmptyListMessageView
import com.vocabulary.customViews.swipeable_view.OnSwipeTouchListener
import com.vocabulary.customViews.swipeable_view.SwipeLanguageClickListener
import com.vocabulary.managers.Injector
import com.vocabulary.models.LanguageModel
import com.vocabulary.ui.common.DeletingDialog
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity(), SwipeLanguageClickListener {

    private lateinit var viewModel: LanguageViewModel
    private lateinit var adapter: LanguageAdapter
    private var isDialogOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.themeManager.onActivityCreateSetTheme(this)

        setContentView(R.layout.activity_language)

        this.viewModel = ViewModelProviders.of(this).get(LanguageViewModel::class.java)

        this.view_empty_languages.initView(EmptyListMessageView.ListType.ADD_LANGUAGES)
        this.view_empty_languages.btnClickListener { showLanguageDialog() }


        this.adapter = LanguageAdapter(this)
        rv_languages.layoutManager = LinearLayoutManager(this)
        rv_languages.adapter = adapter
        object : OnSwipeTouchListener(this, rv_languages){}

        btn_back.setOnClickListener {
            viewModel.checkIfNeedUpdate()
            finish()
        }
        btn_add_language.setOnClickListener { showLanguageDialog() }

        viewModel.apply {
            languages.observe(this@LanguageActivity, Observer<ArrayList<LanguageModel>>{
                adapter.replaceAll(it)
            })
            viewState.observe(this@LanguageActivity,
                Observer<LanguageViewModel.LanguageInitType>{
                initView(it)
            })
            getLanguages()
            this@LanguageActivity.pb_languages.visibility = View.VISIBLE
        }
    }

    private fun initView(type: LanguageViewModel.LanguageInitType) {
        when(type) {
            LanguageViewModel.LanguageInitType.LANGUAGES_LOADING -> {
                this.pb_languages.visibility = View.VISIBLE
                this.view_empty_languages.visibility = View.GONE
                this.rv_languages.visibility = View.GONE
            }
            LanguageViewModel.LanguageInitType.LANGUAGES_EMPTY -> {
                this.pb_languages.visibility = View.GONE
                this.rv_languages.visibility = View.GONE
                this.view_empty_languages.visibility = View.VISIBLE
            }
            LanguageViewModel.LanguageInitType.LANGUAGES_NOT_EMPTY -> {
                this.pb_languages.visibility = View.GONE
                this.view_empty_languages.visibility = View.GONE
                this.rv_languages.visibility = View.VISIBLE
            }
        }
    }

    override fun onViewPressed(languageModel: LanguageModel) {
        this.viewModel.selectLanguage(languageModel)
        this.adapter.notifyDataSetChanged()
    }

    override fun onEditPressed(languageModel: LanguageModel) {
        showLanguageDialog(languageModel)
    }

    override fun onDeletePressed(languageModel: LanguageModel) {
        showDeletingDialog(languageModel)
    }

    private fun showDeletingDialog(languageModel: LanguageModel) {
        val dialog = DeletingDialog.newInstance(languageModel, object : DeletingDialog.DeletingDialogListener {
            override fun onOKPressed(result: () -> Unit) {
                viewModel.deleteLanguage(languageModel) {
                    result.invoke()
                }
            }
        })
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun showLanguageDialog(languageModel: LanguageModel? = null) {
        if(!isDialogOpened) {
            val dialog = LanguageDialog.newInstance(
                language = languageModel?.name,
                listener = object : LanguageDialog.LanguageDialogListener {
                    override fun onOKPressed(title: String, result: (String?) -> Unit) {
                        viewModel.addEditLanguage(
                            languageModel = languageModel,
                            newLanguage = title
                        ) { res ->
                            result.invoke(res)
                        }
                    }

                    override fun onClose() {
                        isDialogOpened = false
                    }
                })
            dialog.show(supportFragmentManager, dialog.tag)
            isDialogOpened = true
        }
    }

    override fun onBackPressed() {
        viewModel.checkIfNeedUpdate()
        finish()
    }
}
