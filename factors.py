from numpy import matrix
from numpy import linalg
import numpy
from numpy import *
import csv
from pylab import *

df1 = csv.reader(open('data.csv', 'rb'),delimiter=',')
tmp = list(df1)
b = numpy.array(tmp).astype('string')
b1 = b[1:,0]
b2 = b[1:,1]
b3 = b[1:,2]
b4 = b[1:,3]
b5 = b[1:,4]

desired_array1 = [float(numeric_string) for numeric_string in b1]
desired_array2 = [float(numeric_string) for numeric_string in b2]
desired_array3 = [float(numeric_string) for numeric_string in b3]
desired_array4 = [float(numeric_string) for numeric_string in b4]
desired_array5 = [float(numeric_string) for numeric_string in b5]

A = numpy.column_stack((np.array(desired_array1),np.array(desired_array2),np.array(desired_array3),np.array(desired_array4)))
X = numpy.array(desired_array5)

print A
print X

prod_1 = numpy.dot(A.T,A)
prod_2 = linalg.inv(prod_1)
prod_3 = numpy.dot(A.T,X)
prod_4 = numpy.dot(prod_2,prod_3)

print prod_4
