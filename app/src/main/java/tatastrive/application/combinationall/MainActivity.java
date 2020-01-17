package tatastrive.application.combinationall;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener {

        EditText Rollno, Name, Marks, Dob;
        Button Insert, Delete, Update, View, ViewAll, Next;
        SQLiteDatabase db;
        Spinner spinner_edu;
        DatePickerDialog datePickerDialog;
        static String dob_val, edu_val;

        /**
         * Called when the activity is first created.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Rollno = findViewById(R.id.editRollno);
            Name = findViewById(R.id.Name);
            Marks = findViewById(R.id.editMarks);
            Insert = findViewById(R.id.Insert);
            Delete = findViewById(R.id.Delete);
            Update = findViewById(R.id.Update);
            View = findViewById(R.id.View);
            ViewAll = findViewById(R.id.ViewAll);
            Next = findViewById(R.id.Next);
            Dob = findViewById(R.id.date);
            spinner_edu = findViewById(R.id.edu);

            spinner_edu.setOnItemSelectedListener(this);
            Insert.setOnClickListener(this);
            Delete.setOnClickListener(this);
            Update.setOnClickListener(this);
            View.setOnClickListener(this);
            ViewAll.setOnClickListener(this);
            Next.setOnClickListener(this);


            // Creating database and table
            db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR,qual VARCHAR,dob VARCHAR);");
        }

        public void onClick(View view) {

            // Inserting a record to the Student table

            switch (view.getId()) {


                case R.id.Insert: {
                    // Checking for empty fields
                    if (Rollno.getText().toString().trim().length() == 0 ||
                            Name.getText().toString().trim().length() == 0 ||
                            Marks.getText().toString().trim().length() == 0 ||
                            Dob.getText().toString().trim().length() == 0) {
                        showMessage("Error", "Please enter all values");
                        return;
                    }
                    db.execSQL("INSERT INTO student VALUES('" + Rollno.getText() + "','" + Name.getText() +
                            "','" + Marks.getText() + "','" + edu_val +"','" +dob_val +"');");
                    showMessage("Success", "Record added");
                    clearText();
                }
                break;
                // Deleting a record from the Student table
                case R.id.Delete: {
                    // Checking for empty roll number
                    if (Rollno.getText().toString().trim().length() == 0) {
                        showMessage("Error", "Please enter Rollno");
                        return;
                    }
                    Cursor c1 = db.rawQuery("SELECT * FROM student WHERE rollno='" + Rollno.getText() + "'", null);
                    if (c1.moveToFirst()) {
                        db.execSQL("DELETE FROM student WHERE rollno='" + Rollno.getText() + "'");
                        showMessage("Success", "Record Deleted");
                    } else {
                        showMessage("Error", "Invalid Rollno");
                    }
                    clearText();
                }
                break;
                // Updating a record in the Student table

                case R.id.Update: {
                    // Checking for empty roll number
                    if (Rollno.getText().toString().trim().length() == 0) {
                        showMessage("Error", "Please enter Rollno");
                        return;
                    }
                    Cursor c2 = db.rawQuery("SELECT * FROM student WHERE rollno='" + Rollno.getText() + "'", null);
                    if (c2.moveToFirst()) {
                        db.execSQL("UPDATE student SET name='" + Name.getText() + "',marks='" + Marks.getText() +
                                "' WHERE rollno='" + Rollno.getText() + "'");
                        showMessage("Success", "Record Modified");
                    } else {
                        showMessage("Error", "Invalid Rollno");
                    }
                    clearText();
                }
                break;
                // Display a record from the Student table
                case R.id.View: {
                    // Checking for empty roll number
                    if (Rollno.getText().toString().trim().length() == 0) {
                        showMessage("Error", "Please enter Rollno");
                        return;
                    }
                    Cursor c3 = db.rawQuery("SELECT * FROM student WHERE rollno='" + Rollno.getText() + "'", null);
                    if (c3.moveToFirst()) {
                        Name.setText(c3.getString(1));
                        Marks.setText(c3.getString(2));
                    } else {
                        showMessage("Error", "Invalid Rollno");
                        clearText();
                    }
                }
                break;
                // Displaying all the records
                case R.id.ViewAll: {
                    Cursor c4 = db.rawQuery("SELECT * FROM student", null);
                    if (c4.getCount() == 0) {
                        showMessage("Error", "No records found");
                        return;
                    }
                    StringBuffer buffer = new StringBuffer();
                    while (c4.moveToNext()) {
                        buffer.append("Rollno: " + c4.getString(0) + "\n");
                        buffer.append("Name: " + c4.getString(1) + "\n");
                        buffer.append("Marks: " + c4.getString(2) + "\n");
                        buffer.append("Education: " + c4.getString(3) + "\n");
                        buffer.append("DOB: " + c4.getString(4) + "\n\n");

                    }
                    showMessage("Student Details", buffer.toString());
                    break;
                }
                case R.id.Next: {
                   // Intent intent = new Intent(MainActivity.this, SecondPage.class);
                    //startActivity(intent);
                }


            }

        }

        public void showMessage(String title, String message) {
            Builder builder = new Builder(this);
            builder.setCancelable(true);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.show();
        }

        public void clearText() {
            Rollno.setText("");
            Name.setText("");
            Marks.setText("");
            Rollno.requestFocus();
        }

        public void selectdob(android.view.View view) {
            final Calendar c = Calendar.getInstance();
            int myear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    dob_val = dayOfMonth + "/" + (month + 1) + "/" + year;
                    Dob.setText(dob_val);
                }
            }, myear, mMonth, mDay);
            datePickerDialog.show();
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
    edu_val = (String) parent.getItemAtPosition(position);
    Toast.makeText(MainActivity.this, "Education is : " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}