package com.example.myapplication.ui

import android.content.pm.ActivityInfo
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import junit.framework.TestCase
import org.junit.Rule

import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.R
import org.hamcrest.Matchers.allOf
import org.junit.Test;
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest : TestCase() {

    @get:Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(
        MainActivity::class.java
    )
    public override fun setUp() {
        super.setUp()
    }

    @Test
    fun testNav() {
        val mFragmentManager = mActivityRule.activity.supportFragmentManager
        onView(isRoot()).perform(click())
        assertEquals(
            mFragmentManager.findFragmentById(R.id.nav_host_fragment)
                ?.findNavController()?.currentDestination?.id, R.id.characterQuoteFragment)
    }

    @Test
    fun testCorrectNav() {
        val fullName = "Sansa Stark";
        val quote = "I hate the king more than any of them."

        onView(isRoot()).perform(waitId(R.id.fullName, 5000))

        onView(allOf(withId(R.id.fullName), withText(fullName))).perform(click())

        val mFragmentManager = mActivityRule.activity.supportFragmentManager

        assertEquals(
            mFragmentManager.findFragmentById(R.id.nav_host_fragment)
                ?.findNavController()?.currentDestination?.id, R.id.characterQuoteFragment)

        onView(isRoot()).perform(waitId(R.id.quote, 5000))

        onView(allOf(withId(R.id.quote), withText(quote))).check(matches(isDisplayed()))
    }

    @Test
    fun testCorrectOrientationChange() {
        val fullName = "Sansa Stark";
        val quote = "I hate the king more than any of them."

        onView(isRoot()).perform(waitId(R.id.fullName, 5000))

        onView(allOf(withId(R.id.fullName), withText(fullName))).perform(click())

        onView(isRoot()).perform(waitId(R.id.fullName, 5000))

        mActivityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onView(isRoot()).perform(waitId(R.id.quote, 5000))

        onView(allOf(withId(R.id.quote), withText(quote))).check(matches(isDisplayed()))
    }

    @Test
    fun testScrollAndClickLandscape() {
        val fullName = "Sansa Stark";
        val quote = "I hate the king more than any of them."

        mActivityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onView(isRoot()).perform(waitId(R.id.characters_rv, 5000))

        onView(withId(R.id.characters_rv))
            .perform(RecyclerViewActions.scrollToPosition<CharacterViewHolder>(5))

        onView(allOf(withId(R.id.fullName), withText(fullName))).perform(click())

        onView(isRoot()).perform(waitId(R.id.quote, 5000))

        onView(allOf(withId(R.id.quote), withText(quote))).check(matches(isDisplayed()))
    }


    fun testOnSaveInstanceState() {

    }
}