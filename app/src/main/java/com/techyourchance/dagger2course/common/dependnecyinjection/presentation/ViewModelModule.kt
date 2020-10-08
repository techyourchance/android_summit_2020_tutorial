package com.techyourchance.dagger2course.common.dependnecyinjection.presentation

import androidx.lifecycle.ViewModel
import com.techyourchance.dagger2course.screens.questiondetails.QuestionDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(QuestionDetailsViewModel::class)
    abstract fun myViewModel(questionDetailsViewModel: QuestionDetailsViewModel): ViewModel

}