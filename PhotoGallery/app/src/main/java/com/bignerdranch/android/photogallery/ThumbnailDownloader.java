package com.bignerdranch.android.photogallery;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 1/22/15.
 *
 * using Token for general purpose (in this case we set Token as ImageView)
 */
public class ThumbnailDownloader<Token> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";

    //Message ID
    private static final int MESSAGE_DOWNLOAD=0;

    //local Handler run in background thread
    private Handler mHandler;

    //MainUI thread attached response handler
    Handler mResponseHandler;

    //listener interface to be created/implemented in MainUI, whose callback method will be called by MainUI's responseHandler(with a posted runnable message).
    Listener<Token> mListener;
    public interface Listener<Token>{
        void onThumbnailDownloaded(Token token, Bitmap thumbnail);
    }
    public void setListener(Listener<Token> listener) {
        mListener = listener;
    }

    //Synchronized Map to attach extra url info to Token(ImageView)
    Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());

    // BitMap Cache
    BitMapCache mBitmapCache = new BitMapCache();

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    // init handlers
    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD){
                    @SuppressWarnings("unchecked")
                    Token token = (Token) msg.obj;
                    Log.i(TAG, "Handler Get a Request to URL: " + requestMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    //[!] Called by MainUI to Create a Message in MQ that target for mHandler(requst handler)
    // so be careful for anything this method touch should be synchronised (like requestMap is Synchronised)
    public void queueThumbnail (Token token, String url){
        Log.i(TAG, "Got an URL: " + url);
        requestMap.put(token, url);

        mHandler.obtainMessage(MESSAGE_DOWNLOAD,token).sendToTarget();
    }



    // Note this "final" while passing token
    private void handleRequest(final Token token) {
        try {
            final String url = requestMap.get(token);
            if (url ==null) return;

            // check from image cache
            Bitmap bitmapFromMemCache = mBitmapCache.getBitmapFromMemCache(url);
            if (bitmapFromMemCache==null){
                byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
                bitmapFromMemCache = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                mBitmapCache.addBitmapToMemoryCache(url,bitmapFromMemCache);
                Log.i(TAG, "BitMap created, adding to cache");
            }else {
                Log.i(TAG, "BitMap Found from Cache!");
            }
            final Bitmap bitmap = bitmapFromMemCache;

            // post to mainUI handler for updating
            mResponseHandler.post(new Runnable() {
                // [!] this run posted to mResponseHandler attached to MainUI thread's
                //     and will run in MainUI thread to update UI with on
                @Override
                public void run() {
                    if (requestMap.get(token) != url)
                        return;
                    requestMap.remove(token);
                    mListener.onThumbnailDownloaded(token, bitmap);
                }
            });
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }

    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }
}
