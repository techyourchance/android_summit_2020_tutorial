package com.techyourchance.dagger2course.screens.questiondetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techyourchance.dagger2course.screens.common.ScreensNavigator
import com.techyourchance.dagger2course.screens.common.activities.BaseActivity
import com.techyourchance.dagger2course.screens.common.dialogs.DialogsNavigator
import com.techyourchance.dagger2course.screens.common.viewsmvc.ViewMvcFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuestionDetailsVMActivity : BaseActivity(), QuestionDetailsViewMvc.Listener  {

    @Inject lateinit var screensNavigator: ScreensNavigator
    @Inject lateinit var dialogsNavigator: DialogsNavigator
    @Inject lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var mQuestionDetailsViewModel: QuestionDetailsViewModel

    private lateinit var viewMvc: QuestionDetailsViewMvc

    private lateinit var questionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewMvc = viewMvcFactory.newQuestionDetailsViewMvc(null)
        setContentView(viewMvc.rootView)

        questionId = intent.extras!!.getString(EXTRA_QUESTION_ID)!!

        mQuestionDetailsViewModel = ViewModelProvider(this).get(QuestionDetailsViewModel::class.java)
        mQuestionDetailsViewModel.status.observe(this, Observer {
            when(it) {
                is QuestionDetailsViewModel.Status.FetchingQuestion -> {
                    viewMvc.showProgressIndication()
                }
                is QuestionDetailsViewModel.Status.QuestionFetched -> {
                    viewMvc.hideProgressIndication()
                    viewMvc.bindQuestionWithBody(it.question)
                }
                is QuestionDetailsViewModel.Status.Error -> {
                    viewMvc.hideProgressIndication()
                    dialogsNavigator.showServerErrorDialog()
                }
            }
        })
        mQuestionDetailsViewModel.fetchQuestionDetails(questionId)
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
    }

    override fun onBackClicked() {
        screensNavigator.navigateBack()
    }

    companion object {
        const val EXTRA_QUESTION_ID = "EXTRA_QUESTION_ID"
        fun start(context: Context, questionId: String) {
            val intent = Intent(context, QuestionDetailsVMActivity::class.java)
            intent.putExtra(EXTRA_QUESTION_ID, questionId)
            context.startActivity(intent)
        }
    }
}