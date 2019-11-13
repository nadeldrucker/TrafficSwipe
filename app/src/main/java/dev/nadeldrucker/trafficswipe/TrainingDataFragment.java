package dev.nadeldrucker.trafficswipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dev.nadeldrucker.trafficswipe.animation.TouchPathView;
import dev.nadeldrucker.trafficswipe.dao.gestures.GestureDao;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class TrainingDataFragment extends Fragment {

    public TrainingDataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        TouchPathView touchPathView = view.findViewById(R.id.trainingTouchPath);
        touchPathView.setTouchPathFinishedListener(touchPaths -> {
            CompletableFuture<String> future = new GestureDao(getContext(), "192.168.178.54:3000").sendData('A', touchPaths);
            future.thenAccept(s -> {
               Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show());
            });
        });
    }

}
