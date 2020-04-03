package ru.gonchar17narod.selferificator.ui.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_dashboard.*
import net.galmiza.android.engine.sound.SoundEngine
import ru.gonchar17narod.selferificator.R
import ru.gonchar17narod.selferificator.data.SpectreRepository

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    private val samplingRate = 44100
    private val fftResolution = 1024
    private val continuousRecord =
        SpectreRepository(samplingRate)
    private val soundEngine = SoundEngine().apply {
        initFSin()
    }

    // Buffers
    private var bufferStack = mutableListOf<ShortArray>() // Store trunks of buffers
    private var fftBuffer = ShortArray(fftResolution) // buffer supporting the fft process
    private var re = FloatArray(fftResolution) // buffer holding real part during fft process
    private var im = FloatArray(fftResolution) // buffer holding imaginary part during fft process

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        with(frequencies) {
            setFFTResolution(fftResolution)
            setSamplingRate(samplingRate)
        }
        loadEngine()

        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        startRecording()
    }

    override fun onPause() {
        stopRecording()
        super.onPause()
    }

    override fun onDestroy() {

        // Stop input streaming
        continuousRecord.stop()
        continuousRecord.release()

        super.onDestroy()
    }

    private fun startRecording() =
        continuousRecord.start(
        object : SpectreRepository.OnBufferReadyListener {
            override fun onBufferReady(buffer: ShortArray) {
                getTrunks(buffer)
            }
        }
        )

    private fun stopRecording() =
        continuousRecord.stop()

    private fun loadEngine() =
    with(continuousRecord) {

        // Stop and release recorder if running
        stop()
        release()

        // Prepare recorder
        prepare(fftResolution) // Record buffer size if forced to be a multiple of the fft resolution

        // Build buffers for runtime
        val n = fftResolution

        val l: Int = bufferLength / (n / 2)
        for (i in 0 until l + 1) {  //+1 because the last one has to be used again and sent to first position
            bufferStack.add(ShortArray(n / 2)) // preallocate to avoid new within processing loop
        }
    }

    private fun getTrunks(recordBuffer: ShortArray) {
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

        // Last item has not yet fully be used (only its first half)
        // Move it to first position in arraylist so that its last half is used
        val first: ShortArray = bufferStack.get(0)
        val last: ShortArray = bufferStack.get(bufferStack.size - 1)
        System.arraycopy(last, 0, first, 0, n / 2)
    }

    private fun process() {
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
