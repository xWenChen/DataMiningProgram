package com.android.wenchen.dataminingprogram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TxtActivity extends AppCompatActivity
{
	TextView txt;
	Intent intent;

	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_txt);

		txt = (TextView)findViewById(R.id.txt);
		intent = getIntent();

		txt.setText(intent.getStringExtra("txt"));
	}
}
