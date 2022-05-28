#!/usr/bin/env python
# coding: utf-8

# In[ ]:


import pandas as pd
import os

path = "carry/"
file_list = os.listdir(path)


for token_name in file_list :
    print("현재 파일 : ",token_name)
    token_df = pd.read_csv("carry/" + token_name)
    market_place_df = pd.read_csv("marketplace_list.csv")

    market_name = []
    transaction_type = []

    for i in range(len(token_df)):
        
        for e in range(len(market_place_df)):
            if token_df.loc[i]['Contract Address'] == market_place_df.loc[e]['Address']:
                market_name.append(market_place_df.loc[e]['Market Place'])         
        if i+1 != len(market_name):
            market_name.append("None")
            
        if token_df.loc[i]['From'][0:10] != "Black Hole" and token_df.loc[i]['To'][0:10] != "Black Hole" and token_df.loc[i]['Value'] != 0 and market_name[i] != "None":
            transaction_type.append("Trading")
        elif token_df.loc[i]['From'][0:10] == "Black Hole" or token_df.loc[i]['From'][0:12] == "NULL Address":
            transaction_type.append("Minting")
        elif token_df.loc[i]['To'][0:10] == "Black Hole" or token_df.loc[i]['To'][0:12] == "NULL Address":
            transaction_type.append("Destroy")
        else :
            transaction_type.append("Transfer")

    token_df['Type'] = transaction_type
    token_df['Market Place'] = market_name
    token_df.to_csv("classification/"+token_name,index=False)
