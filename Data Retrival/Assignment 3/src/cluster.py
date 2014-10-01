#!/usr/bin/env python
# -*- coding: utf-8  -*-
#encoding=utf-8

import tweepy
import time
import sys
import re
import math
from collections import defaultdict
import random



class TwitterCrawler():
    # Fill in the blanks here for your own Twitter app.
    consumer_key = ""
    consumer_secret = ""
    access_key = ""
    access_secret = ""
    auth = None
    api = None


    def __init__(self):
        self.auth = tweepy.OAuthHandler(self.consumer_key, self.consumer_secret)
        self.auth.set_access_token(self.access_key, self.access_secret)
        self.api = tweepy.API(self.auth,parser=tweepy.parsers.JSONParser())
        print self.api.rate_limit_status()

    def re_init(self):
        self.auth = tweepy.OAuthHandler(self.consumer_key, self.consumer_secret)
        self.auth.set_access_token(self.access_key, self.access_secret)
        self.api = tweepy.API(self.auth,parser=tweepy.parsers.JSONParser())

    def check_api_rate_limit(self, sleep_time):
        try:
            rate_limit_status = self.api.rate_limit_status()
        except Exception as error_message:
            if error_message['code'] == 88:
                print "Sleeping for %d seconds." %(sleep_time)
                print rate_limit_status['resources']['statuses']
                time.sleep(sleep_time)

        while rate_limit_status['resources']['statuses']['/statuses/user_timeline']['remaining'] < 10:
            print "Sleeping for %d seconds." %(sleep_time)
            print rate_limit_status['resources']['statuses']
            time.sleep(sleep_time)
            rate_limit_status = self.api.rate_limit_status()
        print rate_limit_status['resources']['statuses']['/statuses/user_timeline']

    def crawl_user_profile(self, user_id):
        self.check_api_rate_limit(900)
        try:
            user_profile = self.api.get_user(user_id)
        except:
            return None
        return user_profile

    def crawl_user_tweets(self, user_id, count):
        self.check_api_rate_limit(900)
        try:
            tweets = self.api.user_timeline(user_id, count = count)
        except:
            tweets = None
        tried_count = 0
        while len(tweets) < count:
            try:
                tweets.extend(self.api.user_timeline(user_id, count = count))
            except:
                pass
            tried_count += 1
            if tried_count == 3:
                break
        return tweets[:count]
    def search_tweet(self,query):
        
        self.check_api_rate_limit(100)
        try:
            tweetjson = self.api.search(query,count=50)
        except:
            tweetjson = None
        return tweetjson


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


GlobalwordDictionary = defaultdict(lambda: defaultdict(int)) 
GlobalTweetFreqDict = defaultdict(lambda: defaultdict(int))
GlobalTweetGroupDict = defaultdict(str)
GloabalSearchText = ["#katyperry","#katycats","#darkhorse","#iHeartRadio","#ladygaga","#TaylorSwift","#sxsw","Rolling Stone","@DwightHoward","#rockets","jeremy lin","toyota center","kevin mchale","houston nba","James Harden","linsanity","Jan Koum","WhatsApp","#SEO","facebook","#socialmedia","Zuckerberg","user privacy","#Instagram","Obama","#tcot","Russia","Putin","White House","Ukraine","Rand Paul","foreign policy"]
                                                                                                                                                                                                                                                                                                                                                 
def processtweet(tweettext,tweetcount,queryNum):
    docname="tweet"+str(tweetcount)
    tweettext=tweettext.lower()
    GlobalTweetGroupDict[docname]= "Group"+str(queryNum/8)
    for word in re.findall("[a-z0-9]+", tweettext):  
            if word:
                GlobalwordDictionary[word][docname] += 1
                GlobalTweetFreqDict[docname][word] += 1
                
                
GlobalTweetTFIDFDict = defaultdict(lambda: defaultdict(float))    
def createTfIdfforTweets():
    for tweet in GlobalTweetFreqDict:
        for word in GlobalTweetFreqDict[tweet]:
            GlobalTweetTFIDFDict[tweet][word] = getTF(GlobalTweetFreqDict[tweet][word]) * getIDF(len(GlobalwordDictionary[word]), len(GlobalTweetFreqDict.keys()))




def getTweetdict():
    tc = TwitterCrawler()
    totalCount=0
    for q in GloabalSearchText:
        tweetjson=tc.search_tweet(q+ " -RT")
        #print "q=",
        #print q,
        #print "     count=",
        #print len(tweetjson["statuses"])
        
        for tweet in tweetjson["statuses"]:
            processtweet(tweet["text"],totalCount,GloabalSearchText.index(q))
        
        totalCount+=1

def getRandomTweetDoc(doneList):
    tweetDoc=0
    while 1:
        tweetDoc=random.randint(0,len(GlobalTweetTFIDFDict.keys())-1)
        if tweetDoc not in doneList:
            break 
    return tweetDoc

def createTandomKvector(kVal):
    randomKvector = defaultdict(lambda: defaultdict(float)) 
    doneList=list()
    for x in range(0,kVal):
        vectorName="K-vec"+str(x)
        tweetdoCno=getRandomTweetDoc(doneList)
        doneList.append(tweetdoCno)
        tweetname="tweet"+str(tweetdoCno)
        randomKvector[vectorName]=  GlobalTweetTFIDFDict[tweetname]     
    return randomKvector

