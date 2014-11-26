#!/usr/bin/env python
from os import walk
from subprocess import call
import sys

print "Arguments: ",sys.argv
path='./'
if len(sys.argv)==2:
	path=sys.argv[1]
f=[]
for (dirpath,dirnames,filenames) in walk(path):
	f.extend(filenames)
	break

for file in f:
	if file.endswith(".mp3"):
		strs=file.split('.')
		wavfile=strs[0]+'.wav'
		lame ='/course/cs5500f14/bin/lame' 	
		call([lame,"--decode",file,wavfile])
		
