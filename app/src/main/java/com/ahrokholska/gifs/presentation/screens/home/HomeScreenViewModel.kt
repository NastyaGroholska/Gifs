package com.ahrokholska.gifs.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ahrokholska.gifs.domain.useCase.DeleteGifUseCase
import com.ahrokholska.gifs.domain.useCase.GetPaginatedDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getPaginatedDataUseCase: GetPaginatedDataUseCase,
    private val deleteGifUseCase: DeleteGifUseCase
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val gifs = _searchQuery.flatMapLatest { query ->
        getPaginatedDataUseCase(query)
    }.cachedIn(viewModelScope)

    fun searchChanged(query: String) {
        viewModelScope.launch {
            _searchQuery.update { query }
        }
    }

    fun deleteGif(id: String) {
        viewModelScope.launch {
            deleteGifUseCase(id)
        }
    }
}

