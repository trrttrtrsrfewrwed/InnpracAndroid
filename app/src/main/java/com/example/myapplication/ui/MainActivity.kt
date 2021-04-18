package com.example.myapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ActivityMainWideBinding
import com.example.myapplication.utils.getFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // val orientation = resources.configuration.orientation
        val isLandscape: Boolean? = resources?.getBoolean(R.bool.is_landscape)

        if (isLandscape != true) {
            val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val navHostFragment: NavHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController

            val appBarConfiguration = AppBarConfiguration(navController.graph)
            binding.toolbar.setupWithNavController(navController, appBarConfiguration)

            navController.navigateUp()

            val fragment1 = getFragment(CharactersFragment::class.java, navHostFragment) as CharactersFragment?
            if (savedInstanceState != null) {
                fragment1?.setRv(savedInstanceState.get("rv") as Int?)
            }

            val characterId = savedInstanceState?.get("id") as Int?

            if (characterId != null && characterId > 0) {
                navController.navigate(
                    R.id.action_charactersFragment_to_characterQuoteFragment,
                    bundleOf("id" to characterId)
                )
            }
        } else {
            val binding: ActivityMainWideBinding = ActivityMainWideBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val fragmentManager = supportFragmentManager

            (fragmentManager.findFragmentById(R.id.characterQuoteFragment) as CharacterQuoteFragment?)?.start(
                savedInstanceState?.get("id") as Int? ?: -1)

            val fragment1 =  fragmentManager.findFragmentById(R.id.charactersFragment) as CharactersFragment?
            if (savedInstanceState != null) {
                fragment1?.setRv(savedInstanceState.get("rv") as Int?)
            }

            // binding.charactersToolbar.title = "Characters"
            // binding.characterQuotesToolbar.title = "Character quotes"

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val fragmentManager = supportFragmentManager

        val isLandscape: Boolean? = resources?.getBoolean(R.bool.is_landscape)

        val fragment1: CharactersFragment?
        val fragment2: CharacterQuoteFragment?

        if (isLandscape != true) {
            val navHostFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
            fragment1 = getFragment(CharactersFragment::class.java, navHostFragment) as CharactersFragment?

            // It's important to save info for character quote fragment only if it is current fragment in navigation
            fragment2 = navHostFragment?.childFragmentManager?.fragments?.get(0) as? CharacterQuoteFragment?
        } else {
            fragment1 = fragmentManager.findFragmentById(R.id.charactersFragment) as CharactersFragment?
            fragment2 = fragmentManager.findFragmentById(R.id.characterQuoteFragment) as CharacterQuoteFragment?
        }

        if (fragment1 != null) {
            outState.putInt("rv", fragment1.getRv())
        }

        if (fragment2 != null) {
            outState.putInt("id",  fragment2.character_id)
        }
    }
}