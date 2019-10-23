package com.casper.testdrivendevelopment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditBookActivity extends AppCompatActivity {

    private EditText editTextBookName,editTextBookPrice;
    private Button buttonOk,buttonCancel;
    private int insertPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editTextBookName=(EditText)findViewById(R.id.edit_text_name);
        editTextBookPrice=(EditText)findViewById(R.id.edit_text_price);
        buttonOk=(Button) findViewById(R.id.button_ok);
        buttonCancel=(Button)findViewById(R.id.button_cancel);

        editTextBookName.setText(getIntent().getStringExtra("title"));
        editTextBookPrice.setText(getIntent().getDoubleExtra("price",0)+"");
        insertPosition=getIntent().getIntExtra("insert_position", 0);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("title", editTextBookName.getText().toString());
                intent.putExtra("price",Double.parseDouble(editTextBookPrice.getText().toString()));
                intent.putExtra("insert_position", insertPosition);
                setResult(RESULT_OK, intent);
                EditBookActivity.this.finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBookActivity.this.finish();
            }
        });
    }
}
