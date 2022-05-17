#!/usr/bin/env python
# coding: utf-8

# In[ ]:


import pandas as pd
import requests
from bs4 import BeautifulSoup
import os
import time

# 컨트랙트 리스트 크롤링
def get_tokenpage(page) :
    p = 'p='+str(page)
    url = 'https://etherscan.io/tokens-nft?sort=7d&order=desc&ps=100&'+p
    headers = {'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36'}
    response = requests.get(url, headers=headers)
    html = response.text
    soup = BeautifulSoup(html, 'html.parser')
    tables = soup.select('table')
    table = tables[0]
    table_html = str(table)

    numcount = (page-1)*100
    contract_address = []
    
    for i in (soup.select('a.text-primary')):
        try:
            contract_address.append(i['title'])
        except:
            contract_address.append(i.text[0:42])
        numcount = numcount + 1
        print(numcount)
        
    table_df_list = pd.read_html(table_html)
    table_df = table_df_list[0]
    table_df['contract'] = contract_address


    if not os.path.exists('token_contract.csv'):
        table_df.to_csv('token_contract.csv', index=False, mode='w', encoding='utf-8-sig')
    else:
        table_df.to_csv('token_contract.csv', index=False, mode='a', encoding='utf-8-sig', header=False)

endpage =2
startpage = 1
for i in range(startpage,endpage+1):
    print(i)
    get_tokenpage(i)
    time.sleep(1)
