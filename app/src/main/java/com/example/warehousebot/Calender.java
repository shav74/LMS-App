package com.example.warehousebot;

import android.app.TimePickerDialog;
import androidx.annotation.NonNull;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CalendarView;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class Calender extends Fragment {

    private FloatingActionButton buttonAddLecTime;
    private TextView title;
    private CalendarView calendarView;
    private EditText lectureData;
    private Button buttonSave;
    private DatabaseReference databaseReference;
    String stringDateSelected;
    EditText startTime, endTime;
    FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calender, container, false);

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
                databaseReference.child(stringDateSelected).setValue(title.getText() + "|" + startTime.getText() + "|" + endTime.getText() + "-");
            }
        });

        return view;
    }

//    private void openCalendarDialog(int _year, int _month, int _date, int _btnId) {
//        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
//                if(_btnId == 1){
//                    startingTime = String.format("%s:%s",String.valueOf(hours), String.valueOf(minutes + 1));
//                } else if (_btnId == 2) {
//                    endingTime = String.format("%s:%s",String.valueOf(hours), String.valueOf(minutes + 1));
//                }
//            }
//        }, 15, 20, true);
//        timePickerDialog.show();
//        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                //todo do what you want with date yyyy mm dd
//                dateAndTime.setText(String.format("%s / %s / %s", String.valueOf(year), String.valueOf(month + 1), String.valueOf(day)));
//                timePickerDialog.show();
//            }
//        }, _year, _month, _date);
//        datePickerDialog.show();
//    }

    private void calendarClicked(){
        databaseReference.child(stringDateSelected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    lectureData.setText(snapshot.getValue().toString());
                }else {
                    lectureData.setText("null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
