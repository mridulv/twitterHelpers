from geopy.geocoders import Nominatim
import urllib2
import csv
import re
import sys

def location_country(location):
    try:
        geolocator = Nominatim()
        location = geolocator.geocode(location)
        lat = location.latitude
        lng = location.longitude
        url = "http://api.geonames.org/countryCode?lat="+str(lat)+"&lng="+str(lng)+"&username=drbakshi"
        result = urllib2.urlopen(url)
        return result.read()[0:2]
    except urllib2.URLError, e:
        return "world"
    except AttributeError, e:
        return "world"
    except:
        return "world"


with open(sys.argv[1],'r') as csvinput:
    with open(sys.argv[2], 'wb') as csvoutput:
        writer = csv.writer(csvoutput)
        for row in csv.reader(csvinput):
            country_code = location_country(row[10])
            print country_code
            writer.writerow(row+[country_code])