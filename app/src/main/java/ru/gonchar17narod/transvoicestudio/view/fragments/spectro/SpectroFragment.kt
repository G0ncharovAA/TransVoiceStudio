package ru.gonchar17narod.transvoicestudio.view.fragments.spectro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_spectro.*
import ru.gonchar17narod.transvoicestudio.R

class SpectroFragment : Fragment() {

    private lateinit var spectroViewModel: SpectroViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        spectroViewModel =
            ViewModelProvider(this).get(SpectroViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_spectro, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        spectroViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        with(frequencies) {
            setFFTResolution(spectroViewModel.fftResolution)
            setSamplingRate(spectroViewModel.samplingRate)
        }
        spectroViewModel.liveTrunks.observe(
            viewLifecycleOwner,
            Observer {
                getTrunks(it)
            }
        )
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        spectroViewModel.startRecording()
    }

    override fun onPause() {
        spectroViewModel.stopRecording()
        super.onPause()
    }

    private fun getTrunks(recordBuffer: ShortArray) {
        with(spectroViewModel) {
            val n: Int = fftResolution

            // Trunks are consecutive n/2 length samples
            for (i in 0 until bufferStack.size - 1) System.arraycopy(
                recordBuffer,
                n / 2 * i,
                bufferStack.get(i + 1),
                0,
                n / 2
            )

            // Build n length buffers for processing
            // Are build from consecutive trunks
            for (i in 0 until bufferStack.size - 1) {
                System.arraycopy(bufferStack.get(i), 0, fftBuffer, 0, n / 2)
                System.arraycopy(bufferStack.get(i + 1), 0, fftBuffer, n / 2, n / 2)
                process()
            }

            // Last item has not yet fully be used (only its first half)
            // Move it to first position in arraylist so that its last half is used
            val first: ShortArray = bufferStack.get(0)
            val last: ShortArray = bufferStack.get(bufferStack.size - 1)
            System.arraycopy(last, 0, first, 0, n / 2)
        }
    }

    private fun process() {
        with(spectroViewModel) {
            val n = fftResolution
            val log2_n = (Math.log(n.toDouble()) / Math.log(2.0)).toInt()
            soundEngine.shortToFloat(fftBuffer, re, n)
            soundEngine.clearFloat(im, n) // Clear imaginary part

            // Windowing to reduce spectrum leakage
            when (context?.getString(R.string.preferences_window_type_default_value)) {
                "Rectangular" -> soundEngine.windowRectangular(re, n)
                "Triangular" -> soundEngine.windowTriangular(re, n)
                "Welch" -> soundEngine.windowWelch(re, n)
                "Hanning" -> soundEngine.windowHanning(re, n)
                "Hamming" -> soundEngine.windowHamming(re, n)
                "Blackman" -> soundEngine.windowBlackman(re, n)
                "Nuttall" -> soundEngine.windowNuttall(re, n)
                "Blackman-Nuttall" -> soundEngine.windowBlackmanNuttall(re, n)
                "Blackman-Harris" -> soundEngine.windowBlackmanHarris(re, n)
                else -> soundEngine.windowHamming(re, n)
            }

            soundEngine.fft(re, im, log2_n, 0) // Move into frquency domain
            soundEngine.toPolar(re, im, n) // Move to polar base
            frequencies.setMagnitudes(re)
            activity?.runOnUiThread {
                frequencies.invalidate()
            }
        }
    }
}
