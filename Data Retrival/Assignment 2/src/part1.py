'''
Created on Feb 20th, 2014

@author: Ashwini Singh
'''
from __future__ import division
 
import sys
import argparse
from collections import defaultdict
from  tweet import *
import cjson
import numpy as np
import matplotlib.pyplot as plt
import heapq
import math


GlobalHubScore=defaultdict(float) # [user:hub_score]
GlobalAuthorityScore=defaultdict(float)#[user:auth_score]

error=0.0000000000001

def massageHubAuthScore(processingNodeCount):
    for hub in GlobalHubScore:
        GlobalHubScore[hub]=GlobalHubScore[hub]*3/math.sqrt(processingNodeCount)
    for auth in GlobalAuthorityScore:
        GlobalAuthorityScore[auth]=GlobalAuthorityScore[auth]*3/math.sqrt(processingNodeCount)
        

def getHubSum(Graph,authnode,oldHubscore):
    retSum=0
    for succ in Graph.predecessors_iter(authnode):
        retSum=retSum+oldHubscore[succ];
    return retSum

def getAuthSum(Graph,hubnode,OldAuthscore):
    retSum=0
    for pred in Graph.successors_iter(hubnode):
        retSum=retSum+OldAuthscore[pred];
    return retSum
  
def getTop20results(docScoreDict):
    heap = [(-value, key) for key,value in docScoreDict.items()]
    largest = heapq.nsmallest(20, heap)
    largest = [(key, -value) for value, key in largest]
    return largest;
       

def shouldConverge(oldHubScore,oldAuthScore):
    for hub in GlobalHubScore:
        if((GlobalHubScore[hub]-oldHubScore[hub])>error):
            return False
    for auth in GlobalAuthorityScore:
        if((GlobalAuthorityScore[auth]-oldAuthScore[auth])>error):
            return False
    return True

def processHubAuthority(processGraph):
    iteration=0
    while(True):
        tmpHubScore=GlobalHubScore.copy()
        tmpAuthScore=GlobalAuthorityScore.copy() 
        
        #update authority
        for auth in GlobalAuthorityScore:
            GlobalAuthorityScore[auth]=getHubSum(processGraph,auth,tmpHubScore)
        #update hubscore
        for hub in  GlobalHubScore:
            GlobalHubScore[hub]=getAuthSum(processGraph,hub,tmpAuthScore)
            
        #smoothing the scores    
        if(shouldConverge(tmpHubScore, tmpAuthScore)):
            break
        
        massageHubAuthScore(processGraph.number_of_nodes())        
        iteration+=1
    return iteration    

def  processnodefFile(notesFile):
    lines = [line.strip() for line in open(notesFile)]
    count=0
    for line in lines:
        count=count+1
        GlobalNodeList[line]=count
        
        
def  processtweetsFile(tweetsFile):
    lines = [line.strip() for line in open(tweetsFile)]
    for line in lines:
        tweetJson= cjson.decode(line)
        t= Tweet()
        t.processTweet(tweetJson)

def printDict(docDict):
    count=0
    for doc in docDict:
       count+=1
       print count,
       print ". ",
       print doc[0],
       print "  [ ",
       print doc[1],
       print" ]"
    
def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--datapath", help="Directory location for tweet files")
    args = parser.parse_args()
    datapath = args.datapath
    if datapath == "":
        print "usage: ./main.py --datapath Data_Source"
        sys.exit(1)
        
    notesFile=datapath+"/nodes.txt"
    tweetsFile=datapath+"/tweets.txt"
    
    print "Loading Tweets and node Files !!"
  
    processnodefFile(notesFile)    
    processtweetsFile(tweetsFile) 
    GlobalDirectedGraphFinal=nx.weakly_connected_component_subgraphs(GlobalDirectedGraph)[0]
  
    #initialize the hub and authority score
    nodeList=GlobalDirectedGraphFinal.nodes()
    for nd in nodeList:
        GlobalHubScore[nd]=1.0

    for nd in nodeList:
        GlobalAuthorityScore[nd]=getHubSum(GlobalDirectedGraphFinal,nd,GlobalHubScore)
        
    it=processHubAuthority(GlobalDirectedGraphFinal);

    print "Processing Statistics::"
    print "*********************************************"
    print("Size of full graph             =",GlobalDirectedGraph.number_of_nodes())
    print("Size of  weekly Conn graph     =",GlobalDirectedGraphFinal.number_of_nodes())
    print("Number of Iterations           =",it)
    
    print "_____________________________________________\n"
    print"Top 20 Hubs:"
    print "*********************************************"
    print printDict(getTop20results(GlobalHubScore))
    
    
    print "_____________________________________________\n"
    print"Top 20 Authorities:"
    print "*********************************************"
    print printDict(getTop20results(GlobalAuthorityScore))
        
   
   
if __name__ == '__main__': 
    main()

