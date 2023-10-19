package com.laivinieks.cogimatch.utilities

import com.laivinieks.cogimatch.R
import java.util.Locale


object Constants {


    const val MAX_SIZE_MULTIPLAYER = 1.8f
    const val MED_SIZE_MULTIPLAYER = 1.4f
    const val MIN_SIZE_MULTIPLAYER = 0.8f
    const val CURRENT_SIZE_MULTIPLAYER = 1f

    const val DELAY = 800L
    const val DELAY_LOW = 300L
    const val ANIMATION_DURATION = 400L

    const val rewardTime = 3000L
    const val TIMER_UPDATE_INTERVAL = 80L

    const val SHARED_PREFERENCES_NAME = "SharedPref"
    const val ARCADE_SCORE = "ARCADE_SCORE"

    const val DEF_VOLUME = 0.4f
    const val MUSIC_VOLUME = "MUSIC_VOLUME"
    const val SFX_VOLUME = "SFX_VOLUME"
    const val LANGUAGE = "LANGUAGE"

     var DEFAULT_LANGUAGE = Locale.getDefault().language

    val cards = listOf(
        R.drawable.cute_archer_mimic_octopus,
        R.drawable.cute_black_mage_leaf_tailed_gecko,
        R.drawable.cute_dark_knight_sloth_bear,
        R.drawable.cute_engineer_mantis,
        R.drawable.cute_rogue_sword_billed_hummingbird_connect_sword,
        R.drawable.cute_tech_soldier_aye_aye,
        R.drawable.guard_platypus,
        R.drawable.ninja_narwhal,
        R.drawable.speedy_sloths,
        R.drawable.wizard_axolotl,
        R.drawable.cute_hooded_seal_barbarian,
        R.drawable.cute_monk_lemur,
        R.drawable.cute_berserker_thorny_dragon,
        R.drawable.cute_dark_wizard_jellyfish,
        R.drawable.cute_knight_batfish,
        R.drawable.cute_knight_armadillo,
        R.drawable.cute_monk_okapai,
        R.drawable.cute_sniper_quokka,
        R.drawable.cute_submarine_glaucus_atlanticus,
        R.drawable.cute_archer_lyrebird,
        R.drawable.cute_old_tarsier_wizard
    )
}