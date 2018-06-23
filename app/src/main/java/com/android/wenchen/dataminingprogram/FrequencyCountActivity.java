package com.android.wenchen.dataminingprogram;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//显示词频统计结果的界面

public class FrequencyCountActivity extends AppCompatActivity
{
	String resultText;
	FrequencyCount count;
	TextView text;

	Handler handler;
	Runnable update;

	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_frequency_count);

		count = new FrequencyCount();
		resultText = "Error";
		text = findViewById(R.id.result_text);
		handler = new Handler();

		//在另外的线程中统计词频
		new Thread(){
			@Override public void run()
			{
				ArrayList<String> words = new ArrayList<>();

				words.add("英雄联盟");
				words.add("守望先锋");
				words.add("DOTA2");
				words.add("荒野行动");
				words.add("季中冠军赛");
				words.add("震中杯");
				words.add("绝地求生");
				words.add("影之诗");
				words.add("第五人格");
				words.add("阴阳师");
				words.add("Ti8");
				words.add("闪电狼");
				words.add("爱萝莉");
				words.add("Dota2");
				words.add("幻想全明星");
				words.add("枪火游侠");
				words.add("MSI");
				words.add("魔兽争霸Ⅲ");
				words.add("炉石传说");
				words.add("决战！平安京");
				words.add("冒险岛2");
				words.add("拳皇命运");
				words.add("皇室战争");
				words.add("AG超玩会");
				words.add("克鲁苏的召唤");
				words.add("非人学园");
				words.add("v社");
				words.add("王者荣耀");
				words.add("剑网3");
				words.add("孤岛惊魂5");
				words.add("一人之下");
				words.add("自由幻想");
				words.add("传奇世界3D");
				words.add("精灵宝可梦");
				words.add("宝可梦GO");
				words.add("洛克人11");
				words.add("灵山奇缘");
				words.add("杀出重围");
				words.add("流放之路");
				words.add("堡垒之夜");
				words.add("300英雄");
				words.add("劲舞团");
				words.add("NBA 2K19");
				words.add("魔兽世界");
				words.add("LOL");
				words.add("血港鬼影");

				//添加词条
				count.addLexicon(words);
				//统计词频
				count.getFrequencyCount();
				//完成任务后删除词条
				count.removeLexicon(words);

				File result = count.getResultFile();

				try
				{
					FileInputStream inputStream = new FileInputStream(result);
					BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

					StringBuilder builder = new StringBuilder("");
					String temp;

					while((temp = br.readLine()) != null)
					{
						builder.append(temp);
					}

					while(builder.toString() != "")
					{
						resultText = builder.toString();
					}

					handler.post(update);
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		};

		update = new Runnable()
		{
			@Override public void run()
			{
				text.setText(resultText);
			}
		};
	}
}
