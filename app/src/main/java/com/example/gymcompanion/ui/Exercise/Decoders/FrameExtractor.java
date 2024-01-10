package com.example.gymcompanion.ui.Exercise.Decoders;

import static android.media.MediaCodec.BUFFER_FLAG_END_OF_STREAM;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;


public class FrameExtractor {

    private boolean isTerminated = false;
    private IVideoFrameExtractor listener;
    private int size = 1;
    private int frameCount = Integer.MAX_VALUE;
    private int SDK_VERSION_INT = android.os.Build.VERSION.SDK_INT;
    private boolean verbose = false;
    private int MAX_FRAMES = 0;
    boolean isPortrait = true;
    int savedFrameWidth = 0;
    int savedFrameHeight = 0;
    String TAG = "FrameExtractor";
    int MAX_RESOLUTION = 2000;

    public FrameExtractor(IVideoFrameExtractor listener){
         this.listener = listener;
     }
    /**
     * Terminate the process
     */
    void terminate() {
        isTerminated = true;
    }

    public void extractFrames(String inputFilePath) {
        MediaCodec decoder = null;
        CodecOutputSurface outputSurface = null;
        MediaExtractor extractor = null;
        int width;
        int height;
        try {
            File inputFile = new File(inputFilePath);

            // Check whether the input file exist or not
            if (!inputFile.canRead()) {
                throw new FileNotFoundException("Unable to read $inputFile");
            }

            extractor = new MediaExtractor();
            extractor.setDataSource(inputFile.toString());
            int trackIndex = selectTrack(extractor);
            if (trackIndex < 0) {
                throw new IOException("No video track found in $inputFile");
            }
            extractor.selectTrack(trackIndex);

            // Checking orientation by degree
            MediaFormat format = extractor.getTrackFormat(trackIndex);
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(inputFile.getAbsolutePath());
            int orientation = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));

