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
import com.vocabulary.ui.common.SwipeHelper
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity(), SwipeLanguageClickListener {

    private lateinit var viewModel: LanguageViewModel
    private lateinit var adapter: LanguageAdapter
//    private lateinit var swipeHelper: SwipeHelper

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

//        this.swipeHelper = object : SwipeHelper(this@LanguageActivity, rv_languages){
//            override fun instantiateUnderlayButton(
//                viewHolder: RecyclerView.ViewHolder,
//                underlayButtons: java.util.ArrayList<UnderlayButton>
//            ) {
//                underlayButtons.add(UnderlayButton(
//                    getString(R.string.delete),
//                    ContextCompat.getColor(this@LanguageActivity, R.color.color_new_delete),
//                    resources.getDimension(R.dimen.swipe_helper_text_size),
//                    object : SwipeHelper.UnderlayButtonClickListener{
//                        override fun onClick(pos: Int) {
//                            showDeletingDialog(adapter.getItemByPosition(pos))
//                        }
//                    }
//
//                ))
//
//                underlayButtons.add(UnderlayButton(
//                    getString(R.string.edit),
//                    ContextCompat.getColor(this@LanguageActivity, R.color.color_new_edit),
//                    resources.getDimension(R.dimen.swipe_helper_text_size),
//                    object : SwipeHelper.UnderlayButtonClickListener{
//                        override fun onClick(pos: Int) {
//                            showLanguageDialog(adapter.getItemByPosition(pos))
//                        }
//                    }
//
//                ))
//            }
//        }

        btn_back.setOnClickListener { finish() }
        btn_add_language.setOnClickListener { showLanguageDialog() }

        viewModel.apply {
            isLoading.observe(this@LanguageActivity, Observer<Boolean>{
                this@LanguageActivity.pb_languages.visibility = View.VISIBLE
                this@LanguageActivity.view_empty_languages.visibility = View.GONE
                this@LanguageActivity.rv_languages.visibility = View.GONE
            })
            languages.observe(this@LanguageActivity, Observer<ArrayList<LanguageModel>>{
                adapter.replaceAll(it)
                initList(it)
            })
            getLanguages()
            this@LanguageActivity.pb_languages.visibility = View.VISIBLE
        }
    }

    private fun initList(arr: ArrayList<LanguageModel>) {
        if(arr.isNotEmpty()) {
            this.pb_languages.visibility = View.GONE
            this.view_empty_languages.visibility = View.GONE
            this.rv_languages.visibility = View.VISIBLE
        } else {
            this.pb_languages.visibility = View.GONE
            this.rv_languages.visibility = View.GONE
            this.view_empty_languages.visibility = View.VISIBLE
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

//    override fun onLanguagePressed(languageModel: LanguageModel) {
//        this.viewModel.selectLanguage(languageModel)
//        this.adapter.notifyDataSetChanged()
//    }

    private fun showDeletingDialog(languageModel: LanguageModel) {
//        this.swipeHelper.recoverItems()
        val dialog = DeletingDialog.newInstance(languageModel, object : DeletingDialog.SureingDialogListener {
            override fun onOKPressed(result: () -> Unit) {
                viewModel.deleteLanguage(languageModel) {
                    result.invoke()
                }
            }
        })
        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun showLanguageDialog(languageModel: LanguageModel? = null) {
//        this.swipeHelper.recoverItems()
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
