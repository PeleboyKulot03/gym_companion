package com.example.gymcompanion.ui.Homepage.fragments.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.Registration.RegistrationPageActivity;
import com.example.gymcompanion.utils.HomeFragmentModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


public class HomeFragment extends Fragment implements IHomeFragment {

    private RecyclerView recyclerView;
    private ImageView customProgressBar;
    private TextView count, addInfo;
    private LinearLayout emptyState;
    private TextView description, day;
    private HomeFragmentPresenter presenter;
    private LinearLayout linearLayout;
    private Context context;
    private int index = 0;
    private ArrayList<String> programs;
    private SimpleDateFormat formatter;
    private Date currentDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (getContext() != null) {
            context = getContext();
        }
        programs = new ArrayList<>();
        programs.add("pushDay");
        programs.add("pullDay");
        programs.add("legDay");
        programs.add("restDay");
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        addInfo = view.findViewById(R.id.addInformation);
        customProgressBar = view.findViewById(R.id.customProgressBar);
        count = view.findViewById(R.id.count);
        emptyState = view.findViewById(R.id.emptyState);
        description = view.findViewById(R.id.description);
        day = view.findViewById(R.id.day);
        linearLayout = view.findViewById(R.id.loader);

        recyclerView = view.findViewById(R.id.recyclerView);

        presenter = new HomeFragmentPresenter(this);
        presenter.getProgram();

        customProgressBar.startAnimation(animation);
        addInfo.setOnClickListener(v -> {
            Intent intent = new Intent(context, RegistrationPageActivity.class);
            intent.putExtra("key", true);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void getProgram(String currentDay, String date) {
        formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        currentDate = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, - 7);
        if (date != null && !date.equals(formatter.format(currentDate))) {
            // Week's program is finish
            // updating the program based on the user's week accuracy and time
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                calendar.add(Calendar.DATE, - 7);
                presenter.checkForNewProgram(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
                return;
            }
            index = programs.indexOf(currentDay);
            if (index == 3) {
                index = 0;
            }
            else {
                index += 1;
            }
            presenter.setNewExercise(programs.get(index), formatter.format(currentDate));
            return;
        }
        presenter.getExercise();

    }

    @Override
    public void getExercise(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date) {
        customProgressBar.clearAnimation();
        linearLayout.setVisibility(View.GONE);
        day.setText(currentDay);

        if (!hasInfo) {
            description.setText(getString(R.string.empty_state_description_1));
            addInfo.setVisibility(View.VISIBLE);
        }
        if (hasInfo && models.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        }
        if (!models.isEmpty()) {
            emptyState.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if (verdict) {
                HomeFragmentAdapter adapter = new HomeFragmentAdapter(context, getActivity(), models);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
            String countValue = finished + "/" + models.size();
            count.setText(countValue);
            count.setVisibility(View.VISIBLE);
            return;
        }

        emptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void setNewExercise(boolean verdict) {
        if (verdict) {
            presenter.getExercise();
        }
    }

    @Override
    public void onGetUpdate(boolean verdict, Map<String, Boolean> isReadyForNewProgram) {
        if (verdict) {
            presenter.updateCurrentProgram(isReadyForNewProgram);
        }
    }

    @Override
    public void onUpdateProgram(boolean verdict) {
        if (verdict) {
            presenter.setNewExercise(programs.get(index), formatter.format(currentDate));
        }
    }
}