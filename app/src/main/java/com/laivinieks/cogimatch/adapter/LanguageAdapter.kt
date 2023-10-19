package com.laivinieks.cogimatch.adapter

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.laivinieks.cogimatch.R
import com.laivinieks.cogimatch.utilities.Language
import com.laivinieks.cogimatch.viewmodel.SettingsViewModel
import timber.log.Timber

class LanguageAdapter(
    private val context: Context,
    private val settingsViewModel: SettingsViewModel,
    private val activity: Activity
) :
    RecyclerView.Adapter<LanguageAdapter.MyCustomHolder>() {

    private val languages: Array<Language> = Language.values()
    var selectedPosition = -1

    inner class MyCustomHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val button = view.findViewById<MaterialButton>(R.id.langButton)

        fun bind(language: Int) {

            button.text = context.getString(language)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LanguageAdapter.MyCustomHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.language_button_item, parent, false)
        return MyCustomHolder(view)
    }


    override fun onBindViewHolder(holder: LanguageAdapter.MyCustomHolder, position: Int) {
        val lang = languages[position]
        holder.bind((lang.language))
        holder.itemView.setOnClickListener {

            if (selectedPosition != position) {
                settingsViewModel.setLanguage(lang,activity)
                Timber.tag("language").d("selected language : $lang")
                it.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.pink
                    )
                )

                selectedPosition = position
                notifyDataSetChanged() // RecyclerView'yi güncelle
            }
        }
    }

    override fun getItemCount(): Int {
        return languages.size
    }
}