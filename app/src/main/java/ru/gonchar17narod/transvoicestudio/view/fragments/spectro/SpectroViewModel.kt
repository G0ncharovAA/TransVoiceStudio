package ru.gonchar17narod.transvoicestudio.view.fragments.spectro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.galmiza.android.engine.sound.SoundEngine
import ru.gonchar17narod.transvoicestudio.data.SpectroRepository

class SpectroViewModel : ViewModel() {

    val samplingRate = 44100
    val fftResolution =
    //1024
        //2048
        4096
    //8192
    private val continuousRecord =
        SpectroRepository(samplingRate)
    val soundEngine = SoundEngine().apply {
        initFSin()
    }

    // Buffers
    var bufferStack = mutableListOf<ShortArray>() // Store trunks of buffers
    var fftBuffer = ShortArray(fftResolution) // buffer supporting the fft process
    var re = FloatArray(fftResolution) // buffer holding real part during fft process
    var im = FloatArray(fftResolution) // buffer holding imaginary part during fft process

    val liveTrunks = MutableLiveData<ShortArray>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is spectro Fragment"
    }
    val text: LiveData<String> = _text

    init {
        loadEngine()
    }

    override fun onCleared() {
        continuousRecord.stop()
        continuousRecord.release()
        super.onCleared()
    }

    fun startRecording() =
        continuousRecord.start(
            { buffer ->
                liveTrunks.postValue(buffer)
            }
        )

    fun stopRecording() =
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
}