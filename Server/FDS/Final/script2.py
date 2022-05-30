#!/usr/bin/env python
# coding: utf-8

# In[ ]:

from txnalyz import *
path = "classification/"
file_list = os.listdir(path)
for token_name in file_list:
    ta = txnalyz()
    ta.read_csv('classification/'+token_name)
    # ta.read_csv('testcase/test10.csv')
    ta.init()
    ta.init_route(3, mode='cycle')

    ta.show_networkx_graph()
    ta.write_csv(ta.get_txlist(), 'cycle_'+token_name)