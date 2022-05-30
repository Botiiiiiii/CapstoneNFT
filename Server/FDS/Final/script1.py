#!/usr/bin/env python
# coding: utf-8

# In[ ]:

from txnalyz import *
import os
a = int(input("최대 홉수 입력:"))
path = "classification/"
file_list = os.listdir(path)
for token_name in file_list:

    ta = txnalyz()
    ta.read_csv('classification/'+token_name)
    # ta.read_csv('testcase/test10.csv')
    ta.init()
    ta.init_route(a)

    ta.show_networkx_graph()
    ta.write_csv(ta.get_txlist(), token_name)