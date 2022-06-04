import pandas as pd
import os
import os
import re

import requests
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

options = webdriver.ChromeOptions()
options.add_argument('headless')
options.add_argument("window-size=1440x900")
options.add_argument("user-agent= Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36")
path = "re/"
file_list = os.listdir(path)
URL_token = 'https://etherscan.io/tx/'


for token_name in file_list:
    print("현재 파일 : ", token_name)
    token_df = pd.read_csv("re/" + token_name)
    # 드라이버 연결
    driver = webdriver.Chrome(options=options)
    token_re = []
    for i in range(14145,len(token_df)+1):
        print('행 수: ',i)
        # 웹사이트 이동
        try:
            driver.get(URL_token + token_df['Txn Hash'][i])
            # 로딩 대기
            driver.implicitly_wait(5)
            # element = WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, "ContentPlaceHolder1_maintable")))
        except:
            driver.quit()
            time.sleep(1)
            print("time sleep1")
            driver = webdriver.Chrome()
            driver.get(URL_token + token_df['Txn Hash'][i])
            # 로딩 대기
            driver.implicitly_wait(5)
            # element = WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, "ContentPlaceHolder1_maintable")))
        try:
            TokenID = 'ID'+driver.find_element(By.XPATH, '//*[@id="wrapperContent"]/li/div[1]/div[2]/span[3]/a/span').get_attribute('innerText')
        except:
            driver.quit()
            time.sleep(1)
            print("time sleep2")
            driver = webdriver.Chrome(options=options)
            # 웹사이트 이동
            driver.get(URL_token + token_df['Txn Hash'][i])
            # 로딩 대기
            driver.implicitly_wait(5)
            # element = WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, "ContentPlaceHolder1_maintable")))
            try:
                TokenID = 'ID' + driver.find_element(By.XPATH,
                                                     '//*[@id="wrapperContent"]/li/div[1]/div[2]/span[3]/a/span').get_attribute(
                    'innerText')
            except:
                driver.quit()
                time.sleep(1)
                print("time sleep3")
                driver = webdriver.Chrome(options=options)
                # 웹사이트 이동
                driver.get(URL_token + token_df['Txn Hash'][i])
                # 로딩 대기
                driver.implicitly_wait(5)
                # element = WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, "ContentPlaceHolder1_maintable")))
                TokenID = 'ID' + driver.find_element(By.XPATH,
                                                     '//*[@id="wrapperContent"]/li/div[1]/div[2]/span[3]/a/span').get_attribute(
                    'innerText')
        print(TokenID)
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
        if not os.path.exists("TokenID_result/ID_" + token_name):
            tmp_df.to_csv("TokenID_result/ID_" + token_name, index=False, mode='w', encoding='utf-8-sig')
        else:
            tmp_df.to_csv("TokenID_result/ID_" + token_name, index=False, mode='a', encoding='utf-8-sig', header=False)
        del raw_data