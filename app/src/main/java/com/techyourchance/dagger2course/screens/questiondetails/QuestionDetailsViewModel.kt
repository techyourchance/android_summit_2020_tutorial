package com.techyourchance.dagger2course.screens.questiondetails

import androidx.lifecycle.*
import com.techyourchance.dagger2course.questions.FetchQuestionDetailsUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionsUseCase
import com.techyourchance.dagger2course.questions.QuestionWithBody
import com.techyourchance.dagger2course.screens.common.viewmodels.SavedStateViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuestionDetailsViewModel @Inject constructor(
        private val fetchQuestionsUseCase: FetchQuestionsUseCase,
        private val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase
): SavedStateViewModel() {

    sealed class Status {
        object FetchingQuestion: Status()
        data class QuestionFetched(val question: QuestionWithBody): Status()
        object Error: Status()
    }

    private lateinit var _status: MutableLiveData<Status>
    val status: LiveData<Status> get() = _status

    override fun init(savedStateHandle: SavedStateHandle) {
        val status : MutableLiveData<Status> = savedStateHandle.getLiveData("status")
        _status = status
    }

    fun fetchQuestionDetails(questionId: String) {
        if (status.value != null) {
            return
        }
        viewModelScope.launch {
            _status.value = Status.FetchingQuestion
            val result = fetchQuestionDetailsUseCase.fetchQuestion(questionId)
            when(result) {
                is FetchQuestionDetailsUseCase.Result.Success -> {
                    _status.value = Status.QuestionFetched(result.question)
                }
                is FetchQuestionDetailsUseCase.Result.Failure -> {
                    _status.value = Status.Error
                }
            }
        }
    }

}