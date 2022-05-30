import pandas as pd
import os
import os
import re

import requests
from bs4 import BeautifulSoup as bs, element
from web3 import eth, contract, Web3
import time
import pandas as pd


import selenium
from selenium import webdriver
from selenium.webdriver import ActionChains

from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By

from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver import ActionChains


path = "re/"
file_list = os.listdir(path)
URL_token = 'https://etherscan.io/tx/'


for token_name in file_list:
    print("현재 파일 : ", token_name)
    token_df = pd.read_csv("re/" + token_name)
    # 드라이버 연결
    driver = webdriver.Chrome()
    token_re = []
    for i in range(len(token_df)):
        # 웹사이트 이동
        driver.get(URL_token + token_df['Txn Hash'][i])
        # 로딩 대기
        driver.implicitly_wait(5)
        print('----')
        TokenID = 'ID'+driver.find_element(By.XPATH, '//*[@id="wrapperContent"]/li/div[1]/div[2]/span[3]/a/span').get_attribute('innerText')
        raw_data = {
            'Txn Hash': [token_df['Txn Hash'][i]],
            'Method': [token_df['Method'][i]],
            'Timestamp': [token_df['Timestamp'][i]],
            'From': [token_df['From'][i]],
            'To': [token_df['To'][i]],
            'TokenID': [TokenID],
            'Value':[token_df['Value'][i]],
            'Type':[token_df['Type'][i]],
            'Market Place':[token_df['Market Place'][i]]
        }
        tmp_df = pd.DataFrame(raw_data)

        if not os.path.exists("re/ID_" + token_name):
            tmp_df.to_csv("re/ID_" + token_name, index=False, mode='w', encoding='utf-8-sig')
        else:
            tmp_df.to_csv("re/ID_" + token_name, index=False, mode='a', encoding='utf-8-sig', header=False)