            // Checking duration by milliseconds
            long duration = Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));


            // We must set MAX_FRAMES for decode
            int FPS = 60;
            if (frameCount == Integer.MAX_VALUE) {
                MAX_FRAMES = (int) (FPS * duration / 1000);
            } else {
                MAX_FRAMES = frameCount;
            }
            height = format.getInteger(MediaFormat.KEY_HEIGHT);
            width = format.getInteger(MediaFormat.KEY_WIDTH);

            if (height > MAX_RESOLUTION || width > MAX_RESOLUTION) {
                float ratio = (float) height / width;
                if (height > width) {
                    height = MAX_RESOLUTION;
                    width = (int) (height / ratio);
                } else {
                    width = MAX_RESOLUTION;
                    height = (int) (ratio * width);
                }
            }
            Log.d(
                    TAG,
                    "$height  h : w  $width"
            );


            // Checking video orientation is portrait or landscape
            isPortrait = orientation == 90 || orientation == 270;
            if (SDK_VERSION_INT >= 21) {
                if (isPortrait) {
                    if (width > height) {
                        savedFrameHeight = width /size;
                    }
                    else {
                        savedFrameHeight = height / size;
                    }

                    if (height < width) {
                        savedFrameWidth = height / size;
                    }
                    else  {
                        savedFrameWidth = width / size;
                    }
                } else {
                    savedFrameHeight = height / size;
                    savedFrameWidth = width / size;
                }
            } else {
                savedFrameHeight = height / size;
                savedFrameWidth = width / size;
            }
            if (verbose) {
                Log.d(TAG, "Video size: " + format.getInteger(MediaFormat.KEY_WIDTH) + "x" + format.getInteger(MediaFormat.KEY_HEIGHT));
            }
            Log.d(TAG, "isPortrait:  $isPortrait" + savedFrameWidth);

            // Could use width/height from the MediaFormat to get full-size frames.
            outputSurface = new CodecOutputSurface(savedFrameWidth, savedFrameHeight, isPortrait);

            // Create a MediaCodec decoder, and configure it with the MediaFormat from the
            // extractor.  It's very important to use the format from the extractor because
            // it contains a copy of the CSD-0/CSD-1 codec-specific data chunks.

            String mime = format.getString(MediaFormat.KEY_MIME);
            decoder = MediaCodec.createDecoderByType(mime);

            decoder.configure(format, outputSurface.getSurface(), null, 0);
            decoder.start();
            doExtract(extractor, trackIndex, decoder, outputSurface);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // release everything we grabbed
            if (outputSurface != null) {
                outputSurface.release();
                outputSurface = null;
            }
            if (decoder != null) {
                decoder.stop();
                decoder.release();
                decoder = null;
            }
            if (extractor != null) {
                extractor.release();
                extractor = null;
            }
        }
    }


    /**
     * Select video tracks
     * Return -1 if no track found
     */
    private int selectTrack(MediaExtractor extractor) {
        // Select the first video track we find, ignore the rest.
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                if (verbose) {
                    Log.d("teeegggg", "Extractor selected track $idx ($mime): $format");
                }
                return i;
            }
        }
        return -1;
    }

    /**
     * Work loop.
     */
    private void doExtract(MediaExtractor extractor, int trackIndex, MediaCodec decoder, CodecOutputSurface outputSurface) throws IOException {
        int TIMEOUT_USEC = 10000;
        ByteBuffer[] decoderInputBuffers = decoder.getInputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int inputChunk = 0;
        int decodeCount = 0;
        long totalSavingTimeNs= 0;
        boolean outputDone = false;
        boolean inputDone = false;
        long presentationTimeUs= 0;

        if (verbose) Log.d(TAG, "Start extract loop...");
        while (!outputDone && !isTerminated) {

            // Feed more data to the decoder.
            if (!inputDone) {
                int inputBufIndex = decoder.dequeueInputBuffer(TIMEOUT_USEC);
                if (inputBufIndex >= 0) {
                    ByteBuffer inputBuf = decoderInputBuffers[inputBufIndex];
                    // Read the sample data into the ByteBuffer.  This neither respects nor
                    // updates inputBuf's position, limit, etc.
                    int chunkSize = extractor.readSampleData(inputBuf, 0);
                    if (chunkSize < 0) {
                        // End of stream -- send empty frame with EOS flag set.
                        decoder.queueInputBuffer(
                                inputBufIndex, 0, 0, 0L,
                                BUFFER_FLAG_END_OF_STREAM
                        );
                        inputDone = true;
                        if (verbose) Log.d(TAG, "sent input EOS");
                    } else {
                        if (extractor.getSampleTrackIndex() != trackIndex) {
                            Log.w(TAG, "WEIRD: got sample from track " + extractor.getSampleTrackIndex() + ", expected " + trackIndex);
                        }
                        presentationTimeUs = extractor.getSampleTime();
                        decoder.queueInputBuffer(
                                inputBufIndex, 0, chunkSize,
                                presentationTimeUs, 0 /*flags*/
                        );
                        if (verbose) {
                            Log.d(TAG, ("submitted frame $inputChunk to dec, size=$chunkSize at $presentationTimeUs"));
                        }
                        inputChunk++;
                        extractor.advance();
                    }
                } else {
                    if (verbose) Log.d(TAG, "input buffer not available");
                }
            }
            if (!outputDone) {
                int decoderStatus = decoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                if (decoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    if (verbose) Log.d(TAG, "no output from decoder available");
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    // not important for us, since we're using Surface
                    if (verbose) Log.d(TAG, "decoder output buffers changed");
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    MediaFormat newFormat = decoder.getOutputFormat();
                    if (verbose) Log.d(TAG, "decoder output format changed: $newFormat");
                } else if (decoderStatus < 0) {
//                    fail("unexpected result from decoder.dequeueOutputBuffer: " + decoderStatus);
                    Log.d(TAG, "doExtract: unexpected result from decoder.dequeueOutputBuffer: $decoderStatus");
                } else { // decoderStatus >= 0
                    if (verbose) Log.d(
                            TAG,
                            ("surface decoder given buffer " + decoderStatus +
                                    " (size=" + info.size + ")")
                    );
                    if (info.flags != 0 && MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                        if (verbose) Log.d(
                                TAG,
                                "output EOS"
                        );
                        outputDone = true;
                    }
                    boolean doRender = (info.size != 0);

                    // As soon as we call releaseOutputBuffer, the buffer will be forwarded
                    // to SurfaceTexture to convert to a texture.  The API doesn't guarantee
                    // that the texture will be available before the call returns, so we
                    // need to wait for the onFrameAvailable callback to fire.
                    decoder.releaseOutputBuffer(decoderStatus, doRender);
                    if (doRender) {
                        if (verbose) Log.d(TAG, "Awaiting decode of frame $decodeCount");
                        outputSurface.awaitNewImage();
                        if (isPortrait) {
                            outputSurface.drawImage(false);
                        } else {
                            outputSurface.drawImage(true);
                        }
                        if (decodeCount < MAX_FRAMES) {
                            long startWhen = System.nanoTime();
                            Frame currentFrame = outputSurface.retrieveFrame(decodeCount, presentationTimeUs);
                            listener.onCurrentFrameExtracted(currentFrame);
                            totalSavingTimeNs += System.nanoTime() - startWhen;
                            if(verbose) Log.d(TAG, "$decodeCount / Max: $MAX_FRAMES");
                        }
                        decodeCount++;
                    }
                }
            }
        }
        int totalSavedFrames = 0;
        if (MAX_FRAMES < decodeCount) {
            totalSavedFrames = MAX_FRAMES;
        }
        else {
            totalSavedFrames = decodeCount;
        }

        if (verbose) Log.d(TAG, ("Total saved frames: $totalSavedFrames  " +
                "| Total time: ${totalSavingTimeNs / 1000000} ms  " +
                "| Each frame took: ${(totalSavingTimeNs / totalSavedFrames / 1000)} us "));

        listener.onAllFrameExtracted(totalSavedFrames, totalSavingTimeNs / 1000000);
    }
}