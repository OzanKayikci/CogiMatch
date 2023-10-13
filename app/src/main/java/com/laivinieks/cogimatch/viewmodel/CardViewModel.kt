package com.laivinieks.cogimatch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laivinieks.cogimatch.data.entity.Card
import com.laivinieks.cogimatch.utilities.Stage

class CardViewModel : ViewModel() {

    private var _selectedCard: MutableLiveData<Card?> = MutableLiveData()
    val selectedCard get() = _selectedCard

    private var _cardsList: MutableLiveData<List<Card>> = MutableLiveData()
    val cardsList get() = _cardsList

    private var _currentStage: MutableLiveData<Stage> = MutableLiveData()
    val currentStage get() = _currentStage

    private var _cardSizeMultiplayer: MutableLiveData<Float> = MutableLiveData()
    val cardSizeMultiplayer get() = _cardSizeMultiplayer


    val isRunning: MutableLiveData<Boolean> = MutableLiveData(false)
    val timeRunInMillis: MutableLiveData<Long> = MutableLiveData(0L)

    private var _turn: MutableLiveData<Int> = MutableLiveData(0)
    val turn get() = _turn
    private var _matchingCount: MutableLiveData<Int> = MutableLiveData(0)
    val matchingCount get() = _matchingCount

    fun markAsOpened(card: Card) {
        _selectedCard.postValue(card)

    }

    fun clearSelectedCard() {
        _selectedCard.postValue(null)
    }

    fun setCardList(cards: MutableList<Card>) {
        _cardsList.postValue(cards)
    }

    fun setStage(stage: Stage, cardSizeMultiplayer: Float) {
        _currentStage.postValue(stage)
        _cardSizeMultiplayer.postValue(cardSizeMultiplayer)
    }

    fun updateCounts(isMatching: Boolean) {
        if (isMatching) {
            _matchingCount.postValue(matchingCount.value!! + 1)
        }
        _turn.postValue(turn.value!! + 1)

    }
}