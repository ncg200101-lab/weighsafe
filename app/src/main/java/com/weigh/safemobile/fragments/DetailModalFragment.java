package com.weigh.safemobile.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.helpers.AppHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetailModalFragment extends DialogFragment {
    @BindView(R.id.btn_detail_modal_close)
    Button btnClose;
    @BindView(R.id.img_detail)
    ImageView imgDetail;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.rl_modal)
    RelativeLayout rl_modal;


    private static final String ARG_PARAM1 = "TITLE";
    private static final String ARG_PARAM2 = "IMAGE_RESOURCE";

    private String title;
    private int imgResource;
    private Unbinder unbinder;
    private Dialog mDialog;

    public static DetailModalFragment newInstance(String title, int imgResource) {
        DetailModalFragment fragment = new DetailModalFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, title);
        args.putInt(ARG_PARAM2, imgResource);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            imgResource = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_detail_modal, new LinearLayout(getActivity()), false);
        unbinder = ButterKnife.bind(this, view);
        Dialog builder = new Dialog(getContext());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setContentView(view);
        if (title != null && !title.isEmpty() && !"".equals(title))
            textTitle.setText(title);

        setupImageSize(view);
        imgDetail.setImageResource(imgResource);
        mDialog = builder;
        setTypeFaces();
        return builder;
    }


    public void setupImageSize(View view){
        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),imgResource, dimensions);
        int height = dimensions.outHeight;
        int width =  dimensions.outWidth;
        float xPro = (float) width / height;
        float yPro = (float) height / width;
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int rl_width = rl_modal.getWidth();
                int img_height = (int )(rl_width * yPro+0.50001);
                imgDetail.getLayoutParams().height = img_height;
                imgDetail.requestLayout();

            }
        });
    }
    private void setTypeFaces() {
        textTitle.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        btnClose.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
    }

    @OnClick(R.id.btn_detail_modal_close)
    public void onClickCloseModal(){
           mDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
