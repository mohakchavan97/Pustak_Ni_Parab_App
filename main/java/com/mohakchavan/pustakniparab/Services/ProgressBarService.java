package com.mohakchavan.pustakniparab.Services;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.mohakchavan.pustakniparab.R;

public class ProgressBarService extends AppCompatDialogFragment {

    private ProgressBar pbs_pb_bar;
    private String progressBarTitle;
//    private ProgressBarServiceListener listener;

    public ProgressBarService(String progressBarTitle) {
        this.progressBarTitle = progressBarTitle;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.progress_bar_service, null);
        pbs_pb_bar = view.findViewById(R.id.pbs_pb_bar);
        builder.setView(view).setTitle(progressBarTitle).setCancelable(false);

//        listener.onBuilderCreated(builder);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
//            listener = (ProgressBarServiceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Must Implement ProgressBarServiceListener");
        }
    }

//    public interface ProgressBarServiceListener {
//        void onBuilderCreated(AlertDialog.Builder builder);
//    }

    public ProgressBar getPbs_pb_bar() {
        return pbs_pb_bar;
    }

    public void setPbs_pb_bar(ProgressBar pbs_pb_bar) {
        this.pbs_pb_bar = pbs_pb_bar;
    }
}
