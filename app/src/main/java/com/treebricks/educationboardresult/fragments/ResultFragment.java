package com.treebricks.educationboardresult.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.treebricks.educationboardresult.R;
import com.treebricks.educationboardresult.model.SubjectResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultFragment extends Fragment {

    private String rollNo, name, board, fatherName, motherName, group, type, dob, result, institute, gpa;
    private HashMap<String, String> stdInfo;
    private ArrayList<SubjectResult> subjectResults;

    // Widgets Declaration START
    private TextView tvResult;

    // Widgets Declaration END

    public static ResultFragment newInstance(Bundle bundle) {
        ResultFragment resultFragment = new ResultFragment();
        resultFragment.setArguments(bundle);
        return resultFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the widgets START
        tvResult = view.findViewById(R.id.textView);

        // Initialize the widgets END

        // Do not touch that code without permission START
        Bundle resultBundle = getArguments();
        if (resultBundle != null) {
            stdInfo = (HashMap<String, String>) resultBundle.getSerializable("STD_INFO");
            subjectResults = resultBundle.getParcelableArrayList("SUB_RESULT");

            if (stdInfo != null) {
                rollNo = stdInfo.get("Roll No");
                name = stdInfo.get("Name");
                board = stdInfo.get("Board");
                fatherName = stdInfo.get("Father's Name");
                motherName = stdInfo.get("Mother's Name");
                group = stdInfo.get("Group");
                type = stdInfo.get("Type");
                dob = stdInfo.get("Date of Birth");
                result = stdInfo.get("Result");
                institute = stdInfo.get("Institute");
                gpa = stdInfo.get("GPA");
            }
        }
        // Do not touch that code without permission END

        // Your Code From here START
        setTemporalView(); // You can remove this. Its for the demo



        // Your Code From here START
    }

    // This can be removed after designing and assigning
    private void setTemporalView() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry e : stdInfo.entrySet()) {
            sb.append(e.getKey());
            sb.append("--");
            sb.append(e.getValue());
            sb.append("\n");
        }

        sb.append("----------------\n");

        for (SubjectResult sr : subjectResults) {
            sb.append(sr.toString());
            sb.append("\n");
        }

        tvResult.setText(sb.toString());
    }
}
