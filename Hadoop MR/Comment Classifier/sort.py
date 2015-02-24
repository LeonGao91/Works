import sys

column = int(sys.argv[1]) * 2
a = {}
for x in range(10):
	a[x] = [0, ""]

for line in sys.stdin:
	flag = False
	position = -1
	tokens = line.split()
	for x in range(10):
		if int(tokens[column]) > a[x][0]:
			flag = True
			position = x
		else:
			break
	if flag:
		a[position] = [int(tokens[column]), tokens[0]]

for key in range(9,0,-1):
	print a[key][1] + "\t" + str(a[key][0])
