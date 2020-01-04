package com.vocabulary.ui.language

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vocabulary.R
import com.vocabulary.managers.Injector
import com.vocabulary.models.LanguageModel
import com.vocabulary.ui.common.DeletingDialog
import com.vocabulary.ui.common.SwipeHelper
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity(), LanguageAdapter.LanguageClickListener {

    private lateinit var viewModel: LanguageViewModel
    private lateinit var adapter: LanguageAdapter
    private lateinit var swipeHelper: SwipeHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.themeManager.onActivityCreateSetTheme(this)

        setContentView(R.layout.activity_language)

        this.viewModel = ViewModelProviders.of(this).get(LanguageViewModel::class.java)

        this.adapter = LanguageAdapter(this)
        rv_languages.layoutManager = LinearLayoutManager(this)
        rv_languages.adapter = adapter

        this.swipeHelper = object : SwipeHelper(this@LanguageActivity, rv_languages){
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder,
                underlayButtons: java.util.ArrayList<UnderlayButton>
            ) {
                underlayButtons.add(UnderlayButton(
                    getString(R.string.delete),
                    ContextCompat.getColor(this@LanguageActivity, R.color.delete_red),
                    resources.getDimension(R.dimen.swipe_helper_text_size),
                    object : SwipeHelper.UnderlayButtonClickListener{
                        override fun onClick(pos: Int) {
                            showDeletingDialog(adapter.getItemByPosition(pos))
                        }
                    }

                ))

                underlayButtons.add(UnderlayButton(
                    getString(R.string.edit),
                    ContextCompat.getColor(this@LanguageActivity, R.color.edit_green),
                    resources.getDimension(R.dimen.swipe_helper_text_size),
                    object : SwipeHelper.UnderlayButtonClickListener{
                        override fun onClick(pos: Int) {
                            showLanguageDialog(adapter.getItemByPosition(pos))
                        }
                    }

                ))
            }
        }

        btn_back.setOnClickListener { finish() }
        btn_add_language.setOnClickListener { showLanguageDialog() }

        viewModel.apply {
            languages.observe(this@LanguageActivity, Observer<ArrayList<LanguageModel>>{
               adapter.replaceAll(it)
            })
            getLanguages()
        }
    }

    override fun onLanguagePressed(languageModel: LanguageModel) {
        this.viewModel.selectLanguage(languageModel)
        this.adapter.notifyDataSetChanged()
    }

    private fun showDeletingDialog(languageModel: LanguageModel) {
        this.swipeHelper.recoverItems()
        val dialog = DeletingDialog.newInstance(languageModel, object : DeletingDialog.SureingDialogListener {
            override fun onOKPressed(result: () -> Unit) {
                viewModel.deleteLanguage(languageModel) { result.invoke()
                }
            }
        })
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun showLanguageDialog(languageModel: LanguageModel? = null) {
        this.swipeHelper.recoverItems()
        val dialog = LanguageDialog.newInstance(language = languageModel?.name, listener = object : LanguageDialog.LanguageDialogListener {
            override fun onOKPressed(title: String, result: (String?) -> Unit) {
                viewModel.addEditLanguage(languageModel = languageModel, newLanguage = title){ res ->
                    result.invoke(res)
                }
            }
        })
        dialog.show(supportFragmentManager, dialog.tag)
    }
}
