import numpy
from numpy import *
import csv
from pylab import *

df1 = csv.reader(open('analysis_tweets.csv', 'rb'),delimiter=',')
tmp = list(df1)
b = numpy.array(tmp).astype('string')
b1 = b[1:,0]
b2 = b[1:,1]

desired_array1 = [float(numeric_string) for numeric_string in b1]
desired_array2 = [float(numeric_string) for numeric_string in b2]

m,b = polyfit(desired_array1, desired_array2, 1)
print m
print b
