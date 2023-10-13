package com.laivinieks.cogimatch.ui.fragments


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.laivinieks.cogimatch.adapter.CardsAdapter
import com.laivinieks.cogimatch.data.entity.Card
import com.laivinieks.cogimatch.databinding.FragmentGameBoardBinding
import com.laivinieks.cogimatch.utilities.CardUtility
import com.laivinieks.cogimatch.utilities.Constants
import com.laivinieks.cogimatch.utilities.GenerateCardLists
import com.laivinieks.cogimatch.utilities.Stage
import com.laivinieks.cogimatch.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class GameBoardFragment : Fragment() {
    private var _binding: FragmentGameBoardBinding? = null
    private val binding get() = _binding!!
    private lateinit var cardsAdapter: CardsAdapter
    private lateinit var cardsList: MutableList<Card>
    private var currentStage: Int = 0
    private var totalTime = 0L
    private var timeStarted = 0L
    private var lapTime = 0L
    private var residualTime = 0L
    private var openedCard: Card? = null
    private var strike: Int = 0
    private val cardViewModel: CardViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameBoardBinding.inflate(inflater, container, false)
        val view = binding.root

        setStage(currentStage)
        getObserves()


        return view
    }

    override fun onStart() {
        super.onStart()
        timeStarted = System.currentTimeMillis()
        cardViewModel.isRunning.postValue(true)
    }

    private fun initRecycleView(stage: Stage, sizeMultiplayer: Float) {
        val gridLayoutManager = GridLayoutManager(
            requireContext(),
            stage.columns,
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.cardsRecycleView.layoutManager = gridLayoutManager
        binding.cardsRecycleView.setHasFixedSize(true)
        cardsAdapter =
            CardsAdapter(requireContext(), cardViewModel, cardsList, sizeMultiplayer)
        binding.cardsRecycleView.adapter = cardsAdapter
    }


    private fun getObserves() {
        cardViewModel.currentStage.observe(viewLifecycleOwner) {
            currentStage = it.ordinal
            totalTime += it.totalTime
            cardsList = GenerateCardLists.generateRandomList(it.totalCard / 2)

            Handler(Looper.getMainLooper()).postDelayed({
                initRecycleView(it, cardViewModel.cardSizeMultiplayer.value!!)

                //updateTimer(it.totalTime, from = "change state")
            }, Constants.DELAY_LOW)
        }

        cardViewModel.isRunning.observe(viewLifecycleOwner) {
            updateTimer(from = "observer")
        }

        cardViewModel.timeRunInMillis.observe(viewLifecycleOwner) {
            val formattedTime = CardUtility.getFormattedStopWatchTime(it, true)
            binding.tvTimer.text = formattedTime
        }

        cardViewModel.selectedCard.observe(viewLifecycleOwner) { selectedCard ->
            if (openedCard == null) {
                openedCard = selectedCard
                return@observe
            }
            if (selectedCard != null) {
                compareCards(selectedCard)
            }

        }
        cardViewModel.cardsList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {

                if (currentStage < Stage.values().size - 1) {
                    setStage(Stage.values()[currentStage + 1].ordinal)

                } else {
                    setStage(Stage.values()[currentStage].ordinal)
                }

                return@observe
            }
            Log.d("Not Finished", it.toString())

        }

        cardViewModel.turn.observe(viewLifecycleOwner) {
            binding.totalTurn.text = it.toString()
            binding.totalMatch.text = cardViewModel.matchingCount.value!!.toString()
        }

    }

    private fun setStage(stageNumber: Int) {
        val cardsSizeMultiplayer =
            CardUtility.sizeConvertorFromDimension(
                requireContext(),
                Stage.values()[stageNumber].sizeMultiplayer
            )

        cardViewModel.setStage(Stage.values()[stageNumber], cardsSizeMultiplayer)

    }


    private fun updateTimer(time: Long = 0, from: String) {


        Timber.tag(from)
            .d(" :$residualTime - $totalTime  - $lapTime - ${cardViewModel.isRunning.value}")

        CoroutineScope(Dispatchers.Main).launch {

            while (cardViewModel.isRunning.value!! && totalTime > 0) {
                Timber.tag(from)
                    .d(" :$residualTime - $totalTime  - $lapTime - ${cardViewModel.isRunning.value}")
                lapTime = System.currentTimeMillis() - timeStarted
                cardViewModel.timeRunInMillis.postValue(totalTime - lapTime)
                delay(Constants.TIMER_UPDATE_INTERVAL)

            }
            //totalTime -= lapTime

        }
    }

    private fun hideCards(firstCard: Int, secondCard: Int) {


        updateCardList(firstCard, secondCard)
        CardUtility.explodeAnimation(
            binding.cardsRecycleView.findViewHolderForLayoutPosition(
                firstCard
            )!!.itemView as CardView
        )
        CardUtility.explodeAnimation(
            binding.cardsRecycleView.findViewHolderForLayoutPosition(
                secondCard
            )!!.itemView as CardView
        )
    }

    private fun updateCardList(firstCard: Int, secondCard: Int) {
        cardsList.remove(cardsList.find { x -> x.id == firstCard })
        cardsList.remove(cardsList.find { x -> x.id == secondCard })
        cardViewModel.setCardList(
            cardsList
        )
    }

    private fun compareCards(selectedCard: Card) {
        CardUtility.compareCards(openedCard!!, selectedCard) { isMatched ->
            val openedCardId = openedCard!!.id
            val selectedCardId = selectedCard!!.id
            if (!isMatched) {
                Handler(Looper.getMainLooper()).postDelayed({
                    cardsAdapter.closeCard(
                        binding.cardsRecycleView,
                        openedCardId,

                        )
                    cardsAdapter.closeCard(
                        binding.cardsRecycleView,
                        selectedCardId,

                        )
                    strike = 0
                    cardViewModel.updateCounts(false)

                }, Constants.DELAY)

                return@compareCards
            }

            Handler(Looper.getMainLooper()).postDelayed({
                hideCards(selectedCardId, openedCardId)
                // updateTimer(strike * Constants.rewardTime, from = "from strike")
                residualTime = Constants.rewardTime * strike
                totalTime += residualTime
                strike++
                cardViewModel.updateCounts(true)
            }, Constants.DELAY)

        }.also {
            cardViewModel.clearSelectedCard()
            openedCard = null
        }
    }


    override fun onStop() {
        super.onStop()
        cardViewModel.isRunning.postValue(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}