#!/usr/bin/env python
# -*- coding: utf-8  -*-
#encoding=utf-8

import tweepy
import time
import sys
import re
import json

from random import randint

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
              #  print rate_limit_status['resources']['statuses']
                time.sleep(sleep_time)

        while rate_limit_status['resources']['statuses']['/statuses/user_timeline']['remaining'] < 10:
         #   print "Sleeping for %d seconds." %(sleep_time)
          #  print rate_limit_status['resources']['statuses']
            time.sleep(sleep_time)
            rate_limit_status = self.api.rate_limit_status()
        #print rate_limit_status['resources']['statuses']['/statuses/user_timeline']

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
        self.check_api_rate_limit(900)
        try:
            tweetjson = self.api.search(query,count=50)
        except:
            tweetjson = None
        return tweetjson

def main():
    tc = TwitterCrawler()
    tc.check_api_rate_limit(900)
    user = tc.crawl_user_profile(12)
    #print user
    while 1:
        qStr = raw_input("####### Enter the search String or 'exit' to exit :") 
        if qStr == 'exit':
            break;
        #query = re.sub(r'[\W_]+',' ', qStr.lower())+" -RT"
        tweetjson=tc.search_tweet(qStr+" -RT")
        
        for tweet in tweetjson["statuses"]:
            print tweet["text"]#.encode('utf8')
           #print "\n"
        
   
if __name__ == '__main__': 
    main()