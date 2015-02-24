import sys
import collections, itertools
import nltk.classify.util, nltk.metrics
from nltk.classify import NaiveBayesClassifier
from nltk.corpus import stopwords
from nltk.collocations import BigramCollocationFinder
from nltk.metrics import BigramAssocMeasures
from nltk.corpus import stopwords
from nltk.stem.snowball import SnowballStemmer

yes = []
no = []
stop = stopwords.words('english')
stemmer = SnowballStemmer("english")

txt = open("cleaned.csv")

#get word features from the comments
def get_words(text):
    bag_of_words = {}
    for word in text.lower().split():
        if word not in stop:
            bag_of_words[stemmer.stem(word)] = True
    return bag_of_words;

#encode original text into features and seperate into positive comments or negative comments
for line in txt:
    tokens = line.split(',')
    if tokens[0] == 'Yes':
        yes.append((get_words(tokens[-1]), 'pos'))
    elif tokens[0] == 'No':
        no.append((get_words(tokens[-1]), 'neg'))

#Choose 3/4 of the data as training data
negcutoff = len(no)*3/4
poscutoff = len(yes)*3/4

print negcutoff
print poscutoff

#choose the smaller one as the cutoff, to make the number of positve and negative comments the same (necessary?)
cutoff = negcutoff if negcutoff < poscutoff else poscutoff

#seperate training set and testing set
trainfeats = no[:cutoff] + yes[:cutoff]
testfeats = no[-cutoff / 3:] + yes[-cutoff / 3:]

classifier = NaiveBayesClassifier.train(trainfeats)

#calculate tpr
tp_counter = 0
for pos in yes[-cutoff / 3:] :
    if(classifier.classify(pos[0]) == "pos"):
        tp_counter += 1

tpr = tp_counter * 1.000 / (cutoff / 3)

#tnr
tn_counter = 0
for negs in no[-cutoff / 3:] :
    if(classifier.classify(negs[0]) == "neg"):
        tn_counter += 1

tnr = tn_counter * 1.000 / (cutoff / 3)

print 'tpr:', tpr
print 'tnr:', tnr

print 'accuracy:', nltk.classify.util.accuracy(classifier, testfeats)
classifier.show_most_informative_features(n = 10000)