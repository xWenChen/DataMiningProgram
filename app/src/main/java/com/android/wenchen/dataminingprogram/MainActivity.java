package com.android.wenchen.dataminingprogram;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
	implements View.OnClickListener
{
	private final int REQUEST_COED = 100;
	private final String FILE_NAME = "/newsData.txt";
	private Button notice, get, show;
	private RandomAccessFile file;

	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		notice = (Button)findViewById(R.id.notice);
		get = (Button)findViewById(R.id.get);
		show = (Button)findViewById(R.id.show);

		int canWrite = ContextCompat.checkSelfPermission(this, Manifest.permission
				.WRITE_EXTERNAL_STORAGE);
		int canRead = ContextCompat.checkSelfPermission(this, Manifest.permission
				.READ_EXTERNAL_STORAGE);

		if(canWrite != PackageManager.PERMISSION_GRANTED || canRead != PackageManager.PERMISSION_GRANTED)
		{
			if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
			{

			}
			else
			{
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
						.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_COED);
			}
		}
		else
		{
			init();
		}
	}

	@Override public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.count_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.count:
			Intent intent = new Intent(MainActivity.this, FrequencyCountActivity.class);
			startActivity(intent);
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		switch(requestCode)
		{
		case REQUEST_COED:
			if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
			{
				init();
			}
			else
			{
				Toast.makeText(this, "请求权限失败，软件无法正常运行", Toast.LENGTH_LONG);
			}
			return;
		}
	}

	private void init()
	{
		//设置提示信息
		notice.setOnClickListener(this);

		//启动爬虫，将数据存入文件中
		//先打开文件，在启动爬虫
		get.setOnClickListener(this);

		//读取文件内容
		show.setOnClickListener(this);
	}

	@Override public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.notice:
			new AlertDialog.Builder(MainActivity.this).setMessage("本软件是一个爬虫APP，可以爬取游戏风云网站的热点资讯模块，"
					+ "并将信息存入本地文件\n\n文档路径：documents/newsData.txt").create().show();
			return;
		case R.id.get:
			//获得当天日期
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
			final String date = formatter.format(Calendar.getInstance().getTime());
			SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
			final String atime = formatter2.format(Calendar.getInstance().getTime());
			//首先打开在外存上的文件
			//如果手机插入了SD卡，并且应用程序具有访问SD卡的权限
			try
			{
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					//获得指定文件的输入流
					File sdCardDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

					File newsFile = new File(sdCardDir + FILE_NAME);
					//以指定文件创建 RandomAccessFile 对象，该对象可以对文件指定内容进行修改
					file = new RandomAccessFile(newsFile, "rw");
					//将文件记录指针移动到最后
					file.seek(newsFile.length());
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
				Toast.makeText(MainActivity.this, "存储文件打开错误，请查看sd卡是否挂载", Toast.LENGTH_SHORT).show();
			}

			//网络操作不能在主线程中，必须新开一个线程
			new Thread()
			{
				@Override public void run()
				{
					super.run();
					try
					{
						//http: post方法
						Document document = Jsoup.connect("http://www.gamefy.cn").data("query", "Java") //设置post的键值对数据
								.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.4843.400 QQBrowser/9.7.13021.400") //设置用户代理，可以判断PC端还是Mobile端
								.cookie("auth", "token") //设置缓存
								.timeout(3000) //设置超市连接
								.post();

						//获取热点新闻的资讯
						Elements newses = document.select("span.news");
						Elements times = document.select("sapn.time");

						//将数据存入文件
						for(int i = 0; i < newses.size(); i++)
						{
							Element news = newses.get(i);
							Element time = times.get(i);

							//存储到文件中一行的格式，爬取日期/爬取时间/新闻/时间
							file.write((date + "/" + atime + "/" + news.text() + "/" + time.text() +
									"\n").getBytes());
						}
						file.write("\n\n".getBytes()); //爬取一次，增加一行空行
						file.close();
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}.start();

			Toast.makeText(MainActivity.this, "爬取信息成功", Toast.LENGTH_SHORT).show();
			return;
		case R.id.show:
			new Thread()
			{
				@Override public void run()
				{
					super.run();

					StringBuilder sb = new StringBuilder("");

					try
					{
						if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
						{
							File sdCardDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
							FileInputStream inputStream = new FileInputStream(sdCardDir + FILE_NAME);
							//将指定输入流包装成BufferedReader，方便读取整行数据
							BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

							String line = null;
							while((line = br.readLine()) != null)
							{
								sb.append(line + "\n");
							}
							br.close();

							Intent intent = new Intent(MainActivity.this, TxtActivity.class);
							intent.putExtra("txt", sb.toString());
							startActivity(intent);
						}
					}
					catch(FileNotFoundException e)
					{
						e.printStackTrace();
						Toast.makeText(MainActivity.this, "存储文件打开错误，请查看文件是否存在", Toast.LENGTH_SHORT).show();
					}
					catch(IOException e)
					{
						e.printStackTrace();
						Toast.makeText(MainActivity.this, "存储文件打开错误，请文件权限分配是否成功", Toast.LENGTH_SHORT).show();
					}
				}
			}.start();
			return;
		default:
			return;
		}
	}
}
