import json
import os
from pyvis import network as net
import pandas as pd
from django.shortcuts import render
from django.http import HttpResponse

# from elasticsearch import Elasticsearch

# es = Elasticsearch('http://192.168.65.128:9200/')
#
# response_score = es.get(index='scorecheck_df', id=1)
#
# print(response_score)

import elasticsearch
from elasticsearch.helpers import scan
import json

es = elasticsearch.Elasticsearch('http://192.168.65.128:9200')
es_response = scan(
    es,
    index='scorecheck_df',
    query={"query": { "match_all" : {}}}
)

for item in es_response:
    print(json.dumps(item))