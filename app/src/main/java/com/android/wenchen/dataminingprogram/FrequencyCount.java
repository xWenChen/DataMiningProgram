package com.android.wenchen.dataminingprogram;

import android.os.Environment;

import org.apdplat.word.WordFrequencyStatistics;
import org.apdplat.word.dictionary.DictionaryFactory;
import org.apdplat.word.segmentation.SegmentationAlgorithm;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by WenChen on 2018/6/15.
 */

//自定义的词频统计类

public class FrequencyCount
{
	private String fileName; //待分词的文件
	private File sdCardDir; //文件存储目录

	public FrequencyCount()
	{
		this.sdCardDir = Environment.getExternalStoragePublicDirectory(Environment
				.DIRECTORY_DOCUMENTS);
		this.fileName = sdCardDir + "/" + "newsData.txt";
	}

	public void addLexicon(String word) //自定义添加词条
	{
		DictionaryFactory.getDictionary().add(word);
	}

	public void addLexicon(ArrayList<String> words) //自定义添加词条
	{
		DictionaryFactory.getDictionary().addAll(words);
	}

	public void removeLexicon(String word) //自定义删除词条
	{
		DictionaryFactory.getDictionary().remove(word);
	}

	public void removeLexicon(ArrayList<String> words) //自定义删除词条
	{
		DictionaryFactory.getDictionary().removeAll(words);
	}

	public void getFrequencyCount() //统计词频
	{
		//词频统计设置
		WordFrequencyStatistics wordFrequencyStatistics = new WordFrequencyStatistics();
		//设置移除停用词(类似于 "的" 这样的词)
		wordFrequencyStatistics.setRemoveStopWord(false);
		//设置词频统计结果保存路径
		wordFrequencyStatistics.setResultPath(sdCardDir + "/" + "result.txt");
		//设置分词算法
		wordFrequencyStatistics.setSegmentationAlgorithm(SegmentationAlgorithm.MaxNgramScore);

		//对文件进行分词，将结果保存
		wordFrequencyStatistics.reset();
		wordFrequencyStatistics.seg(sdCardDir + "/" + fileName);
		wordFrequencyStatistics.dump();
	}

	public File getResultFile() //获取结果文件
	{
		return new File(sdCardDir + "/" + "result.txt");
	}
}
