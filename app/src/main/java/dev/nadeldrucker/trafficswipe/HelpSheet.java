package dev.nadeldrucker.trafficswipe;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpSheet extends Fragment {

    private static class HelpEntryBean {
        int textId;
        int imageId;

        HelpEntryBean(int textId, int imageId) {
            this.textId = textId;
            this.imageId = imageId;
        }
    }

    public HelpSheet() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_sheet, container, false);

        final LinearLayout layout = view.findViewById(R.id.layoutHelpContainer);

        HelpEntryBean[] beans = {
                new HelpEntryBean(R.string.help_circle_description, R.drawable.ic_help),
                new HelpEntryBean(R.string.help_right_description, R.drawable.ic_help),
                new HelpEntryBean(R.string.help_down_description, R.drawable.ic_help),
                new HelpEntryBean(R.string.help_up_description, R.drawable.ic_help)
        };

        for (HelpEntryBean b : beans) {
            View child = inflater.inflate(R.layout.help_entry, layout, false);
            ((TextView) child.findViewById(R.id.helpEntryText)).setText(getResources().getString(b.textId));
            ((ImageView) child.findViewById(R.id.helpEntryImage)).setImageResource(b.imageId);
            layout.addView(child);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.helpBackButton).setOnClickListener(l -> Navigation.findNavController(view).popBackStack());
    }
}
