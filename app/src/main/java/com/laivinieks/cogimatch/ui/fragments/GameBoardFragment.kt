package com.laivinieks.cogimatch.ui.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.laivinieks.cogimatch.R
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
import javax.inject.Inject

@AndroidEntryPoint
class GameBoardFragment : Fragment() {
    private var _binding: FragmentGameBoardBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPreferences


    private lateinit var cardsAdapter: CardsAdapter
    private lateinit var cardsList: MutableList<Card>
    private var currentStage: Int = 0
    private var stageForScore: Int = 0
    private var totalTime = 0L
    private var timeStarted = 0L
    private var lapTime = 0L
    private var residualTime = 0L
    private var openedCard: Card? = null
    private var strike: Int = 0
    private var matchesCount: Int = 0
    private var turnsCount: Int = 0
    private val cardViewModel: CardViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameBoardBinding.inflate(inflater, container, false)
        val view = binding.root

        setStage(currentStage)
        buttonHandle()
        getObserves()


        return view
    }


    override fun onStart() {
        super.onStart()
        startTimer()
    }

    private fun startTimer() {
        timeStarted = System.currentTimeMillis()
        cardViewModel.isRunning.postValue(true)
    }

    private fun stopTimer() {
        totalTime -= lapTime
        cardViewModel.isRunning.postValue(false)
    }

    private fun buttonHandle() {
        binding.btnMenu.setOnClickListener {
            initPauseGameDialog()
        }
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
            if (Stage.values().last() != it) {
                totalTime += it.totalTime

            }
            cardsList = GenerateCardLists.generateRandomList(it.totalCard / 2)
            stageForScore = currentStage

            Handler(Looper.getMainLooper()).postDelayed({
                initRecycleView(it, cardViewModel.cardSizeMultiplayer.value!!)

                //updateTimer(it.totalTime, from = "change state")
            }, Constants.DELAY_LOW)
        }

        cardViewModel.isRunning.observe(viewLifecycleOwner) {
            Timber.d("$it")
            updateTimer()
        }

        cardViewModel.timeRunInMillis.observe(viewLifecycleOwner) {
            Timber.d("$it")
            if (it < -1) {
                totalTime = 0
                binding.tvTimer.text = "End Of Time"
                navigateToGameOverScreen()
                return@observe
            }
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
            Timber.d("Not Finished")

        }

        cardViewModel.turn.observe(viewLifecycleOwner) {
            matchesCount = cardViewModel.matchingCount.value!!
            turnsCount = it

            binding.totalTurn.text = "$it "
            binding.totalMatch.text = "$matchesCount "



            binding.tvStrike.apply {
                isVisible = strike > 0
                text = "x$strike "
                when (strike) {
                    in 1..2 -> {
                        val white = getColor(requireContext(), R.color.white)
                        setTextColor(white)
                    }

                    in 3..4 -> {
                        val blue = getColor(requireContext(), R.color.blue)
                        setTextColor(blue)
                    }

                    else -> {

                        val pink = getColor(requireContext(), R.color.pink)
                        setTextColor(pink)

                    }
                }
            }
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


    private fun updateTimer() {


        CoroutineScope(Dispatchers.Main).launch {

            while (cardViewModel.isRunning.value!! && totalTime > 0) {

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
                stageForScore = currentStage + 1
                cardViewModel.updateCounts(true)
            }, Constants.DELAY)

        }.also {
            cardViewModel.clearSelectedCard()
            openedCard = null
        }
    }

    private fun initPauseGameDialog() {
        stopTimer()
        val dialogFragment = PauseGameDialogFragment()
        dialogFragment.setOnDialogCanceledCallback {
            Timber.d("run canceled")
            startTimer()
        }
        dialogFragment.setOnPositiveButtonCallback {
            findNavController().navigate(R.id.action_gameBoardFragment_to_mainMenuFragment)
        }
        dialogFragment.show(childFragmentManager, PauseGameDialogFragment.TAG)
    }

    private fun navigateToGameOverScreen() {
        val bundle = bundleOf("values" to intArrayOf(turnsCount, matchesCount, stageForScore))
        findNavController().navigate(R.id.action_gameBoardFragment_to_gameOverFragment, bundle)


    }

    override fun onStop() {
        super.onStop()
        stopTimer()
    }

    private fun writeDataToSharedPref() {
        val score = CardUtility.scoreCalculator(turnsCount, matchesCount, stageForScore)
        val oldScore = sharedPref.getInt(Constants.ARCADE_SCORE, 0)
        val currentScore = if (oldScore > score) oldScore else score
        sharedPref.edit().putInt(Constants.ARCADE_SCORE, currentScore).apply()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                initPauseGameDialog()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, callback
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        writeDataToSharedPref()
        _binding = null
    }
}