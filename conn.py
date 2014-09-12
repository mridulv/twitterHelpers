import MySQLdb

from pattern.en import sentiment
from string import punctuation
import re

#start process_tweet
def processTweet(tweet):
    tweet = tweet.lower()
    tweet = re.sub('((www\.[\s]+)|(https?://[^\s]+))','URL',tweet)
    tweet = re.sub('@[^\s]+','',tweet)
    tweet = re.sub('[\s]+', ' ', tweet)
    tweet = re.sub(r'#([^\s]+)', r'\1', tweet)
    tweet = tweet.strip('\'"')
    for p in list(punctuation):
        tweet=tweet.replace(p,'')
    return tweet

db = MySQLdb.connect(host="localhost",user="",passwd="",db="test")

# you must create a Cursor object. It will let
#  you execute all the queries you need
cur = db.cursor()
cur2 = db.cursor()

# Use all the SQL you like
cur.execute("SELECT * FROM analysis_tweets_new")

# print all the first cell of all the rows
count  = 0
for row in cur.fetchall() :
    key = row[0]
    name = row[0]
    tweet = row[15]
    tweet_processed=processTweet(tweet)
    score = sentiment(tweet_processed)[0]
    # print tweet_processed , score
    #print query
    cur.execute("UPDATE analysis_tweets_new SET sentiment = '%s' WHERE key_val LIKE '%s'"% (score, name))
    db.commit()
    if count % 1000 == 0 :
        print count
    count = count + 1
