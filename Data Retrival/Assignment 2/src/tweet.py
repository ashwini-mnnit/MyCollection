

from collections import defaultdict
import re
from collections import Counter
import networkx as nx


GlobalNodeList = defaultdict(int) #[int: id of the node 0.....n]
GlobalDirectedGraph=nx.DiGraph()
GlobalDirectedGraphFinal=nx.DiGraph()

class Tweet():
    global GlobalDirectedGraph
    def __init__(self):
        self.tid = None
        self.text = None
        self.wordfreq = {} # [term] = frequency
        self.nwordfreq = {} # [term] = normailzed tf
        self.wordlist = []
        self.tuser = None
        self.tuser_mention = {}
        
    def printTweet(self):
        print "ID            : %s" %self.tid
        print "text          : %s" %self.text
        print "wordfreq      : %s" %self.wordfreq
        print "nwordfreq     : %s" %self.nwordfreq
        print "tweet users   : %s" %self.tuser
        print "user_mentions : %s" %self.tuser_mention
        
    def processTweet(self,tweetJson):
        self.tid = tweetJson["id"]
        self.text = tweetJson["text"].lower()
        self.wordlist = re.findall(r"[\w']+",self.text)
        self.wordfreq = Counter(self.wordlist)
        self.tuser = str(tweetJson["user"]["screen_name"])
        
        entities = tweetJson["entities"]
        if entities:
            self.tuser_mention=entities["user_mentions"]
            for e in self.tuser_mention:
                node= e["screen_name"]
                GlobalDirectedGraph.add_edge(self.tuser, node,weight=1)
               
                    
                      
                    
                
