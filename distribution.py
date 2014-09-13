from numpy import matrix
from numpy import linalg
import numpy
from numpy import *
import csv
from pylab import *
import numpy as np
import matplotlib.pyplot as pp

df1 = csv.reader(open('distribution_followers.csv', 'rb'),delimiter=',')
tmp = list(df1)
b = numpy.array(tmp).astype('string')
b1 = b[1:,0]
b2 = b[1:,1]

desired_array1 = [float(numeric_string) for numeric_string in b1]
desired_array2 = [float(numeric_string) for numeric_string in b2]

val = 0.
ar = desired_array1
pp.plot(ar, np.zeros_like(ar) + val, 'x')
pp.show()
