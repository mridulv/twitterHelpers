__author__ = 'mridul.v'

import csv
import numpy
from numpy import matrix
from numpy import linalg
from pylab import *
import matplotlib.pyplot as pp
from sklearn import linear_model

clf = linear_model.LinearRegression()

reader=csv.reader(open("rating_deviation.csv","rb"),delimiter=',')
x=list(reader)
result=numpy.array(x).astype('float')

print result[1:,0]

A = numpy.column_stack((result[1:,0],result[1:,1],result[1:,2],result[1:,3]))
X = numpy.array(result[1:,4])

clf.fit (A, X)

print clf.coef_
print clf