from elasticsearch import Elasticsearch
from elasticsearch.helpers import bulk
from elasticsearch import helpers
from pandas import json_normalize
from datetime import datetime
import pandas as pd
import os, json
import time

es = Elasticsearch('http://34.64.68.123:9200/')

def create_index(index):
    if not es.indices.exists(index=index):
        return es.indices.create(index=index)

def delete(index, data):
    if data is None:
        data = {"match_all": {}}
    else:
        data = {"match": data}
    body = {"query": data}
    return es.delete_by_query(index, body=body)

def insert(index, doc_type, body):
    return es.index(index=index, doc_type =doc_type, body=body)

def search(index, data=None):
    if data is None:
        data = {
            'size': 10000,
            'query': {
                'match_all': {}
            }
        }
    else:
        data = {"match": data}
    res = es.search(index=index,body=data)

    return res

def update(index, doc_type, id, doc):
    body = {
        'doc' : doc
    }
    res = es.update(index=index, id=id, body=body, doc_type = doc_type)

json_data = search("the_evolving_forest")

df = json_normalize(json_data['hits']['hits']) #es에서 읽은 데이터를 dataframe으로 생성

print(df)
