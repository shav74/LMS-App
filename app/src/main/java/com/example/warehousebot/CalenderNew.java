package com.example.warehousebot;

import androidx.annotation.NonNull;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CalendarView;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Calendar;

public class CalenderNew extends Fragment {

    private TextView title;
    private CalendarView calendarView;
    private EditText lectureData;
    private Button buttonSave;
    String lecDetails = "";
    private DatabaseReference databaseReference;
    String stringDateSelected;
    EditText startTime, endTime;

    TextView lec1Time, lec2Time, lec1Name, lec2Name;

    CardView lecCard1, lecCard2;
    FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calender_new, container, false);

        database = FirebaseDatabase.getInstance();

        calendarView = view.findViewById(R.id.calendarView);
        lectureData = view.findViewById(R.id.editTextLec);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        title = view.findViewById(R.id.calendar_title);
        buttonSave = view.findViewById(R.id.buttonSave);

        startTime = view.findViewById(R.id.start_time);
        endTime = view.findViewById(R.id.end_time);

        lec1Name = view.findViewById(R.id.lec1_name);
        lec2Name = view.findViewById(R.id.lec2_name);
        lec1Time = view.findViewById(R.id.lec1_time);
        lec2Time = view.findViewById(R.id.lec2_time);

        lecCard1 = (CardView) view.findViewById(R.id.lec_card1);
        lecCard2 = (CardView) view.findViewById(R.id.lec_card2);

        if(LecturerData.isIsLecturer()){
            buttonSave.setVisibility(View.VISIBLE);
            startTime.setVisibility(View.VISIBLE);
            endTime.setVisibility(View.VISIBLE);
            lectureData.setVisibility(View.VISIBLE);
        }else{
            lecCard1.setVisibility(View.VISIBLE);
            lecCard2.setVisibility(View.VISIBLE);
        }
        //todo here
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                stringDateSelected = Integer.toString(i) + Integer.toString(i1+1) + Integer.toString(i2);
                calendarClicked();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(stringDateSelected).setValue(lecDetails + lectureData.getText() + "|" + startTime.getText() + "-" + endTime.getText() + "^");
            }
        });

        return view;
    }

    private void calendarClicked(){
        databaseReference.child(stringDateSelected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    lecDetails = (snapshot.getValue().toString());
                    if(lecDetails.length() > 10){
                        try {
                            int lecOneEnd = 0;
                            if(lecDetails.indexOf('^') != -1){
                                lecOneEnd  = lecDetails.indexOf('^');
                                String _lec1Name = lecDetails.substring(0, lecDetails.indexOf('|'));
                                lec1Name.setText(_lec1Name);
                                String _lec1Time = lecDetails.substring(lecDetails.indexOf('|')+1, lecDetails.indexOf('^'));
                                lec1Time.setText(_lec1Time);
                            }else{
                                lec1Name.setText("-");
                                lec1Time.setText("-");
                            }
                            if(lecDetails.indexOf('^') != -1 && lecDetails.lastIndexOf('^') != lecOneEnd){
                                String _lec2Name = lecDetails.substring(lecDetails.indexOf('^')+1,lecDetails.lastIndexOf('|'));
                                lec2Name.setText(_lec2Name);
                                String _lec2Time = lecDetails.substring(lecDetails.lastIndexOf('|'),lecDetails.lastIndexOf('^'));
                                lec2Time.setText(_lec2Time);
                            }else{
                                lec2Name.setText("-");
                                lec2Time.setText("-");
                            }
                        }catch (Exception e){

                        }
                    }
                }else {
                    lectureData.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
