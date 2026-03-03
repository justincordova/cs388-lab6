# Bottom Navigation Fragment Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Convert Parks and Campground Explorer into a single app with bottom navigation tabs using fragments

**Architecture:** Fragment-based architecture with MainActivity managing fragment transactions through BottomNavigationView. ParksFragment and CampgroundFragment each manage their own RecyclerView, API calls, and data.

**Tech Stack:** Kotlin, Android SDK, Material Components (BottomNavigationView), RecyclerView, AsyncHttpClient, kotlinx.serialization

---

## Task 1: Create ParksFragment Layout

**Files:**
- Create: `app/src/main/res/layout/fragment_parks.xml`

**Step 1: Create fragment_parks.xml layout**

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ParksFragment">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/parks"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:listitem="@layout/item_park" />

</FrameLayout>
```

**Step 2: Commit**

```bash
git add app/src/main/res/layout/fragment_parks.xml
git commit -m "feat: create ParksFragment layout with RecyclerView"
```

---

## Task 2: Create ParksFragment with Network Logic

**Files:**
- Create: `app/src/main/java/com/codepath/lab6/ParksFragment.kt`

**Step 1: Create ParksFragment.kt with all functionality**

```kotlin
package com.codepath.lab6

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "ParksFragment"
private const val API_KEY = BuildConfig.API_KEY
private const val PARKS_URL =
    "https://developer.nps.gov/api/v1/parks?api_key=${API_KEY}"

class ParksFragment : Fragment() {

    private val parks = mutableListOf<Park>()
    private lateinit var parksRecyclerView: RecyclerView
    private lateinit var parksAdapter: ParksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_parks, container, false)

        val layoutManager = LinearLayoutManager(context)
        parksRecyclerView = view.findViewById(R.id.parks)
        parksRecyclerView.layoutManager = layoutManager
        parksRecyclerView.setHasFixedSize(true)
        parksAdapter = ParksAdapter(view.context, parks)
        parksRecyclerView.adapter = parksAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchParks()
    }

    companion object {
        fun newInstance(): ParksFragment {
            return ParksFragment()
        }
    }

    private fun fetchParks() {
        val client = AsyncHttpClient()
        client.get(PARKS_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch parks: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched parks: $json")
                try {
                    val parsedJson = createJson().decodeFromString(
                        ParksResponse.serializer(),
                        json.jsonObject.toString()
                    )
                    parsedJson.data?.let { list ->
                        parks.addAll(list)
                        parksAdapter.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })
    }
}
```

**Step 2: Commit**

```bash
git add app/src/main/java/com/codepath/lab6/ParksFragment.kt
git commit -m "feat: create ParksFragment with API integration"
```

---

## Task 3: Create Park Icon Vector Asset

**Files:**
- Create: `app/src/main/res/drawable/ic_baseline_park_24.xml`

**Step 1: Create park icon vector asset**

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
  <path
      android:fillColor="#222"
      android:pathData="M17.6,14.7c0.7,0 1.3,0.3 1.8,0.7 0.3,-0.6 0.5,-1.2 0.5,-1.9 0,-2.2 -1.8,-4 -4,-4 -0.7,0 -1.3,0.2 -1.8,0.5 -0.9,-1.4 -2.5,-2.3 -4.3,-2.3 -2.9,0 -5.2,2.3 -5.2,5.2 0,0.7 0.1,1.4 0.4,2C4.4,15 4.1,15.6 4.1,16.2c0,1.6 1.3,2.8 2.8,2.8 0.5,0 1,-0.1 1.4,-0.4l1.5,2.1c0.2,0.3 0.6,0.5 1,0.5h2.2c0.4,0 0.8,-0.2 1,-0.5l1.5,-2.1c0.4,0.3 0.9,0.4 1.4,0.4 1.6,0 2.8,-1.3 2.8,-2.8 0,-0.5 -0.1,-1 -0.4,-1.4L17.6,14.7z"/>
</vector>
```

**Step 2: Commit**

```bash
git add app/src/main/res/drawable/ic_baseline_park_24.xml
git commit -m "feat: add park icon vector asset"
```

---

## Task 4: Create Campground Icon Vector Asset

**Files:**
- Create: `app/src/main/res/drawable/ic_baseline_campground_24.xml`

**Step 1: Create campground icon vector asset**

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
  <path
      android:fillColor="#222"
      android:pathData="M19,20h-2v-2h2V20zM19,16h-2v-2h2V16zM19,12h-2v-2h2V12zM21,20h-2v-2h2V20zM21,16h-2v-2h2V16zM21,12h-2v-2h2V12zM12,2L2,12h3v8h6v-6h2v6h6v-8h3L12,2z"/>
</vector>
```

**Step 2: Commit**

```bash
git add app/src/main/res/drawable/ic_baseline_campground_24.xml
git commit -m "feat: add campground icon vector asset"
```

---

## Task 5: Create Bottom Navigation Menu

**Files:**
- Create: `app/src/main/res/menu/bottom_navigation_menu.xml`

**Step 1: Create bottom navigation menu**

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
  <item
      android:id="@+id/nav_parks"
      android:icon="@drawable/ic_baseline_park_24"
      android:title="Parks" />

  <item
      android:id="@+id/nav_campgrounds"
      android:icon="@drawable/ic_baseline_campground_24"
      android:title="Campgrounds" />
</menu>
```

**Step 2: Commit**

```bash
git add app/src/main/res/menu/bottom_navigation_menu.xml
git commit -m "feat: create bottom navigation menu with Parks and Campgrounds"
```

---

## Task 6: Update MainActivity Layout

**Files:**
- Modify: `app/src/main/res/layout/activity_main.xml:1-21`

