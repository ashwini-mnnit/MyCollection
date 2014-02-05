# This is calculate the log of base 10
from __future__ import division 
import sys
import argparse 
import os, fnmatch
import time
import re
import math
from collections import defaultdict
import heapq

def log10(x): 
    return math.log(x) / math.log(10)  

def getTF(x):
    return 1+log10(x)

def getIDF(x, totalDocCount):
    return log10(totalDocCount/x)



#calculate |vector.values()|
def getVectorLen(vectorDic):
    sq_sum=0;
    for val in vectorDic.values():
        sq_sum+=val*val
    return math.sqrt(sq_sum);


#This is to sort the ranking
def getTop50results(docScoreDict):
    heap = [(-value, key) for key,value in docScoreDict.items()]
    largest = heapq.nsmallest(50, heap)
    largest = [(key, -value) for value, key in largest]
    return largest;
 
