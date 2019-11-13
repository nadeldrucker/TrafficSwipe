package dev.nadeldrucker.trafficswipe.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dev.nadeldrucker.trafficswipe.Constants;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.animation.TouchPathView;
import dev.nadeldrucker.trafficswipe.dao.gestures.GestureDao;

import java.util.concurrent.CompletableFuture;

public class TrainingDataFragment extends Fragment {

    private Character currentChar;
    private static final String TAG = "TrainingDataFragment";
    private TextView tvChar;
    private TouchPathView touchPathView;

    public TrainingDataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        final GestureDao gestureDao = new GestureDao(getContext(), Constants.TRAINING_SERVER_HOST);

        tvChar = view.findViewById(R.id.trainingCharTv);
        touchPathView = view.findViewById(R.id.trainingTouchPath);


        tvChar.setText(R.string.fetching);

        view.findViewById(R.id.trainingButtonCancel).setOnClickListener(l -> touchPathView.clearTouchPaths());

        Button btnSubmit = view.findViewById(R.id.trainingButtonSubmit);

        btnSubmit.setOnClickListener(l -> {
            if (touchPathView.getTouchPaths().isEmpty()) {
                Toast.makeText(getContext(), "No character drawn!", Toast.LENGTH_SHORT).show();
                return;
            }

            CompletableFuture<Character> future = gestureDao.sendData(currentChar, touchPathView.getTouchPaths());
            tvChar.setText(R.string.fetching);
            btnSubmit.setEnabled(false);

            future.thenAccept(c -> {
                onNextCharUpdated(c);
                btnSubmit.setEnabled(true);
            });
        });

        gestureDao.getMostNeededCharacter().whenComplete((c, t) -> {
            if (t != null) {
                Log.w(TAG, "Failed while retrieving nextChar", t);
            } else {
                onNextCharUpdated(c);
            }
        });
    }

    private void onNextCharUpdated(Character currentChar){
        this.currentChar = currentChar;
        tvChar.setText(currentChar.toString());
        touchPathView.clearTouchPaths();
    }
}
