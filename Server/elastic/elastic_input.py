from elasticsearch import Elasticsearch
from elasticsearch.helpers import bulk
from elasticsearch import helpers
from datetime import datetime
import pandas as pd
import os
import time

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
        data = {"match_all": {}}
    else:
        data = {"match": data}
    body = {"query": data}
    res = es.search(index=index)

    return res

def update(index, doc_type, id, doc):
    body = {
        'doc' : doc
    }
    res = es.update(index=index, id=id, body=body, doc_type = doc_type)

es = Elasticsearch('http://192.168.65.128:9200/')

path = "scorecheck_df.csv" #파일명

pd.set_option('display.max_colwidth', None)

df = pd.read_csv(path)



#pandas dataframe to es main
def doc_genrator(df):
    df_iter = df.iterrows()
    cnt = 1
    for index, document in df_iter:
        # print(cnt)
        # cnt = cnt + 1
        yield{
            "_index": 'test_data',
            "_type": "_doc",
            "_id":f"{cnt}",
            "_source": filterKeys(document),
        }

use_these_keys = ['wallet','week trade count','week value sum','week value average','day trade count','day value sum','day value average','hour trade count','hour value sum','hour value average','single cycle number','multi cycle number','cycle wallets','trade count','value sum','value average','score','type']

def filterKeys(document):
    return {key:document[key] for key in use_these_keys}

helpers.bulk(es,doc_genrator(df))


#dataframe to es 1
'''es.indices.create(index='bulk_test', body={})

df = pd.DataFrame(data={'From': df['From'],
                        'To': df['To'],
                        'Value': df['Value']})

documents = df.to_dict(orient='records')
bulk(es, documents, index='bulk_test', doc_type='foo', raise_on_error=True)'''



#엘라스틱 단일 index 삽입
'''doc = {
    'author': 'kimchy',
    'text': 'Elasticsearch: cool. bonsai cool.',
    'timestamp': datetime.now(),
}
res = es.index(index="test-index", id=1, document=doc)
print(res['result'])

res = es.get(index="test-index", id=1)
print(res['_source'])

es.indices.refresh(index="test-index")

res = es.search(index="test-index", query={"match_all": {}})
print("Got %d Hits:" % res['hits']['total']['value'])
for hit in res['hits']['hits']:
    print("%(timestamp)s %(author)s: %(text)s" % hit["_source"])'''