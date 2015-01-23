package com.bignerdranch.android.photogallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by administrator on 1/22/15.
 */
public class PhotoGalleryFragment extends Fragment{
    private static final String TAG = "PhotoGalleryFragment";

    GridView mGridView;
    ArrayList<GalleryItem> mItems;

    //Thumbnail downloader HandlerThread
    ThumbnailDownloader<ImageView> mThumbnailDownloader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);  // retain fragment instance if App is visible, resilient to rotate(but no in background)

        new FetchItemsTask().execute();

        // create a downloader HandlerThread, but set a listener with a MainUI callback.
        mThumbnailDownloader = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailDownloader.setListener(new ThumbnailDownloader.Listener<ImageView>(){
            @Override
            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
                if (isVisible()){
                    imageView.setImageBitmap(thumbnail);
                }
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();  // block and wait for thread initialized
        Log.i(TAG, "Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_photo_gallery,container, false);
        mGridView = (GridView) v.findViewById(R.id.gridView);

        setupAdapter();

        return v;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    void setupAdapter(){
        if (getActivity() == null || mGridView == null) return;

        if (mItems != null) {
            mGridView.setAdapter(new GalleryItemAdapter(mItems));
        } else {
            mGridView.setAdapter(null);
        }
    }


    //region inner classes for BackGround thread fetching images (using AsyncTask)
    //       this is a temporary solution for small things.
    //       For Thumbnails/images, We will further use ThumbnailDownlader (HandlerThread pattern) instead of AsyncTask
    /**
     * Async Background Task
     */
    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<GalleryItem>>{

        // run in background thread
        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params) {
            /*try{
                String result = new FlickrFetchr().getUrl("http://www.google.com");
                Log.i(TAG, "Fetched contents of URL: " + result);
            } catch (IOException ioe){
                Log.e(TAG, "Failed to Fetch  URL: " + ioe);
            }*/

            return new FlickrFetchr().fetchItems();
        }

        // run in MainUI thread
        @Override
        protected void onPostExecute(ArrayList<GalleryItem> galleryItems) {
            mItems = galleryItems;
            setupAdapter();
        }
    }
    //endregion

    //region Adapters
    private class GalleryItemAdapter extends ArrayAdapter<GalleryItem>{

        private GalleryItemAdapter(ArrayList<GalleryItem> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.brian_up_close);

            GalleryItem item = getItem(position);
            mThumbnailDownloader.queueThumbnail(imageView, item.getUrl());
            return convertView;
        }
    }
    //endregion
}
