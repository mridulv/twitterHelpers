import csv
from pattern.en import sentiment
from string import punctuation
import re
import sys

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

with open(sys.argv[1],'r') as csvinput:
    with open(sys.argv[2], 'wb') as csvoutput:
        writer = csv.writer(csvoutput)
        for row in csv.reader(csvinput):
            tweet = row[13]
            tweet_processed=processTweet(tweet)
            score = sentiment(tweet_processed)[0]
            print score
            writer.writerow(row+[score])