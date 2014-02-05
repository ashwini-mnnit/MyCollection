#######################################################################################
#######################################################################################
################        HOMEWORK 1 REPORT           ###################################
#######################################################################################
#######################################################################################

StudentDetails:
--------------
Name : Ashwini Singh
UIN: 522006254

Platform Used :           Linux(Ubuntu) and python 2.7
---------------

Directory Structure:   hw1_522006254
--------------------        |-------- src
                            |          |-----main.py (**The entry point to the application)
                            |          |-----util.py (contains some util function)
                            |-------- README ( containing the execution instruction)  


Design Architecture:
---------------------

           The indexing contains two level of indexing. One word-document, containing the 
           count of word in a document. Second document-word, containing TF-IDF score for 
           each document.


How To Execute:
---------------
            step1: Go to src folder.
            step2: Execute cmd "python main.py --datapath=<location of nsf-award-abstracts folder>
 

            Sample Output:
            -------------
                root@xxx# python main.py --datapath="/home/ashwini/hw1/data/nsf-award-abstracts"
                Indexing the Documents .Please Wait......


                Initial word indexing is done !!
                Number of Files Indexed is               : <file count>
                Number of distinct word in the index     : <distinct word count>
                Initial Word Indexing Time               : <time> seconds

                Creating TF-IDF index .Please Wait......

                TF-IDF Indexing Time                     : <time> seconds
                ________________________________________________________________________________
                Total Indexing Time                      : <time> seconds

                \####### Enter the search String or 'exit' to exit :<provide search  query>

                \###########################Boolean Retrieval Result#############################

                         a9972725   a0318637   a0089681   a0083790
                ------------------------------------------------------------------------------------

                \##########################Vector Space Retrieval Result#########################

                1. a0083790   [  0.17402126  ]
                2. a9972725   [  0.17169620  ]


Execution Time:
--------------------
           The are two indexing is done in the before the start of search query. First, word-document 
           dictionary containing word count in each document.This involes file read. During my testing,
           this varies from 15 sec to 50 sec.
           Second, the TF-IDF score calculation for document. This varies from 13 sec to 30 sec.
           score calculation.  
