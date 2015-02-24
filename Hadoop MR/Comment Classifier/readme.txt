CHECKLIST:

# Java source code. They are under folder src
# lingpipe-4.1.0.jar. This is a NLP library, on which our classification module depends.
# sort.py. This is a python program which sorts the output of the star user part.
# Training-Sample.zip. This is a training sample to train the language model.

Comments clustering:
This map reduce program will generate the total number of comments in 4 different categories. You need to put the training samples in HDFS as distributed cache.

Who is the star:
This map reduce program will generate user id as key and the total number of each type of comment that user has made. You need to put the training samples in HDFS as distributed cache. 

sort.py:
This python program will sort the output from whoIsTheStar to see the top ten users in different types of comments. It will take an single number argument to decide which column to sort
Usage: cat output | python sort.py 1