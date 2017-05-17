package habit.duyle.habit.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import habit.duyle.habit.R;

/**
 * Created by Duy Le on 12/19/2016.
 */

public class ProgressBarFragment extends Fragment {
    ProgressBar pb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress_bar_fragment,container,false);
        pb= (ProgressBar) view.findViewById(R.id.progressBar);
        pb.setVisibility(ProgressBar.VISIBLE);
        return view;
    }

}
