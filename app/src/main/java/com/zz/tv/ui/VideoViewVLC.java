
package com.zz.tv.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.zz.tv.util.VLCInstance;
import com.zz.tv.util.VLCOptions;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import java.io.File;



public class VideoViewVLC extends SurfaceView
        implements MediaController.MediaPlayerControl {

    public static final String TAG = "VideoViewVLC";
    private MediaPlayer mediaPlayer;
    private boolean mIsAudioTrack;
    private boolean surfaceCreated = false;
    private int width = 0;
    private int height = 0;
    private int sourceVideoWidth;
    private int sourceVideoHeight;

    private static final int STATE_ERROR              = -1;
    private static final int STATE_IDLE               = 0;
    private static final int STATE_PREPARING          = 1;
    private static final int STATE_PREPARED           = 2;
    private static final int STATE_PLAYING            = 3;
    private static final int STATE_PAUSED             = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private int mCurrentState = STATE_IDLE;
    private int mTargetState  = STATE_IDLE;
    private String url;
    private IVLCVout vlcVout;
    public VideoViewVLC(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        getinitSize();
        this.setDrawingCacheEnabled(true);
        mediaPlayer = newMediaPlayer();
        //mediaPlayer.setEventListener();
    }

    private void getinitSize() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(
                        this);
                height = getMeasuredHeight();
                width =  getMeasuredWidth();
                Log.d(TAG, "==== obersver width is " + width);
                Log.d(TAG, "==== observer height is " + height);
            }
        });
    }

    private void changeSurfaceLayout(int visiableWidth,int visiableHeight,int sarNum,int sarDen) {
        if(width == 0) {
            Log.e(TAG, "surface not created(width is 0),this should not happen!");
            return;
        }
        sourceVideoWidth = visiableWidth;
        sourceVideoHeight = visiableHeight;
        Log.d(TAG, "====  width is " + getWidth());
        Log.d(TAG, "==== height is " + getHeight());
        int displayWidth;
        int displayHeigth;
        double radio = (double)width/(double)height;
        double videoSourceRadio = (double)visiableWidth/(double)visiableHeight*((double)sarNum/(double)sarDen);
        if(videoSourceRadio>radio) {
            displayWidth = width;
            displayHeigth = (int) (width/videoSourceRadio);
        } else {
            displayHeigth = height;
            displayWidth = (int) (height*videoSourceRadio);
        }
        Log.d(TAG, "display radio is " + radio);
        Log.d(TAG, "video source radio is " + videoSourceRadio);
        Log.d(TAG, "fianl display is width " + displayWidth + " heidht " + displayHeigth);
        this.setLayoutParams(new RelativeLayout.LayoutParams(displayWidth, displayHeigth));
        this.invalidate();
        //mediaPlayer.getVLCVout().setWindowSize(640, 320);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    private IVLCVout.Callback vlcoutCallback = new IVLCVout.Callback() {
        @Override
        public void onNewLayout(IVLCVout ivlcVout, int width, int height, int visiablWidth, int visiableHeight, int sarNum, int sarDen) {
            Log.d(TAG, "onNewLayout i =" + width + " i1=" + height + " i2=" + visiablWidth + " i3=" + visiableHeight + " i4=" + sarNum + " i5=" + sarDen);
            //changeSurfaceLayout(visiablWidth,visiableHeight,sarNum,sarDen);

        }

        @Override
        public void onSurfacesCreated(IVLCVout ivlcVout) {
            Log.d(TAG,"onSurfacesCreated");
            surfaceCreated = true;
            openVideo();
            if (mTargetState == STATE_PLAYING) {
                start();
            }
        }

        @Override
        public void onSurfacesDestroyed(IVLCVout ivlcVout) {
            Log.d(TAG,"onSurfacesDestroyed");
            surfaceCreated = false;
        }
    };
    private MediaPlayer newMediaPlayer() {
        Log.d(TAG,"newMediaPlayer begin");
        final MediaPlayer mp = new MediaPlayer(VLCInstance.get());
        mp.setEventListener(listener);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        final String aout = VLCOptions.getAout(mSettings);
        if (mp.setAudioOutput(aout) && aout.equals("android_audiotrack")) {
            mIsAudioTrack = true;
        } else {
            mIsAudioTrack = false;
        }
        IVLCVout vlcout = mp.getVLCVout();
        vlcout.setVideoView(this);
        vlcout.addCallback(vlcoutCallback);
        vlcout.attachViews();
        //vlcout.setWindowSize();
        Log.d(TAG, "newMediaPlayer end");
        return mp;
    }



    @Override
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private MediaPlayer.EventListener listener = new MediaPlayer.EventListener() {
        @Override
        public void onEvent(MediaPlayer.Event event) {
            switch (event.type) {
                case MediaPlayer.Event.Opening:
                    Log.i(TAG, "MediaPlayer.Event.Opening");
                    break;
                case MediaPlayer.Event.Playing:
                    Log.i(TAG, "MediaPlayer.Event.Playing");
                    break;
                case MediaPlayer.Event.Paused:
                    Log.i(TAG, "MediaPlayer.Event.Paused");
                    break;
                case MediaPlayer.Event.Stopped:
                    Log.i(TAG, "MediaPlayer.Event.Stopped");
                    break;
                case MediaPlayer.Event.EndReached:
                    Log.i(TAG, "MediaPlayer.Event.EndReached");
                    mCurrentState = STATE_IDLE;
                    if (eventListener != null) {
                        eventListener.onCompleted(mediaPlayer);
                    }
                    break;
                case MediaPlayer.Event.EncounteredError:
                    mCurrentState = STATE_ERROR;
                    if (eventListener != null) {
                        eventListener.onError(mediaPlayer,0,0);
                    }
                    Log.i(TAG, "MediaPlayer.Event.STATE_ERROR");
                    break;
                case MediaPlayer.Event.TimeChanged:
                    break;
                case MediaPlayer.Event.PositionChanged:
                    break;
                case MediaPlayer.Event.Vout:
                    break;
                case MediaPlayer.Event.ESAdded:
                    Log.d(TAG,"===== es added");
                    mCurrentState = STATE_PREPARED;
                    break;
                case MediaPlayer.Event.ESDeleted:
                    break;
                case MediaPlayer.Event.PausableChanged:
                    break;
                case MediaPlayer.Event.SeekableChanged:
                    break;
                default:
                    Log.d(TAG,"===== MediaPlayer event" + event.type);
                    break;
            }
        }
    };
    public void setVideoPath(String path) {
        Log.i(TAG, "setVideoPath " + path);
        url = path;
        openVideo();
    }

    private void openVideo() {
        if (url == null || !surfaceCreated) {
            return;
        }
        final Media media = new Media(VLCInstance.get(), Uri.parse(url));
        //setMediaOptions中设置软解码或硬件解码
        VLCOptions.setMediaOptions(media, getContext(), 0 | VLCOptions.MEDIA_VIDEO);
        //media.setEventListener(listener);
        mediaPlayer.setMedia(media);
        //media.release();
        mCurrentState = STATE_PREPARED;
    }

    public static final int MEDIA_ERROR_UNKNOWN = 1;
    public static final int MEDIA_ERROR_SERVER_DIED = 100;
    public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
    public static final int MEDIA_ERROR_IO = -1004;
    public static final int MEDIA_ERROR_MALFORMED = -1007;
    public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
    public static final int MEDIA_ERROR_TIMED_OUT = -110;

    public static final int MEDIA_INFO_UNKNOWN = 1;
    public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
    public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
    public static final int MEDIA_INFO_METADATA_UPDATE = 802;
    public static final int MEDIA_INFO_EXTERNAL_METADATA_UPDATE = 803;
    public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
    public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;
    public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;

    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;

    private EventListener eventListener;
    public void setEventListener(EventListener listener) {
        eventListener = listener;
    }
    public interface EventListener {
        void onError(MediaPlayer mediaPlayer,int what,int extra);
        void onCompleted(MediaPlayer mediaPlayer);
        void onInfo(MediaPlayer mediaPlayer,int what,int extra);
        void onBufferingUpdate(MediaPlayer mp, int percent);
    }

    @Override
    public void start() {
        if (isInPlaybackState()) {
            Log.d(TAG,"start");
            mediaPlayer.play();
            mCurrentState = STATE_PLAYING;
        }
        mTargetState = STATE_PLAYING;
    }

    @Override
    public void pause() {
        Log.d(TAG,"pause");
        if (isInPlaybackState()) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
            }
        }
        mTargetState = STATE_PAUSED;
    }

    public void stopPlayback() {
        Log.d(TAG, "stopPlayback");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            //mediaPlayer.release();
            //mediaPlayer = null;
            mCurrentState = STATE_IDLE;
            mTargetState  = STATE_IDLE;
            AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    private boolean isInPlaybackState() {
        return (mediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    public void resume() {
        openVideo();
    }

    @Override
    public int getDuration() {
        return (int)mediaPlayer.getLength();
    }

    @Override
    public int getCurrentPosition() {
        //TODO
        return 0;
    }

    @Override
    public void seekTo(int msec) {
        //TODO
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public boolean takeSnapShot(File file) {
        Log.d(TAG, "takeSnapShot begin " + SystemClock.elapsedRealtime());
        boolean ret = mediaPlayer.takeSnapshot(file.getAbsolutePath(),0,sourceVideoWidth,sourceVideoHeight);
        Log.d(TAG,"takeSnapShot end   " + SystemClock.elapsedRealtime());
        Log.d(TAG, "takeSnapShot ret " + ret);
        return ret;
    }
    public boolean recordStart(File f) {
        return mediaPlayer.recordStart(f.getAbsolutePath());
    }

    public void recordStop() {
        mediaPlayer.recordStop();
    }

    public boolean isRecording() {
        return mediaPlayer.isRecording();
    }

    public boolean isRecordable() {
        return mediaPlayer.isRecordable();
    }
}
