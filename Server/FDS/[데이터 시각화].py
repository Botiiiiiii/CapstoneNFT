#!/usr/bin/env python
# coding: utf-8

# In[ ]:


import matplotlib.pyplot as plt
import networkx as nx
import pandas as pd
import os

path = "classification/"
file_list = os.listdir(path)

token_df = pd.read_csv("classification/"+file_list[0])

# 민팅(블랙홀) 트랜잭션 제거
idx = token_df[token_df['From'].str.slice(start=0,stop=10) == "Black Hole"]['From'].index
token_df.drop(idx,inplace=True)

G = nx.Graph(directed=True)
G = nx.from_pandas_edgelist(token_df,source ='From', target = 'To')
print(nx.info(G))

plt.figure(figsize=(50, 50))
pos = nx.spring_layout(G, k = 0.15)
nx.draw_networkx(G,pos,with_labels = False, node_size = 10, node_color = 'green')
plt.show()
