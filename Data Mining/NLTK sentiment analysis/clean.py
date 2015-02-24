#this one is used to reduce the number of columns and remove punctuations manually.

import re

txt = open("Data.csv")
counter = 0
camera = 0

for line in txt:
	counter += 1
	tokens = line.split(',')
	#only get camera data (necessary?)
	if not tokens[-2].lower() == 'Camera'.lower():
		continue
	camera += 1
	line = ''
	text = re.sub("[^A-Za-z']", ' ', tokens[-1]).strip()
	text = re.sub('[ ]+', ' ', text)
	print tokens[2] + "," + tokens[-1]