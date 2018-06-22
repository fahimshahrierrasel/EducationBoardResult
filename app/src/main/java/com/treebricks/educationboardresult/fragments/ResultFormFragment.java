package com.treebricks.educationboardresult.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.treebricks.educationboardresult.R;
import com.treebricks.educationboardresult.model.Board;
import com.treebricks.educationboardresult.model.Exam;
import com.treebricks.educationboardresult.model.SubjectResult;
import com.treebricks.educationboardresult.model.Year;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultFormFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView tvCaptcha;
    private Spinner spinnerBoard;
    private Spinner spinnerYear;
    private Spinner spinnerExam;
    private SwipeRefreshLayout refreshLayout;
    private EditText etRollNo;
    private EditText etRegNo;
    private EditText etCaptchaResult;
    private Connection.Response resultFormRequest;
    private HashMap<String, String> stdInfo;
    private ArrayList<SubjectResult> subjectResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout = view.findViewById(R.id.swipe_container);
        refreshLayout.setOnRefreshListener(this);
        tvCaptcha = view.findViewById(R.id.tvCaptcha);
        spinnerBoard = view.findViewById(R.id.spinnerBoard);
        spinnerYear = view.findViewById(R.id.spinnerYear);
        spinnerExam = view.findViewById(R.id.spinnerExam);
        etRollNo = view.findViewById(R.id.etRollNo);
        etRegNo = view.findViewById(R.id.etRegNo);
        etCaptchaResult = view.findViewById(R.id.etCaptchaResult);

        stdInfo = new HashMap<>();
        subjectResults = new ArrayList<>();

        setCaptcha();

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                Year.getExamYears());

        spinnerYear.setAdapter(yearAdapter);


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String examName = spinnerExam.getSelectedItem().toString();
                String year = spinnerYear.getSelectedItem().toString();
                String boardName = spinnerBoard.getSelectedItem().toString();
                String rollNo = etRollNo.getText().toString();
                String regNo = etRegNo.getText().toString();
                String captchaResult = etCaptchaResult.getText().toString();

                String exam = Exam.getExamKeyword(examName);
                String board = Board.getBoardKeyword(boardName);

                if (validateFields())
                    getResult(exam, year, board, rollNo, regNo, captchaResult);

            }
        });
    }

    private boolean validateFields() {
        boolean result = true;

        String rollNumber = etRollNo.getText().toString();
        String regNumber = etRegNo.getText().toString();
        String captcha = etCaptchaResult.getText().toString();

        if (rollNumber.isEmpty()) {
            etRollNo.setError("Roll number can not be empty!");
            result = false;
        }

        if (regNumber.isEmpty()) {
            etRegNo.setError("Registration number can not be empty!");
            result = false;
        }

        if (captcha.isEmpty()) {
            etCaptchaResult.setError("You have to provide Captcha!");
            result = false;
        }

        return result;
    }

    public void setCaptcha() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    resultFormRequest = Jsoup.connect("http://www.educationboardresults.gov.bd/")
                            .timeout(5000)
                            .method(Connection.Method.GET)
                            .execute();

                    Document doc = resultFormRequest.parse();
                    Elements elements = doc.select(".black12bold");
                    Elements tbody = elements.select("tbody");
                    Elements alltr = tbody.select("tr");
                    Elements alltd = alltr.select("td");

                    for (Element td : alltd) {
                        String tdData = td.html();
                        if (tdData.matches("[0-9]*\\s\\+\\s[0-9]*"))
                            builder.append(tdData);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String captcha = builder.toString();
                            tvCaptcha.setText(captcha);
                            if (refreshLayout.isRefreshing())
                                refreshLayout.setRefreshing(false);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getResult(final String exam, final String year, final String board,
                          final String rollNo, final String regNo, final String captchaResult) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Connection.Response responsed = Jsoup.connect("http://www.educationboardresults.gov.bd/result.php")
                            .timeout(5000)
                            .data("sr", "3").data("et", "2")
                            .data("exam", exam).data("year", year)
                            .data("board", board).data("roll", rollNo)
                            .data("reg", regNo).data("value_s", captchaResult)
                            .data("button2", "submit")
                            .cookies(resultFormRequest.cookies())
                            .method(Connection.Method.POST)
                            .execute();
                    Document doc = responsed.parse();
                    Elements elements = doc.select(".black12");
                    if(elements.size() == 2)
                    {
                        setStudentInfo(elements.get(0));
                        setGradeSheet(elements.get(1));
                        showResultFragment();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void showResultFragment() {
        Bundle resultBundle = new Bundle();
        resultBundle.putSerializable("STD_INFO", stdInfo);
        resultBundle.putParcelableArrayList("SUB_RESULT", subjectResults);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_placeholder, ResultFragment.newInstance(resultBundle))
                .addToBackStack(null)
                .commit();
    }

    private void setGradeSheet(Element element) {
        Elements tbody = element.select("tbody");
        Elements alltr = tbody.select("tr");
        Elements alltd = alltr.select("td");

        subjectResults.clear();

        for (int i = 3; i < alltd.size(); i+=3) {
            subjectResults.add(new SubjectResult(alltd.get(i).html(),
                            alltd.get(i+1).html(),
                            alltd.get(i+2).html()));
        }
    }

    private void setStudentInfo(Element element) {
        Elements tbody = element.select("tbody");
        Elements alltr = tbody.select("tr");
        Elements alltd = alltr.select("td");

        stdInfo.clear();

        for (int i = 0; i < alltd.size(); i+=2) {
            stdInfo.put(alltd.get(i).html(), alltd.get(i+1).html());
        }
    }

//    private void getCaptchaResult(String captcha) {
//        Pattern digitPattern = Pattern.compile("\\d+");
//        Matcher matcher = digitPattern.matcher(captcha);
//        int total = 0;
//        while (matcher.find()) {
//            total += Integer.parseInt(matcher.group());
//        }
//        //etCaptchaResult.setText(String.valueOf(total));
//    }

    @Override
    public void onRefresh() {
        setCaptcha();
    }
}
