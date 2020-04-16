package com.gmail.sasha.inverse.transvoicestudio.data

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder.AudioSource

class SpectroRepository(private val samplingRate: Int) {

    // Attributes
    private var audioRecord: AudioRecord? = null
    var bufferLength = 0
        private set
    private var thread: Thread? = null
    private var run = false

    /**
     * Initiate the recording service
     * The service is then ready to start recording
     * The buffer size can be forced to be multiple of @param multiple (size in sample count)
     * @param multiple is ineffective if set to 1
     */
    fun prepare(multiple: Int) {

        // Setup buffer size
        val BYTES_PER_SHORT = 2
        bufferLength = AudioRecord.getMinBufferSize(
            samplingRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ) / BYTES_PER_SHORT

        // Increase buffer size so that it is a multiple of the param
        val r = bufferLength % multiple
        if (r > 0) bufferLength += multiple - r

        // Log value
        //Log.d("ContinuousRecord","Buffer size = "+recordLength+" samples");

        // Init audio recording from MIC
        audioRecord = AudioRecord(
            AudioSource.MIC,
            samplingRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferLength * BYTES_PER_SHORT
        )
    }

    /**
     * Start recording in a independent thread
     * @param listener is call every time a sample is ready
     */
    fun start(
        listener: (ShortArray) -> Unit
    ) {
        if (!run) {
            audioRecord?.let {
                run = true
                //Log.d("ContinuousRecord","Starting service...")
                audioRecord?.startRecording()
                val recordBuffer = ShortArray(bufferLength)
                thread = Thread(Runnable {
                    while (run) {
                        audioRecord?.read(
                            recordBuffer,
                            0,
                            bufferLength
                        )
                        listener.invoke(recordBuffer)
                    }
                }).apply {
                    start()
                }
                //Log.d("ContinuousRecord","Service started")
            }
        }
    }

    /**
     * Stop recording
     * Notifies the thread to stop and wait until it stops
     * Also stops the recording service
     */
    fun stop() {
        if (run) {
            audioRecord?.let {
                run = false
                while (thread?.isAlive ?: false) try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                it.stop()
                //Log.d("ContinuousRecord","Service stopped")
            }
        }
    }

    /**
     * Destroys the recording service
     * @method start and @method stop should then not be called
     */
    fun release() {
        if (!run) {
            audioRecord?.release()
        }
    }
}