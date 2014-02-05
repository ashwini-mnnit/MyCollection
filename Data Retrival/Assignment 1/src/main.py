'''
Created on Jan 24, 2014

@author: Ashwini Singh
'''

from util import *
import glob

# Global Variables.
GlobalDataDictionary = defaultdict(lambda: defaultdict(int)) 
GlobalDocumentFreqDict = defaultdict(lambda: defaultdict(int))
GlobalQueryVectorDict = defaultdict(int)
GlobalDocTFIDFDict = defaultdict(lambda: defaultdict(float))
total_N_doc = 0

def createTfIdfforDocument():
    global total_N_doc
    for doc in GlobalDocumentFreqDict:
        for word in GlobalDocumentFreqDict[doc]:
             GlobalDocTFIDFDict[doc][word] = getTF(GlobalDocumentFreqDict[doc][word]) * getIDF(len(GlobalDataDictionary[word]), total_N_doc)

        
def creatSimpleBooleanIndex(dirname):
    global total_N_doc
    pattern = dirname + "/*/*/a*.txt"
    for filename in glob.glob(pattern):
        pFile = open(filename)
        total_N_doc += 1
        filetext = pFile.read().lower()
#        for word in re.sub(r'[a-z0-9]',' ', filetext).split():
        for word in re.findall("[a-z0-9]+", filetext):  
            if word:
                GlobalDataDictionary[word][filename] += 1
                GlobalDocumentFreqDict[filename][word] += 1
        pFile.close()
    print "Initial word indexing is done !!"
    print "            Number of Files Indexed is               : {0}".format(total_N_doc)
    print "            Number of distinct word in the index     : {0}".format(len(GlobalDataDictionary.keys()))  
    

def getDocumentList(queryList):
    keySetList = list()
    for word in queryList:
        if GlobalDataDictionary.has_key(word):
            keySetList.append(set(GlobalDataDictionary[word].keys()))
        else:
            keySetList.append(set())
    returnVal = list(set.intersection(*keySetList))
    return returnVal    
  
def printBooleanRetrivalDocmentList(dList):
    if len(dList) <= 0:
        print "sorry, no match\n"
    else:
        for doc in dList:
            print doc.replace("/", " ").rsplit(" ", 1)[1].rsplit(".", 1)[0],
            print " ",
            

def printVectoSpaceRetrivalDocmentList(docDict):
    if len(docDict) <= 0:
        print "sorry, no match\n"
    else:
        count=0;
        for doc in docDict:
            count+=1
            print count,
            print ". ",
            print doc[0].replace("/", " ").rsplit(" ", 1)[1].rsplit(".", 1)[0],
            print "  [ ",
            print("{0:.8f}".format(round(doc[1], 8))),
            print" ]"
                    
def updateQueryVector(queryList):
    GlobalQueryVectorDict.clear()
    for word in queryList:
        GlobalQueryVectorDict[word] += 1
    # take the tf weight         
    for word in GlobalQueryVectorDict:
        GlobalQueryVectorDict[word] = getTF(GlobalQueryVectorDict[word])        
        
# get the score for a document for a query. Use GlobalQueryVectorDict
def getSpaceVectorScore(docvector, queryList):
    cosinescore = 0
    sizeQuery = getVectorLen(GlobalQueryVectorDict)
    sizeDoc = getVectorLen(docvector) 
    for word in queryList:
        if word in docvector:
            cosinescore += GlobalQueryVectorDict[word] * docvector[word]
        
    return cosinescore / (sizeQuery * sizeDoc)    


# get the list document score. TODO Use heap for top 50 results

def getdocumrtCount(word):
    if word in GlobalDataDictionary:
        return len(GlobalDataDictionary[word].keys())
    return 0;


def getVectorSpaceRetrivalDocmentList(qList):
    retDocumentDict = defaultdict()  # this contains the score for each document
    # get the document list to process
    for word in qList:
        if word in GlobalDataDictionary:
            fileprocessDict = GlobalDataDictionary[word];
            for docname in fileprocessDict:
                retDocumentDict[docname] = getSpaceVectorScore(GlobalDocTFIDFDict[docname], qList)
    return getTop50results(retDocumentDict)        

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--datapath", help="Directory location of the data")
    args = parser.parse_args()
    datapath = args.datapath
    if datapath == "":
        print "usage: ./main.py --datapath Data_Source"
        sys.exit(1)
        
    print "Indexing the Documents .Please Wait......\n"
    print ""
    
    total_time = time.time() 
    start_time = time.time()
    creatSimpleBooleanIndex(str(datapath))
    print "            Initial Word Indexing Time               :",
    print time.time() - start_time, "seconds"
    
    
    print ""
    print "Creating TF-IDF index .Please Wait......"
    print ""

    start_time = time.time()
    createTfIdfforDocument()
    print "            TF-IDF Indexing Time                     :",
    print time.time() - start_time, "seconds"
    
    print ""
    print "_____________________________________________________________________________________________"
    print "            Total Indexing Time                      :",
    print time.time() - total_time, "seconds"
    
    print ""
    
    
    while 1:
        qStr = raw_input("####### Enter the search String or 'exit' to exit :") 
        if qStr == 'exit': 
            break
        #qList = [word.lower() for word in re.split("( |\\\".*?\\\"|'.*?')", qStr) if word.strip()]
        qList = re.sub(r'[\W_]+',' ', qStr.lower()).split()
        booleanDocumnetList = list()
        if len(qList) < 1:
            continue
        booleanDocumnetList = getDocumentList(qList)
        print ""
        print "#####################################################Boolean Retrieval Result################################################################"
        print ""
        printBooleanRetrivalDocmentList(booleanDocumnetList) 
        print ""
        print "--------------------------------------------------------------------------------------------------------------------------------------------"
        print ""
        print ""
        print "#####################################################Vector Space Retrieval Result################################################################"
        print ""
        updateQueryVector(qList)
        vectorSpaceDocumnetDict = getVectorSpaceRetrivalDocmentList(qList)
        printVectoSpaceRetrivalDocmentList(vectorSpaceDocumnetDict)
        print "--------------------------------------------------------------------------------------------------------------------------------------------"
    print "Successfully Exit form the Search Engine. Have Fun !!\n" 
    return
     
if __name__ == '__main__': 
    main()

