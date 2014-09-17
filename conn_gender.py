import csv
from genderator.detector import *
import re
import sys

d = Detector()
male = 0
female = 0
unknown = 0
with open(sys.argv[1],'r') as csvinput:
    with open(sys.argv[2], 'wb') as csvoutput:
        writer = csv.writer(csvoutput)
        for row in csv.reader(csvinput):
            #print row
            if len(row) > 13 :
                pattern = re.compile(r'\W+')
                name = pattern.split(row[5])[0]
                #print name
                if d.getGender(name) == MALE :
                    gender = 'male'
                if d.getGender(name) == FEMALE :
                    gender = 'female'
                if d.getGender(name) == ANDROGYNOUS :
                    gender = 'unknown'
                #print gender
                writer.writerow(row+[gender])