**Step 1: Replace RecyclerView with FrameLayout and add BottomNavigationView**

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <FrameLayout
        android:id="@+id/main_frame_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintBottom_toTopOf="@+id/bottom_navigation"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:menu="@menu/bottom_navigation_menu" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

**Step 2: Commit**

```bash
git add app/src/main/res/layout/activity_main.xml
git commit -m "refactor: update MainActivity layout for fragment navigation"
```

---

## Task 7: Build and Verify Fragment Setup

**Step 1: Run Gradle build**

```bash
./gradlew assembleDebug
```

**Expected Output:** BUILD SUCCESSFUL

**Step 2: Fix any build errors if present**

Note: The `createJson()` function is defined as a top-level function in MainActivity.kt (line 17-21). Since ParksFragment.kt will be in the same package (com.codepath.lab6), it should be accessible. If there's an error about createJson not being found, you may need to:
- Move the createJson() function to a shared utilities file, or
- Copy it to ParksFragment.kt

Also ensure the `JSON` type is imported from com.codepath.asynchttpclient.callback.JsonHttpResponseHandler (not kotlinx.serialization.json.Json).

**Step 3: Commit any fixes**

```bash
git add .
git commit -m "fix: resolve fragment setup build issues"
```

---

## Task 8: Refactor MainActivity to Use Fragment Navigation

**Context:** At this point, we have ParksFragment created, but MainActivity still contains all the parks code from the original app. We need to remove that code and replace MainActivity with fragment navigation logic.

**Files:**
- Modify: `app/src/main/java/com/codepath/lab6/MainActivity.kt:1-81`

**Step 1: Replace entire MainActivity content with fragment navigation logic**

Important notes about this implementation:
- Removes all parks-related code (API_KEY, PARKS_URL, parks list, RecyclerView setup, networking)
- Uses companion object newInstance() to create fragment instances
- Creates fragment instances once in onCreate() and reuses them (not recreating on each nav click)
- Loads default fragment immediately before setting selectedItemId to avoid blank screen
- When selectedItemId is set, it triggers the OnItemSelectedListener which replaces fragment again

```kotlin
package com.codepath.lab6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.lab6.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var parksFragment: ParksFragment? = null
    private var campgroundFragment: CampgroundFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize fragments
        parksFragment = ParksFragment.newInstance()
        campgroundFragment = CampgroundFragment.newInstance()

        // Load default fragment immediately
        replaceFragment(parksFragment!!)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_parks -> {
                    replaceFragment(parksFragment!!)
                    true
                }
                R.id.nav_campgrounds -> {
                    replaceFragment(campgroundFragment!!)
                    true
                }
                else -> false
            }
        }

        // Set default selection (triggers listener but fragment already loaded)
        bottomNavigationView.selectedItemId = R.id.nav_parks
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
```

**Step 2: Commit**

```bash
git add app/src/main/java/com/codepath/lab6/MainActivity.kt
git commit -m "refactor: MainActivity to use fragment navigation"
```

**Expected Behavior at This Point:**
- App should launch and show the Parks list (from ParksFragment)
- Bottom navigation should be visible with Parks tab selected
- Clicking Campgrounds tab should show campgrounds list
- Clicking back to Parks tab should show parks list (no duplicate loading if fragment instances are preserved)

---

## Task 9: Final Build and Test

**Step 1: Run full Gradle build**

```bash
./gradlew assembleDebug
```

**Expected Output:** BUILD SUCCESSFUL

**Step 2: Fix any build errors**

Check for:
- Missing imports
- Resource reference errors
- Type mismatches
- Duplicate declarations

**Step 3: Test in emulator if needed**

**Step 4: Commit final fixes**

```bash
git add .
git commit -m "fix: resolve final build issues"
```

---

## Task 10: Final Commit All Changes

**Step 1: Run final build to ensure everything works**

```bash
./gradlew clean assembleDebug
```

**Expected Output:** BUILD SUCCESSFUL

**Step 2: Stage all changes**

```bash
git add -A
```

**Step 3: Final commit**

```bash
git commit -m "feat: complete bottom navigation fragment implementation

- Created ParksFragment with RecyclerView and API integration
- Added vector icons for Parks and Campgrounds
- Created bottom navigation menu
- Updated MainActivity layout with FrameLayout and BottomNavigationView
- Implemented fragment navigation in MainActivity
- Successfully merged Parks and Campground Explorer apps
"
```

**Step 4: Verify commit**

```bash
git log --oneline -5
```

---

## Summary

This plan implements bottom navigation fragment architecture by:

1. **Fragment Creation**: Building ParksFragment to match existing CampgroundFragment structure, including lifecycle methods (onCreate, onCreateView, onViewCreated)
2. **UI Assets**: Adding vector icons for navigation items with proper fillColor (#222)
3. **Menu Setup**: Creating bottom navigation menu resource with Parks and Campgrounds items
4. **Layout Updates**: Converting MainActivity to use FrameLayout for fragment hosting and adding BottomNavigationView
5. **Navigation Logic**: Implementing OnItemSelectedListener for fragment transactions with proper fragment instance management
6. **Quality Assurance**: Building after each major feature and fixing errors immediately

**Key Implementation Details:**
- Fragments use companion object newInstance() methods for creation
- Fragment instances created once in MainActivity and reused to preserve state
- Default fragment (Parks) loaded immediately in onCreate to avoid blank screen
- BottomNavigationView.selectedItemId set after listener to trigger default tab
- RecyclerView IDs match lab specifications (@+id/parks)
- JSON type is com.codepath.asynchttpclient.callback.JsonHttpResponseHandler.JSON

The final result is a unified app with two tabs (Parks and Campgrounds) accessible via bottom navigation, with both tabs fully functional with their respective API integrations and RecyclerViews.
