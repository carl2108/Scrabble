'''
This file is for processing Google's n-gram data to fill a dictionary 
with relative word frequencies.

The file is parsed, and if a match is made it is added to the count.
'''

import sys, string

def condense(text):
	dictionary = open("OSPD.txt", "r")
	wordFreq = open("allWordFreq.txt", "r")
	condenseFreq= open("condensedFreq.txt", "w")

	for dictLine in dictionary:
		dictWord = dictLine.strip()
		freq = 0
		for wordFreqLine in wordFreq:
			word = wordFreqLine.strip("0123456789\n\r\t")
			if word == dictWord:
				temp = wordFreqLine.strip("ABCDEFGHIJKLMNOPQRSTUVWXYZ\n\r\t")
				freq += int(temp)
		wordFreq = open("allWordFreq.txt", "r")
		line = dictWord + " 	%d" %freq
		condenseFreq.write(line + "\n")
	condenseFreq.close()
	print "Done!"

def isScrabbleWord(word):
	dictionary = open("media/OSPD.txt", "r")
	for line in dictionary:
		line = line.strip()
		if word == line:
			return True
	return False

def saveWordFreq(word, freq):
	line = word + "		%d" %freq
	myfile = open("ngrams/allWordFreq.txt", "a")
   	myfile.write(line + "\n")
   	myfile.close()
   	print line

def parse(letter):
	global totalLines
	global wordsFound
	global wordsNotFound

	text = "googlebooks-eng-all-1gram-20120701-" + letter.lower()
	print "Processing: " + text
	dictionary = open("OSPD.txt", "r")
	dictHash = {}
	dictCount = 0
	for word in dictionary:
		word = word.rstrip()
		if word[0] == letter:
			dictHash[word] = 0
			dictCount += 1

	print "dictHash: ", dictCount

	lastWord = ""
	totalUsage = 0
	totalUpdated = 0
	lineCount = 0

	num_lines = sum(1 for line in open(text))
	print "num_lines: ", num_lines

	totalLines += num_lines
		
	with open(text) as infile:
		for line in infile:
			
			line = line.rstrip()
			tokens = line.split()
				
			word = tokens[0].split("_")[0].upper()
			year = int(tokens[1])
			count = int(tokens[2])

			lineCount += 1
				
			if lastWord == word:
				if year > 1949:		#parse year
					totalUsage += count
			else:
				if dictHash.has_key(lastWord): 	#isScrabbleWord(lastWord, totalUsage)
					#saveWordFreq(lastWord, totalUsage)
					dictHash[lastWord] += totalUsage
					#totalUpdated += 1

				totalUsage = count
			lastWord = word	
			if lineCount % 1000000 == 0:
				print lineCount*100/num_lines

	myfile = open("allWordFreq-a-z.txt", "a")
	for key in dictHash:
		if dictHash[key] == 0:
			dictHash[key] = -1
			wordsNotFound += 1
		else:
			wordsFound += 1
		line = key + "		%d" %dictHash[key]
   		myfile.write(line + "\n")
   	myfile.close()
	print "Done!"					
		
#Main!
global totalLines
totalLines = 0
global wordsFound
wordsFound = 0
global wordsNotFound
wordsNotFound = 0

for letter in string.uppercase:
	parse(letter)

num_lines = sum(1 for line in open("OSPD.txt"))
print "Dict words: ", num_lines

if (num_lines == (wordsFound+wordsNotFound)):
	allFound = True
else:
	allFound = False

print "Lines processed: ", totalLines
print "Words found: ", wordsFound
print "Words not found: ", wordsNotFound
print "All words: ", allFound