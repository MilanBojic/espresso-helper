# Espresso Helper

## What is this?

Espresso helper is powerful wrapper which it make easy to write UI android test. You can simplify your test code just like I mentioned here in this article! You might still need a few additional util functions, but it’s a start!

    
## Requirements

Android 5.0 or later (Minimum SDK level 21)

Android Studio 3.0 (to compile and use)

Eclipse is not supported



## Getting Started
Download Android Studio

Launch Android Studio

Start your new project

Open your project's main Gradle file, in root directory (/build.gradle)

Make sure you are using jcenter() in the repository block (mavenCentral() should work too)

Open your app module Gradle file, for example /app/build.gradle

Click Tools/Android/Sync Project with Gradle Files or click on the Sync icon in the top toolbar

Click Run/Run 'app' to see if it's resolved correctly

This will run the app on your device. You may need to download a newer version of Gradle, which will be available in the Android Studio UI if compile fails


## Using Espresso Helper

In order to use Espresso Helper, you need to add dependency in your build.gradle

**androidTestImplementation** *'com.android.support.test.espresso:espresso-core:3.0.2'*,

{

        exclude group:'com.android.support', module: 'recyclerview-v7'
}

**androidTestImplementation** *'com.android.support.test.uiautomator:uiautomator-v18:2.1.2'*

**implementation** '*com.android.support:recyclerview-v7:+'*


## Example:
**What if we want to check if a view is visible on the screen?**

          Espresso.onView(

                    CoreMatchers.allOf(

                        ViewMatchers.withId(id),

                        ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed())

**We could just write:**

         assertViewId(id:Int)

**What if we want to handle click on view?**

          Espresso.onView(CoreMatchers.allOf<View>(

                        ViewMatchers.withId(id), ViewMatchers.withParent(CoreMatchers.allOf<View>

                                (ViewMatchers.withParent(ViewMatchers.withId(parent)), 

                                ViewMatchers.isDisplayed())))).perform(ViewActions.click())

        
**We could just write**      
  
         clickIdWithParent(id: Int, parent: Int)




**Support**

If you've found an error while using the library, please file an issue. All patches are encouraged, and may be submitted by forking this project and submitting a pull request through GitHub.
