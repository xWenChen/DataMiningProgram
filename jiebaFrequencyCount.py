#! python3.6
# -*- coding: utf-8 -*-
import os, codecs
import jieba
jieba.load_userdict("userdict.txt")
from collections import Counter

jieba.add_word('英雄联盟');
jieba.add_word('守望先锋');
jieba.add_word('DOTA2');
jieba.add_word('荒野行动');
jieba.add_word('季中冠军赛');
jieba.add_word('震中杯');
jieba.add_word('绝地求生');
jieba.add_word('影之诗');
jieba.add_word('第五人格');
jieba.add_word('阴阳师');
jieba.add_word('Ti8');
jieba.add_word('闪电狼');
jieba.add_word('爱萝莉');
jieba.add_word('Dota2');
jieba.add_word('幻想全明星');
jieba.add_word('枪火游侠');
jieba.add_word('MSI');
jieba.add_word('魔兽争霸Ⅲ');
jieba.add_word('炉石传说');
jieba.add_word('决战！平安京');
jieba.add_word('冒险岛2');
jieba.add_word('拳皇命运');
jieba.add_word('皇室战争');
jieba.add_word('AG超玩会');
jieba.add_word('克鲁苏的召唤');
jieba.add_word('非人学园');
jieba.add_word('v社');
jieba.add_word('王者荣耀');
jieba.add_word('剑网3');
jieba.add_word('孤岛惊魂5');
jieba.add_word('一人之下');
jieba.add_word('自由幻想');
jieba.add_word('传奇世界3D');
jieba.add_word('精灵宝可梦');
jieba.add_word('宝可梦GO');
jieba.add_word('洛克人11');
jieba.add_word('灵山奇缘');
jieba.add_word('杀出重围');
jieba.add_word('流放之路');
jieba.add_word('堡垒之夜');
jieba.add_word('300英雄');
jieba.add_word('劲舞团');
jieba.add_word('NBA 2K19');
jieba.add_word('魔兽世界');
jieba.add_word('LOL');
jieba.add_word('血港鬼影');
jieba.add_word('LPL');
jieba.add_word('KPL');
jieba.add_word('PCPI');



def get_words(txt):
    seg_list = jieba.cut(txt)
    c = Counter()
    for x in seg_list:
        if len(x)>1 and x != '\r\n':
            c[x] += 1
    print('常用词频度统计结果')
    for (k,v) in c.most_common(100):
        print('%s%s%d' % (k, '   ', v))

if __name__ == '__main__':
    with codecs.open('newsData.txt', 'r', 'utf8') as f:
        txt = f.read()
    get_words(txt)