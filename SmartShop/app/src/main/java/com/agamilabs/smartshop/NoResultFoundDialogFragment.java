package com.agamilabs.smartshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NoResultFoundDialogFragment extends DialogFragment implements View.OnClickListener {
    Button button_scanAgain;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_result_found_scaner_dialog_fragment, null);
        button_scanAgain = view.findViewById(R.id.btn_scanAgain);

        button_scanAgain.setOnClickListener(this);

        setCancelable(false);
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_scanAgain:
                dismiss();
                break;
        }
    }
}
