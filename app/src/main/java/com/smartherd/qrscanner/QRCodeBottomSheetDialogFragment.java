package com.smartherd.qrscanner;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class QRCodeBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_BITMAP = "bitmap";

    private ImageView qrCodeImageView;

    public static QRCodeBottomSheetDialogFragment newInstance(Bitmap bitmap) {
        QRCodeBottomSheetDialogFragment fragment = new QRCodeBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BITMAP, bitmap);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_code_bottom_sheet, container, false);
        qrCodeImageView = view.findViewById(R.id.qrCodeImageView);

        if (getArguments() != null) {
            Bitmap bitmap = getArguments().getParcelable(ARG_BITMAP);
            qrCodeImageView.setImageBitmap(bitmap);
        }

        return view;
    }

}
