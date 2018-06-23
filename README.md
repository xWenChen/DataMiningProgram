# DataMiningProgram
 
题目：门户网站资讯爬取---游戏风云网站热点资讯爬取

数据挖掘爬虫程序：
 
MainActivity：主要实现爬取功能，讲爬取的内容存入文件，格式是文本文档

TxtActivity：显示已爬取的资讯内容
 
其余功能都有想过但是没有实现，使用了Python实现
 
对总的文本进行词频统计：jiebaFrequencyCount.py
 
对每日词频进行统计：jieba2.py
 
爬虫源代码路径： https://github.com/xWenChen/DataMiningProgram/tree/master/app/src/main/java/com/android/wenchen/dataminingprogram
 
dayData.txt：已对资讯按天进行词频统计
 
newsData.txt：去掉爬取时间和资讯发布资讯后的资讯主体文本

newsData1.txt：爬取的原始数据，可以看到爬取日期，爬取时间和资讯的发布时间，大多数资讯都是在当天发布
