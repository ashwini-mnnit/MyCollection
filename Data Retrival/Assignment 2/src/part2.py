'''
Created on Feb 20th, 2014

@author: Ashwini Singh
'''
from __future__ import division

 
import sys
import argparse
from collections import defaultdict
from sklearn.svm import *
import heapq 

def printDict(docDict,realDict):
    count=0
    for doc in docDict:
       count+=1
       featureid=doc[0]
       print count,
       print ". ",
       print doc[0],
       print "  [ ",
       print realDict[featureid],
       print" ]"


def gettrainedSVC(featureValList,ClassValList):
    #Classifier = SVC(C=0.04,gamma=0.1,kernel='linear')
    Classifier = SVC(C=0.1,gamma=0.1,kernel='linear')#fold2
    #Classifier = SVC(C=0.004,gamma=0.1,kernel='linear')#fold3
    Classifier.fit(featureValList,ClassValList) 
    return Classifier

def getPropAndclassificationList(QueryDocDict):
    featureValList=list()
    classValList=list()
    
    for queryID in QueryDocDict:
        for doc in QueryDocDict[queryID]:
            for doc1 in QueryDocDict[queryID]:
                if((doc==doc1) or (QueryDocDict[queryID][doc]['0']==QueryDocDict[queryID][doc1]['0'])): #same doc or same relScore of documents -> ignore
                    continue;                
                #add class value
                if(QueryDocDict[queryID][doc]['0']>QueryDocDict[queryID][doc1]['0']):
                    classValList.append(1)
                else:
                    classValList.append(-1)
                
                #add feature value
                featurevalL=list()
                
                for featureID in range(1,47):
                    strfeatureID=str(featureID)
                    featurevalL.append( QueryDocDict[queryID][doc][strfeatureID]- QueryDocDict[queryID][doc1][strfeatureID])
                    
                featureValList.append(featurevalL)
                    
    
    
    return featureValList,classValList
    

def processTrainfile(trainFile):
    QueryDocDict=defaultdict(lambda: defaultdict((lambda: defaultdict(float))))
    lines = [line.strip() for line in open(trainFile)]    
    old_qid=0
    docID=0
    for line in lines:
        processline=line.split()        
        relScode=float(processline[0])        
        qid=processline[1].split(":")[1]
        
        if(old_qid!=qid):
            old_qid=qid
            docID=0
            
        docID+=1
        docname="doc"+str(docID)
        QueryDocDict[qid][docname]['0']=relScode #0 index contains relevance score
        for index in range(len(processline)):
            if index < 2: 
                continue
            featureID=processline[index].split(":")[0]
            featureVal=float(processline[index].split(":")[1])
            QueryDocDict[qid][docname][featureID]=featureVal
               
    return QueryDocDict;

def getTop10results(docScoreDict):
    heap = [(-value, key) for key,value in docScoreDict.items()]
    largest = heapq.nsmallest(10, heap)
    largest = [(key, -value) for value, key in largest]
    return largest;
    
def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--datapath", help="Directory location for tweet files")
    args = parser.parse_args()
    datapath = args.datapath
    if datapath == "":
        print "usage: ./main.py --datapath Data_Source"
        sys.exit(1)
        
    testFile=datapath+"/test.txt"
    trainFile=datapath+"/train.txt"
    
    print "Loading test file for Training!!"
    QueryDocDictTrain=processTrainfile(trainFile)    
    
    print "Classification of training data !!"
    
    featureValListTrain,ClassValListTrain=getPropAndclassificationList(QueryDocDictTrain)    
    trainedClassifier=gettrainedSVC(featureValListTrain,ClassValListTrain)
    
    topFeatureList= trainedClassifier.coef_
    
    print "Processing Test Data !!"
    QueryDocDictTest=processTrainfile(testFile)
    featureValListTest,ClassValListTest=getPropAndclassificationList(QueryDocDictTest)
    

    countTotal=0
    matchCount=0
    for index in range(len(featureValListTest)):
        val=trainedClassifier.predict(featureValListTest[index])
        if(val==ClassValListTest[index]):
            matchCount+=1
        
        countTotal+=1
        
    print "Match % =",
    print (matchCount/countTotal)*100
    
    print "****************Top 10 Feature******************"
    
    featureID=0
    topfeatureDict=defaultdict(float)
    realValfeatureDict=defaultdict(float)
    for featureVal in topFeatureList[0]:
        featureID+=1
        strFID="Feature"+str(featureID)
        topfeatureDict[strFID]=abs(featureVal)
        realValfeatureDict[strFID]=featureVal
 

    print printDict(getTop10results(topfeatureDict),realValfeatureDict)
   

if __name__ == '__main__': 
    main()
