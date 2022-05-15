import selenium
from selenium import webdriver
from selenium.webdriver import ActionChains

from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By

from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver import ActionChains


import pandas as pd
import requests
import json
import time
import numpy as np
import requests
import json
import os

kas_url = "https://wallet-api.klaytnapi.com/v2/tx/"
headers = {
  'x-chain-id': '8217',
  'Authorization': 'Basic S0FTS0lPUFRIUkdST0c3UUdWMDJYTTRWOjFMWGMzLWVHc1hUQ2FLZEhhdmJTVEpRX3Fnd05MbWg1NTU1U3NBVUU=',
  'Content-Type': 'application/json'
}

URL_account = 'https://scope.klaytn.com/account/'
URL_tx = 'https://scope.klaytn.com/tx/'
ContractAddress = '0x41cff281b578f4cf45515d6e4efd535e47e76efd?tabId=txList'
tmp = np.array([])

# 라이브러리 불러오기
from selenium import webdriver



def crawling_tx(num):
    tx_list = []
    # 드라이버 연결
    driver = webdriver.Chrome()

    try:
        for j in range(1,num):
            # 웹사이트 이동
            driver.get(URL_account + ContractAddress +'?version=2&page='+str(j))
            # 로딩 대기
            driver.implicitly_wait(5)
            # 테이블
            table = driver.find_element(By.XPATH,
                                        '//*[@id="root"]/div/div[2]/div[2]/div/div/div[4]/div/div[2]/div/div[1]/div[2]')
            for i in range(1,26):
                    Xpath_Tx_hash_link = '//*[@id="root"]/div/div[2]/div[2]/div/div/div[4]/div/div[2]/div/div[1]/div[2]/div/div[' + str(i) + ']/div[1]/div/a' #href

                    Tx_hash_link = table.find_element(By.XPATH,Xpath_Tx_hash_link).get_attribute('href')
                    tx_list.append(Tx_hash_link)

        return tx_list

    except:
        print('crawling error')

    finally:
        # 브라우저 (드라이버) 종료
        driver.quit()


def crawling_data(tx_data):
    data_list = []
    # 드라이버 연결
    driver = webdriver.Chrome()

    try:
        for i in tx_data:
            # 웹사이트 이동
            driver.get(i+'?tabId=tokenTransfer')
            # 로딩 대기
            driver.implicitly_wait(10)
            # 테이블 서치
            table = driver.find_element(By.XPATH,'//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[1]/div[2]')
            Timestamp = driver.find_element(By.XPATH,'//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[1]/div/div[1]/div[6]/div[2]/span').get_attribute('innerText')
            trans_num_check = driver.find_element(By.XPATH,'//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[1]/div/div[1]/div[4]/div[2]').get_attribute('innerText')
            try:
                if(trans_num_check == '4'):
                    Xpath_From_Address = '//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[1]/div[2]/div/div[4]/div[3]/div/span[1]/div/a'  # href
                    Xpath_To_Address = '//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[1]/div[2]/div/div[4]/div[3]/div/span[3]/div/a' #href
                    Xpath_To_Value = '//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[1]/div[2]/div/div[4]/div[5]/div[1]/span[1]'
                    From_Address_link = table.find_element(By.XPATH, Xpath_From_Address).get_attribute('href')
                    From_Address = From_Address_link[33:]
                    To_Address_link = table.find_element(By.XPATH, Xpath_To_Address).get_attribute('href')
                    To_Address = To_Address_link[33:]
                    Value = table.find_element(By.XPATH, Xpath_To_Value).get_attribute('innerText')

                else:
                    Xpath_From_Address = '//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[1]/div[2]/div/div[3]/div[3]/div/span[1]/div/a'  # href
                    Xpath_To_Address = '//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[1]/div[2]/div/div[3]/div[3]/div/span[3]/div/a'  # href
                    Xpath_To_Value = '//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[1]/div[2]/div/div[3]/div[5]/div[1]/span[1]'
                    From_Address_link = table.find_element(By.XPATH, Xpath_From_Address).get_attribute('href')
                    From_Address = From_Address_link[33:]
                    To_Address_link = table.find_element(By.XPATH, Xpath_To_Address).get_attribute('href')
                    To_Address = To_Address_link[33:]
                    Value = table.find_element(By.XPATH, Xpath_To_Value).get_attribute('innerText')
            except:
                print(i)


            driver.find_element(By.XPATH, '//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[2]/div/div[1]/div[2]').click() #NFT Transfers 클릭
            driver.implicitly_wait(5)
            Token_name = driver.find_element(By.XPATH,'//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[1]/div[2]/div/div/div[4]/a/div/span[2]').get_attribute('innerText')
            Token_Id = driver.find_element(By.XPATH,'//*[@id="root"]/div/div[2]/div[2]/div/div/div/div[2]/div/div[2]/div/div[1]/div[2]/div/div/div[5]/a').get_attribute(('innerText'))

            data_list.append(i[28:])
            data_list.append(Timestamp)
            data_list.append(From_Address)
            data_list.append(To_Address)
            data_list.append(Token_Id)
            data_list.append(Token_name)
            data_list.append('0x41cff281b578f4cf45515d6e4efd535e47e76efd')
            data_list.append(Value)
            print(Value)

    except:
        print("crawling data error")

    finally:
        # 브라우저 (드라이버) 종료
        driver.quit()
        return data_list

def create_csv(data_df):
    i=0
    Name = 'tmp'
    csv_name = Name + ".csv"
    if not os.path.exists("token/" + csv_name):
        data_df.to_csv("token/" + csv_name, index=False, mode='w', encoding='utf-8-sig')
    else:
        data_df.to_csv("token/" + csv_name, index=False, mode='a', encoding='utf-8-sig', header=False)
    i += 1


num=3
crawl_tx = crawling_tx(num)
crawl_data = crawling_data(crawl_tx)
# print(crawl_data)
tmp = np.append(tmp,crawl_data).reshape((num-1)*25,8)
df = pd.DataFrame(tmp,columns=['Txn Hash', 'Timestamp', 'From', 'To', 'TokenID','Token_Name','Contract_Address','Value'])
print(df)
create_csv(df)









#print(table.text)

# # 바디
# for tr in table.find_elements(By.CLASS_NAME,'Table__tr'):
#     href = tr.find_element(By.XPATH,'//*[@id="root"]/div/div[2]/div[2]/div/div/div[2]/div/div[2]/div/div[1]/div[2]/div/div[1]/div[1]').get_attribute('href')
#     block_num = tr.find_element(By.XPATH,'').get_attribute('innerText')
#     data_list.append(href)
# #print(data_list)


