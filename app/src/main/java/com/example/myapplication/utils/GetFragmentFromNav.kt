package com.example.myapplication.utils

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

@Suppress("UNCHECKED_CAST")
fun <F : Fragment> getFragment(fragmentClass: Class<F>, navHostFragment: NavHostFragment?): F? {
    navHostFragment?.childFragmentManager?.fragments?.forEach {
        if (fragmentClass.isAssignableFrom(it.javaClass)) {
            return it as F
        }
    }

    return null
}