# get the score for a document for a query. Use GlobalQueryVectorDict
def getVectorsCosineScore(docvect1,docvect2):
    cosinescore = 0
    size1= getVectorLen(docvect1)
    size2 = getVectorLen(docvect2) 
    for word in docvect1:
        if word in docvect2:
            cosinescore += docvect1[word] * docvect2[word]
        
    return cosinescore / (size1 * size2)    


def getDistanceBetweenTwoVec(docvect1,docvect2):
    summ=0;
    donedict=defaultdict(int)
    for word in docvect1:
        if word in docvect2:
            summ+=math.pow(docvect1[word]-docvect2[word],2)
            donedict[word]=1
        else:
            summ+=math.pow(docvect1[word],2)
            
    #consider remaingword in vector2
    for word in docvect2:
        if word not in donedict:
            summ+=math.pow(docvect2[word],2)    
    return math.sqrt(summ)           


def getCentroidFromCluster(tweetList):
    centroidVector = defaultdict(float) 
    for tweet in tweetList:
        if tweet in GlobalTweetTFIDFDict:
            for word in GlobalTweetTFIDFDict[tweet]:
                centroidVector[word]+=GlobalTweetTFIDFDict[tweet][word]
     
    for word in centroidVector:
        centroidVector[word]=centroidVector[word]/len(tweetList);
    
    return centroidVector

def updaterandomVector(randomVect,clusterDict):
    for doc in randomVect:
        randomVect[doc]=getCentroidFromCluster(clusterDict[doc])

    
def createKMeanCluster(kValue):
    rvClusterDict=defaultdict(lambda: list())  
    randomVect=createTandomKvector(kValue)
    clusterDict=defaultdict(lambda: list()) #clusterNum,LIstof tweetsID
    for doc in randomVect:
        clusterDict[doc]=list()
    
    for i in range(0,10):
        for tweet in GlobalTweetTFIDFDict:
            distanceDict = defaultdict(float)
            for doc in randomVect:
                distanceDict[doc]=getVectorsCosineScore(GlobalTweetTFIDFDict[tweet],randomVect[doc]) 
        
            minvalkey=[k for k,v in distanceDict.items() if v==max(distanceDict.values())][0]
            clusterDict[minvalkey].append(tweet)
    
        updaterandomVector(randomVect,clusterDict)
        rvClusterDict=clusterDict.copy()
        clusterDict.clear()
        
    return rvClusterDict
    
def getRSSfromCluster(clusterDict):
    rss=0
    for cluster in clusterDict:
        centroidDict= getCentroidFromCluster(clusterDict[cluster])
        summ=0
        for tweet in clusterDict[cluster]:
            summ+=(1-getVectorsCosineScore(GlobalTweetTFIDFDict[tweet],centroidDict))
            #summ+=math.pow(getDistanceBetweenTwoVec(GlobalTweetTFIDFDict[tweet],centroidDict),2)
        rss+=summ
    return rss

def getMax(tweetcollection):
    groupCountDict= defaultdict(int)
    for tweet in tweetcollection:
        groupCountDict[GlobalTweetGroupDict[tweet]]+=1
    return [v for k,v in groupCountDict.items() if v==max(groupCountDict.values())][0]
    
def calculatePurity(clusterDict):
    maxx=0.0
    totalCount=0.0
    for cluster in clusterDict:
        maxx+= getMax(clusterDict[cluster])
        totalCount+=len(clusterDict[cluster])
    return maxx/totalCount
        
def outputClusterInfo(clusterDict):
    for cluster in clusterDict:
        print"clusterID=",
        print cluster,
        print"count=",
        print len(clusterDict[cluster])
  
def getMaxCluster(size):
    maxClusterDict=defaultdict(lambda: list())  
    maxRSS=sys.float_info.max
    maxPurity=0.0
    for i in range(0,50):
        clusterDict=createKMeanCluster(size)
        rss= getRSSfromCluster(clusterDict) 
        if(rss<maxRSS):
            maxRSS=rss
            maxClusterDict=clusterDict
        if size==4:
            purity=calculatePurity(clusterDict)
            if purity>maxPurity:
                maxPurity=purity 
    
    return maxClusterDict,maxRSS,maxPurity
def main():
    tc = TwitterCrawler()
    tc.check_api_rate_limit(900)
    print ""
    print "Fetching and Indexing tweets. Please Wait......"
    print ""
    getTweetdict()
    print "Creating weighted TF-IDF for tweets. Please Wait......"
    print ""
 
    print "Stats*************"
    print "Number of words=",
    print len(GlobalwordDictionary)
    print "Number of tweets=",
    print len(GlobalTweetFreqDict)
    createTfIdfforTweets()
    print "Creating cluster Using K-Means. Please Wait......"
    print ""
    
    clustersize=[2,4,6,8]
    for size in clustersize:
        clusterDict,rss,purity= getMaxCluster(size)
        if size==4:
            print"purity=",
            print purity,
        print"size=",
        print size,
        print "  rss=",
        print rss
        outputClusterInfo(clusterDict)
    
        
   
if __name__ == '__main__': 
    main()