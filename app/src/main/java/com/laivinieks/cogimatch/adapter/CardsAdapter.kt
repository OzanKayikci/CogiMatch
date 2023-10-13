package com.laivinieks.cogimatch.adapter

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.laivinieks.cogimatch.R
import com.laivinieks.cogimatch.data.entity.Card
import com.laivinieks.cogimatch.utilities.CardUtility
import com.laivinieks.cogimatch.utilities.Constants
import com.laivinieks.cogimatch.viewmodel.CardViewModel

class CardsAdapter(
    private val context: Context,
    private val cardViewModel: CardViewModel,
    cardsList: MutableList<Card>,
    private  val cardSizeMultiplayer:Float

) :
    RecyclerView.Adapter<CardsAdapter.MyCustomHolder>() {

    private val cards = cardsList.toMutableList()


    inner class MyCustomHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView = view.findViewById<ImageView>(R.id.image)
        val cardView: CardView = view.findViewById<CardView>(R.id.card)

        fun bind(image: Int) {
            cardView.layoutParams.height =
                (cardView.layoutParams.height * cardSizeMultiplayer).toInt()

            cardView.layoutParams.width =
                (cardView.layoutParams.width * cardSizeMultiplayer).toInt()

            imageView.setImageResource(image)


        }
    }

    fun closeCard(view: RecyclerView, card: Int) {
        val firstCard = view.findViewHolderForLayoutPosition(card)!!.itemView
        val firstImage = firstCard.findViewById<ImageView>(R.id.image)
        firstCard.isClickable = true
        val drawableBack =
            context!!.resources.getDrawable(R.drawable.card_background, context!!.theme)
        CardUtility.flipAnimation(
            firstCard!! as CardView,
            firstImage!!,
            null,
            drawableBack,
            false
        )

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCustomHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return MyCustomHolder(view)
    }


    override fun onBindViewHolder(holder: MyCustomHolder, position: Int) {
        val currCard = cards[position]
        holder.bind(R.drawable.card_background)

        //get drawables
        val drawableFront = context!!.resources.getDrawable(currCard.image, context!!.theme)
        val drawableBack =
            context!!.resources.getDrawable(R.drawable.card_background, context!!.theme)


        holder.itemView.setOnClickListener {
            CardUtility.flipAnimation(
                holder.cardView,
                holder.imageView,
                drawableFront,
                drawableBack,
                true

            ).also {
                cardViewModel.markAsOpened(currCard)
                holder.itemView.isClickable = false
            }
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }


}