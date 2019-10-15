package com.brucetoo.listvideoplay.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brucetoo.listvideoplay.MainActivity;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nd.sdp.bk.video.R;
import com.nd.sdp.video.scrolldetector.ListScrollDetector;
import com.nd.sdp.video.tracker.IViewTracker;
import com.nd.sdp.video.tracker.Tracker;
import com.nd.sdp.video.tracker.VisibleChangeListener;
import com.nd.sdp.video.videomanager.controller.DefaultControllerView;
import com.nd.sdp.video.videomanager.interfaces.PlayerItemChangeListener;
import com.nd.sdp.video.videomanager.interfaces.VideoPlayerListener;
import com.nd.sdp.video.videomanager.meta.DefaultMetaData;
import com.nd.sdp.video.videomanager.player.RatioImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by Bruce Too
 * On 05/04/2017.
 * At 09:28
 */

public class ListSupportFragment extends Fragment implements View.OnClickListener, VisibleChangeListener, PlayerItemChangeListener, VideoPlayerListener {

    public static final String TAG = "ListSupportFragment";
    private ListView mListView;
    private ImageView mImageTop;
    private TextView mTextCalculator;
    private static final float VISIBLE_THRESHOLD = 0.5f;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_support, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list_view);
        QuickAdapter<VideoModel> adapter = new QuickAdapter<VideoModel>(getActivity(), R.layout.item_list_view_new, ListDataGenerater.datas) {
            @Override
            protected void convert(BaseAdapterHelper helper, VideoModel item) {
                RatioImageView imageCover = (RatioImageView) helper.getView(R.id.view_tracker);
                Picasso.with(getActivity())
                    .load(item.coverImage)
                    .into(imageCover);
                imageCover.setRatio(16,9);
                //bind meta data
                imageCover.setTag(R.id.tag_tracker_view,new DefaultMetaData(item.videoUrl));
                imageCover.setOnClickListener(ListSupportFragment.this);
            }
        };
        mListView.setAdapter(adapter);
        mListView.setRecyclerListener(new AbsListView.RecyclerListener() {
            @Override
            public void onMovedToScrapHeap(View view) {
                //the tracker view is moved to scrap,and be re-used,so we need detach view in decor
                IViewTracker tracker = Tracker.getViewTracker(getActivity());
                if(tracker != null) {
                    View trackerView = tracker.getTrackerView();
                    if (trackerView != null && trackerView.equals(view.findViewById(tracker.getTrackerViewId()))) {
                        //TODO Configuration Changed may cause problem
                        Log.e(TAG, "onMovedToScrapHeap -> " + view.findViewById(R.id.view_tracker));
                        Tracker.detach(getActivity());
                    }
                }
            }
        });
        Tracker.addPlayerItemChangeListener(this);
        Tracker.addVideoPlayerListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onMovedToScrapHeap onClick -> " + v);
        if(!Tracker.isSameTrackerView(getActivity(),v)) {
            Tracker.attach(getActivity())
                .trackView(v)
                .into(new ListScrollDetector(mListView))
                .controller(new DefaultControllerView())
                .visibleListener(this);
        }
        ((MainActivity) getActivity()).addDetailFragment();
    }


    @Override
    public void onVisibleChange(float visibleRatio, IViewTracker tracker) {
        Log.e(TAG, "onVisibleChange : edge -> " + tracker.getEdgeString());
        if(!tracker.getFloatLayerView().getVideoPlayerView().isComplete()) {
            if (visibleRatio <= 0.5) {
                tracker.hide();
            } else {
                tracker.show();
            }
        }
    }

    @Override
    public void onPlayerItemChanged(IViewTracker viewTracker) {
        Log.i(TAG, "onPlayerItemChanged ");
    }

    @Override
    public void onVideoSizeChanged(IViewTracker viewTracker, int width, int height) {
        Log.e(TAG, "onVideoSizeChanged");
    }

    @Override
    public void onVideoPrepared(IViewTracker viewTracker) {
        Log.e(TAG, "onVideoPrepared");
    }

    @Override
    public void onVideoCompletion(IViewTracker viewTracker) {
        Log.e(TAG, "onVideoCompletion");
    }

    @Override
    public void onError(IViewTracker viewTracker, int what, int extra) {
        Log.e(TAG, "onError");
    }

    @Override
    public void onBufferingUpdate(IViewTracker viewTracker, int percent) {

    }

    @Override
    public void onVideoStopped(IViewTracker viewTracker) {
        Log.e(TAG, "onVideoStopped");
    }

    @Override
    public void onVideoReset(IViewTracker viewTracker) {

    }

    @Override
    public void onVideoReleased(IViewTracker viewTracker) {

    }

    @Override
    public void onInfo(IViewTracker viewTracker, int what) {
        Log.e(TAG, "onInfo");
    }

    @Override
    public void onVideoStarted(IViewTracker viewTracker) {
        Log.e(TAG, "onVideoStarted");
    }

    @Override
    public void onVideoPaused(IViewTracker viewTracker) {
        Log.e(TAG, "onVideoPaused");
    }
}
