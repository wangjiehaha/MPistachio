package com.yn.wj.pistachio.binding.viewadapter.seekbar

import android.databinding.BindingAdapter
import android.widget.SeekBar
import com.yn.wj.pistachio.binding.command.BindingCommand

/**
 *
 *
 * @date Create time：2020/2/21
 */
object ViewAdapter {

    @JvmStatic
    @BindingAdapter(value = ["progressChanged", "startTrack", "stopTrack"], requireAll = false)
    fun addSeekChangeListener(seekBar: SeekBar,
                              changeCommand: BindingCommand<Int>?,
                              startCommand: BindingCommand<Unit>?,
                              stopCommand: BindingCommand<Unit>?) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                changeCommand?.execute(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                startCommand?.execute()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                stopCommand?.execute()
            }
        })
    }
}