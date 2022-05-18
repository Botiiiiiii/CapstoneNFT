import os
import re

import requests
from bs4 import BeautifulSoup as bs, element
import web3
import time
import pandas as pd
from web3 import eth, Web3, contract


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


kas_url = "https://wallet-api.klaytnapi.com/v2/tx/"

URL_account = 'https://scope.klaytn.com/account/'
URL_tx = 'https://scope.klaytn.com/tx/'
ContractAddress = '0x41cff281b578f4cf45515d6e4efd535e47e76efd?tabId=txList'
tmp = np.array([])

# 라이브러리 불러오기
from selenium import webdriver

global error
error = 0


def get_Token_soup(address, page):
    p = page
    url = "https://etherscan.io/token/generic-tokentxns2?contractAddress=" + address + "&mode=&sid=ad30bae7c2017834eabe9b8d33a1de43&m=normal&p=" + str(
        p)

    page = requests.get(url, headers=headers)
    if error >= 1:
        print("error : 1")
        time.sleep(1)
        if error >= 2:
            print("error : 2")
            time.sleep(2)

    soup = bs(page.text, "html.parser")
    return soup


def get_Token_Dataframe(arg_soup, w_num):
    soup = arg_soup
    tables = soup.select('table')
    table = tables[0]
    table_html = str(table)

    table_df_list = pd.read_html(table_html)
    table_df = table_df_list[0]

    table_df.pop("Unnamed: 0")
    table_df.pop("Unnamed: 5")
    table_df.pop("Unnamed: 8")

    value = []
    tx_to = []
    for i in range(len(table_df)):
        transaction = web3[w_num].eth.getTransaction(table_df.loc[i]['Txn Hash'])
        value.append(transaction['value'] / 1000000000000000000)
        tx_to.append(transaction['to'])
    timestamp = [x.get_text() for x in soup.find_all(class_='showDate')]

    table_df.rename(columns={'Unnamed: 3': 'TimeStamp'}, inplace=True)
    table_df['TimeStamp'] = timestamp
    table_df['Contract Address'] = tx_to
    table_df['value'] = value

    return table_df


def create_csv(Token_address, Name, web3_num, pagenum):
    address = Token_address
    csv_name = Name + ".csv"
    w_num = web3_num
    lastpage = pagenum

    for i in range(lastpage):

        print(Name, "현재페이지 :", i + 1)

        # web3 초과 요청 방지
        if i + 1 % 500 == 0:
            if w_num < 9:
                w_num = w_num + 1
                print("change web3 : ", w_num)
            else:
                w_num = 0
                print("change web3 : ", w_num)

        global error
        try:
            soup = get_Token_soup(address, i + 1)
            df = get_Token_Dataframe(soup, w_num)
        except:
            try:
                error = 1
                soup = get_Token_soup(address, i + 1)
                df = get_Token_Dataframe(soup, w_num)
                error = 0
            except:
                error = 2
                soup = get_Token_soup(address, i + 1)
                df = get_Token_Dataframe(soup, w_num)
                error = 0

        if not os.path.exists("token/" + csv_name):
            df.to_csv("token/" + csv_name, index=False, mode='w', encoding='utf-8-sig')
        else:
            df.to_csv("token/" + csv_name, index=False, mode='a', encoding='utf-8-sig', header=False)


def main():
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

    token_list_df = pd.read_csv('token_contract_list.csv')

    web3_num = 0
    for i in range(len(token_list_df)):
        if web3_num >= 10:
            web3_num = 0
        print("현재 web3_num", web3_num)
        print("현재 진행상황 : ", i)

        token_contract = token_list_df.loc[i]['contract']
        address = token_contract
        p = 1
        url = "https://etherscan.io/token/generic-tokentxns2?contractAddress=" + address + "&mode=&sid=ad30bae7c2017834eabe9b8d33a1de43&m=normal&p=" + str(
            p)

        page = requests.get(url, headers=headers)
        time.sleep(7)
        soup = bs(page.text, "html.parser")
        lastpage = soup.select_one(
            '#maindiv > div.table-responsive.mb-2.mb-md-0 > div > div > ul > li.page-item.disabled > span > strong:nth-child(2)').text
        token_name = re.sub(r"[?!$/'.,]", "", token_list_df.loc[i]['Token'])
        if len(token_name) > 151:
            token_name = token_name[0:150]

        create_csv(token_contract, token_name, web3_num, int(lastpage))
        web3_num = web3_num + 1


main()
