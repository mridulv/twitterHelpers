from numpy import matrix
from numpy import linalg
import numpy
from numpy import *
import csv
from pylab import *
import numpy as np
import matplotlib.pyplot as pp

df1 = csv.reader(open('distribution_rating.csv', 'rb'),delimiter=',')
tmp = list(df1)
b = numpy.array(tmp).astype('string')
b1 = b[1:,0]

desired_array1 = [float(numeric_string) for numeric_string in b1]

val = 0.
ar = desired_array1
pp.plot(ar, np.zeros_like(ar) + val, 'x')
pp.show()
