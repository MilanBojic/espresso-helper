package com.espressohelper

import android.app.Activity
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.core.internal.deps.guava.base.Preconditions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import org.hamcrest.*

object EspressoHelpers {

    //select allow or deny object in native permission dialog, ignore case and perform click
    fun handlePermission(permit: String) {
        val device = UiDevice.getInstance()
        val input = permit.substring(0, 1).toUpperCase() + permit.substring(1).toLowerCase()
        val allowPermissions = device.findObject(UiSelector().text(input.trim { it <= ' ' }))
        try {
            allowPermissions.click()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    /*Click Events*/

    //handle click on object with forwarded param
    fun clickId(id: Int) {
        Espresso.onView(Matchers.allOf(ViewMatchers.withId(id), ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
    }

    //handle click on object with forwarded id and parent
    fun clickIdWithParent(id: Int, parent: Int) {
        Espresso.onView(
            CoreMatchers.allOf<View>(
                ViewMatchers.withId(id), ViewMatchers.withParent(
                    CoreMatchers.allOf<View>
                        (ViewMatchers.withParent(ViewMatchers.withId(parent)), ViewMatchers.isDisplayed())
                )
            )
        ).perform(ViewActions.click())
    }

    //handle click with selected desc on object view
    fun clickDescWithParent(desc: String, parent: Int) {
        Espresso.onView(
            CoreMatchers.allOf<View>(
                ViewMatchers.withContentDescription(desc), ViewMatchers.withParent(
                    CoreMatchers.allOf<View>
                        (ViewMatchers.withParent(ViewMatchers.withId(parent)), ViewMatchers.isDisplayed())
                )
            )
        ).perform(
            ViewActions.click()
        )
    }

    //matched object with forwarded descrpition and handle click
    fun clickDesc(text: String) {
        Espresso.onView(Matchers.allOf(ViewMatchers.withContentDescription(text), ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
    }

    //matched text on screen and perform click
    fun clickText(text: String) {
        Espresso.onView(Matchers.allOf(ViewMatchers.withText(text), ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
    }

    //matched displayed object with forwarded id and descrpition, perform click
    fun clickWithIdAndDesc(id: Int, desc: String) {
        Espresso.onView(
            Matchers.allOf<View>(
                ViewMatchers.withId(id),
                ViewMatchers.withText(desc),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())
    }

    //handle click with full matched id, description, and parent of views
    fun clickIdAndDecsWithParent(id: Int, desc: String, parent: Int) {
        Espresso.onView(
            CoreMatchers.allOf<View>(
                ViewMatchers.withId(id), ViewMatchers.withContentDescription(desc), ViewMatchers.withParent(
                    ViewMatchers
                        .withId(parent)
                ), ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())
    }

    //matched displayed item, related of forwarded name
    fun longClickOnListItem(name: String) {
        Espresso.onView(assertTextInListItem(name)).perform(ViewActions.longClick())
    }

    //handle text to EditText dependency of forwarded id.
    fun inputTextToEditField(editTextId: Int, text: String) {
        val appCompatEditText = Espresso.onView(ViewMatchers.withId(editTextId))
        appCompatEditText.perform(ViewActions.replaceText(text), ViewActions.closeSoftKeyboard())
    }


    //functions for assert objects

    //screen contains object with forwarded text
    fun assertText(text: String) {
        Espresso.onView(
            CoreMatchers.allOf(
                ViewMatchers.withText(text),
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    //screen not contains view with forwarded text
    fun doesNotExistText(text: String) {
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withText(text)))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    //screen contains view with forwarded id
    fun assertViewId(id: Int) {
        Espresso.onView(
            CoreMatchers.allOf(
                ViewMatchers.withId(id),
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        ).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed
                    ()
            )
        )
    }

    //screen contains object with forwarded text and id
    fun assertTextWithId(text: String, id: Int) {
        Espresso.onView(
            CoreMatchers.allOf(
                ViewMatchers.withText(text),
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                ViewMatchers.withId(id)
            )
        ).check(
            ViewAssertions.matches(
                ViewMatchers
                    .isDisplayed
                        ()
            )
        )
    }

    //screen contains object with forwarded description
    fun assertDesc(desc: String) {
        Espresso.onView(ViewMatchers.withContentDescription(desc))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    //RecyclerView contains item with forwarded text
    fun assertTextInListItem(itemText: String): Matcher<View> {
        Preconditions.checkArgument(!TextUtils.isEmpty(itemText), "Cannot be null")
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View): Boolean {
                return Matchers.allOf(
                    ViewMatchers.isDescendantOfA(ViewMatchers.isAssignableFrom(RecyclerView::class.java)),
                    ViewMatchers.withText(itemText),
                    ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
                ).matches(item)
            }

            override fun describeTo(description: Description) {
                description.appendText("is descendant of a RecyclerView with text" + itemText)
            }
        }
    }

    //handle native home on device
    fun launchHome(activity: Activity) {
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_HOME)
        activity.startActivity(intent)
    }

    //handle specific activity
    fun goToActivity(activity: Activity) {
        val appContext = InstrumentationRegistry.getTargetContext()
        val intent = Intent(appContext, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        activity.startActivity(intent)
    }

    //block thread in milliseconds
    fun waitFor(millis: Long) {
        Espresso.onView(ViewMatchers.isRoot()).perform(sleep(millis))
    }

    // extra function for block thread
    private fun sleep(millis: Long): ViewAction {

        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $millis milliseconds."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadForAtLeast(millis)
            }
        }
    }


}
