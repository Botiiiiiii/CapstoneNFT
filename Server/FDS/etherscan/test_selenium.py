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


import time
import numpy as np
from selenium import webdriver

URL_token = 'https://etherscan.io/token/'
infura_url = []

infura_url.append("https://mainnet.infura.io/v3/1c71753252df499cab6273d110ab7e52")
infura_url.append("https://mainnet.infura.io/v3/f6001581b0f34af7abe1af48dda3d835")
infura_url.append("https://mainnet.infura.io/v3/72eb988bdc3d432fa65f2130f1444516")
infura_url.append("https://mainnet.infura.io/v3/3137bd8a2c4a40228f47f4ef18ce24b0")
infura_url.append("https://mainnet.infura.io/v3/c7929b3eaa4e4a38924b8f5dc02c8a43")
infura_url.append("https://mainnet.infura.io/v3/16bd8d41c1084ee1aba7404e088f1670")
infura_url.append("https://mainnet.infura.io/v3/fd567b901f3648128074dfb97c3dfb99")
infura_url.append("https://mainnet.infura.io/v3/8b5d96ae74ec43059b32740f7ccfe595")
infura_url.append("https://mainnet.infura.io/v3/5690a3fd70bf4afca7342d5748af164b")
infura_url.append("https://mainnet.infura.io/v3/aaf7776d32504a87a4d7b62fedda9bdc")

web3 = []
for i in range(len(infura_url)):
    web3.append(Web3(Web3.HTTPProvider(infura_url[i])))

def crawling_data_ether(tx_address, Name, web3_num, lastpage,):
    w_num = web3_num
    raw_data = {
        'Txn Hash': [],
        'Method': [],
        'Timestamp': [],
        'From': [],
        'To': [],
        'TokenID': []
    }
    # 드라이버 연결
    driver = webdriver.Chrome()
    driver.maximize_window()
    # 카운팅
    cnt = 0
    try:
        # 웹사이트 이동
        driver.get(URL_token + tx_address)
        # 로딩 대기
        driver.implicitly_wait(5)

        WebDriverWait(driver, 10).until(EC.frame_to_be_available_and_switch_to_it((By.ID, "tokentxnsiframe")))
        elements = WebDriverWait(driver, 20).until(EC.visibility_of_all_elements_located((By.XPATH, '//*[@id="maindiv"]/div[2]/table/tbody/tr[1]/td[2]/span/a')))

        driver.find_element(By.XPATH,'//*[@id="maindiv"]/div[1]/nav/ul/li[4]/a').click()
        driver.implicitly_wait(5)
        time.sleep(10)

    except:
        print("crawling data error")



def main():

    token_list_df = pd.read_csv('token_contract_list.csv')
    web3_num = 0
    for i in range(len(token_list_df)):
        if web3_num >= 10:
            web3_num = 0
        print("현재 web3_num", web3_num)
        print("현재 진행상황 : ", i)

        token_contract = token_list_df.loc[i]['contract']
        address = token_contract

        url = "https://etherscan.io/token/" + str(address)
        # 드라이버 연결
        driver = webdriver.Chrome()
        # 웹사이트 이동
        driver.get(URL_token + address)
        # 로딩 대기
        driver.implicitly_wait(5)

        WebDriverWait(driver, 10).until(EC.frame_to_be_available_and_switch_to_it((By.ID, "tokentxnsiframe")))
        elements = WebDriverWait(driver, 20).until(EC.visibility_of_all_elements_located(
            (By.XPATH, '//*[@id="maindiv"]/div[2]/table/tbody/tr[1]/td[2]/span/a')))

        lastpage = driver.find_element(By.XPATH,'//*[@id="maindiv"]/div[1]/nav/ul/li[3]/span/strong[2]').get_attribute('innerText')

        print('마지막 페이지:', lastpage)
        driver.quit()
        token_name = re.sub(r"[?!$/'.,]", "", token_list_df.loc[i]['Token'])
        if len(token_name) > 151:
            token_name = token_name[0:150]

        crawling_data_ether(token_contract, token_name, web3_num, int(lastpage))
        web3_num = web3_num + 1


main()
