package com.laivinieks.cogimatch.utilities

import com.laivinieks.cogimatch.data.entity.Card
import kotlin.random.Random

object GenerateCardLists {

    fun generateRandomList(size: Int = 4): MutableList<Card> {

        var randomList: MutableList<Int> = mutableListOf()
        val cards = Constants.cards.toMutableList()

        val finalCardList: MutableList<Card> = mutableListOf()
        for (i in 1..size) {
            val randomIndex = Random.nextInt(cards.size)
            randomList!!.add(cards[randomIndex])
            cards.removeAt(randomIndex)

        }
        val copyOfRandomList = randomList.toMutableList()
        val combinedList = (randomList + copyOfRandomList).toMutableList()
        combinedList.shuffle()

        for (i in 0..<combinedList.size) {
            finalCardList.add(Card(i, combinedList[i]))
        }
        return finalCardList
    }
}