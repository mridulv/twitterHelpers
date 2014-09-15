import csv
import numpy
from numpy import matrix
from numpy import linalg
from pylab import *
import matplotlib.pyplot as pp

reader=csv.reader(open("data_normalized.csv","rb"),delimiter=',')
x=list(reader)
result=numpy.array(x).astype('float')

print result[1:,0]

A = numpy.column_stack((result[1:,0],result[1:,1],result[1:,2],result[1:,3]))
X = numpy.array(result[1:,4])

prod_1 = numpy.dot(A.T,A)
prod_2 = linalg.inv(prod_1)
prod_3 = numpy.dot(A.T,X)
prod_4 = numpy.dot(prod_2,prod_3)

print prod_4
