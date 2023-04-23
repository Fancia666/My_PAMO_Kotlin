// MainActivityFragment.java
// MainActivityFragment creates and manages a CannonView
package com.example.tipper.cannon

import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R

class CannonGame : Fragment() {
    private var cannonView // custom view to display the game
            : CannonView? = null

    // called when Fragment's view needs to be created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // inflate the fragment_main.xml layout
        val view = inflater.inflate(R.layout.fragment_cannon_game, container, false)

        // get a reference to the CannonView
        cannonView = view.findViewById<View>(R.id.cannonView) as CannonView
        cannonView!!.setCannonGame(this)
        return view
    }

    // set up volume control once Activity is created
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // allow volume buttons to set game volume
        requireActivity().volumeControlStream = AudioManager.STREAM_MUSIC
    }

    // when MainActivity is paused, terminate the game
    override fun onPause() {
        super.onPause()
        cannonView!!.stopGame() // terminates the game
        NavHostFragment.findNavController(this@CannonGame)
            .navigate(R.id.action_cannon_to_options)
    }

    // when MainActivity is paused, MainActivityFragment releases resources
    override fun onDestroy() {
        super.onDestroy()
        cannonView!!.releaseResources()
    }
}