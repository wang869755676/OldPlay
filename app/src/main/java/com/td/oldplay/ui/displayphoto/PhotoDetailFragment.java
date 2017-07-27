package com.td.oldplay.ui.displayphoto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by snowbean on 16-6-9.
 */
public class PhotoDetailFragment extends BaseFragment {

    private static final String ARGUMENT_URL = "url";

    @BindView(R.id.photo)
    PhotoView photo;
    @BindView(R.id.loading)
    ProgressBar loading;

    private String mUrl;
    Unbinder unbinder;
    public static PhotoDetailFragment newInstance(final String url) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_URL, url);

        PhotoDetailFragment fragment = new PhotoDetailFragment();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) return;
        mUrl = bundle.getString(ARGUMENT_URL, "null");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void init(View view) {
        GlideUtils.setImage(mActivity, mUrl, photo);
        photo.setOnPhotoTapListener((new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                photo.setScale(1f);
                getActivity().finish();
            }
        }));
    }
}
