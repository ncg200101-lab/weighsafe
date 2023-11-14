package com.weigh.safemobile.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.weigh.safemobile.R;
import com.weigh.safemobile.helpers.AppHelper;

public class PdfViewFragment extends DialogFragment {

    private Button btnClose;
    private PDFView pdfView;

    public PdfViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_pdf_view);
        dialog.setCancelable(false);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setWidthPercent(dialog, 90);

        btnClose = (Button) dialog.findViewById(R.id.btn_close);
        pdfView = (PDFView) dialog.findViewById(R.id.pdfView);

        btnClose.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Medium"));

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (PdfViewFragment.this).getDialog().dismiss();
            }
        });

        pdfView.fromAsset("towmanual.pdf")
                .password(null) // if password protected, then write password
                .defaultPage(0) // set the default page to open
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        Toast.makeText(getActivity(), String.format("Error at page : %s", page), Toast.LENGTH_LONG);
                    }
                })
                .load();

        return dialog;
    }

    private void setWidthPercent(Dialog dialog, int percentage)
    {
        Window window = dialog.getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}