package com.techyourchance.dagger2course.screens.questiondetails

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.techyourchance.dagger2course.questions.FetchQuestionDetailsUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionsUseCase
import com.techyourchance.dagger2course.questions.QuestionWithBody
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuestionDetailsViewModel @ViewModelInject constructor(
        private val fetchQuestionsUseCase: FetchQuestionsUseCase,
        private val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase,
        @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    sealed class Status {
        object FetchingQuestion: Status()
        data class QuestionFetched(val question: QuestionWithBody): Status()
        object Error: Status()
    }

    private val _status: MutableLiveData<Status> = savedStateHandle.getLiveData("status")
    val status: LiveData<Status> get() = _status


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