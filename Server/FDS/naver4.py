from selenium import webdriver
from bs4 import BeautifulSoup 
import csv
import os
import re
import sys
import argparse
#
# parser = argparse.ArgumentParser()
#
# parser.add_argument('--keyword', required=True, help = "검색할 키워드")
# parser.add_argument('--start', required=True, help = "시작할 날짜")
# parser.add_argument('--end', required=True, help = "끝나는 날짜")
#
# args = parser.parse_args()
#
# keyword = args.keyword
# start_date = args.start
# end_date = args.end

keyword = "디지털 마약"
start_date = "20180930"
end_date = "20220602"



driver = webdriver.Chrome()

url = "https://search.naver.com/search.naver?where=news&query={0}&sort=2&nso=so:da,"\
            "p:from{1}to{2},a:all&field=1".format(keyword, start_date, end_date)

infos = []

driver.get(url)
driver.implicitly_wait(10)


cnt =
before_date = ""

while True :
    html = driver.page_source
    bs = BeautifulSoup(html, 'html.parser')
    infos += bs.find("ul", class_="list_news").find_all("li")    
    next_page = bs.find("div", class_="sc_page").find_all("a")[-1]

    if((next_page["aria-disabled"] != "false") and (cnt != 400)) :
        break

    cnt += 1
    if(cnt <= 400) :
        driver.get("https://search.naver.com/search.naver" + next_page["href"])
        driver.implicitly_wait(10)
    else :
        temp_date = infos[-1].find("div", class_ = "info_group").find("span", class_="info").text
        temp_date = temp_date.replace(".","")
        if(temp_date == before_date) :
            if(int(end_date) == int(temp_date)) :
                break
            else :
                temp_date = str(int(temp_date) + 1)
        
        before_date = temp_date

        url = "https://search.naver.com/search.naver?where=news&query={0}&sort=2&nso=so:da,"\
            "p:from{1}to{2},a:all&field=1".format(keyword, temp_date, end_date)
        driver.get(url)
        driver.implicitly_wait(10)
        cnt = 1


print("Finish crawl infos")

# 중복 제거 코드
temp = []

for i in infos :
    if (i not in temp) :
        temp.append(i)

infos = temp

result = []

print("Start get info")

for info in infos :
    link = info.find("a", class_ = "news_tit")["href"]
#    link = info.find("dl").find("a")["href"]
    title = info.find("a", class_ = "news_tit")["title"]
#    title = info.find("dl").find("a")["title"]
    if (info.find("div", class_ = "info_group").find("a", class_="info press") == None) :
        continue
    where = info.find("div", class_ = "info_group").find("a", class_="info press").text
    date = info.find("div", class_ = "info_group").find_all("span", class_="info")
    if(len(date) > 1) :
        date = date[-1].text
    else :
        date = date[0].text
    
    
#    temp = info.find("dl").find("dd").text
    result.append([date, title, where, link])
    print([date, title, where, link])


    # try :
    #     pattern = '\d+.\d+.\d+.'
    #     r = re.compile(pattern)
    #     date = r.search(temp).group(0)
    # except AttributeError:
    #     pattern = '\w* (\d\w*)' 
    #     r = re.compile(pattern)
    #     date = r.search(temp).group(1)

# 파일 저장 하기

if not(os.path.isdir(keyword)) :
    os.makedirs(os.path.join(keyword))

f = open("{0}/naver_{1}_{2}.csv".format(keyword, start_date, end_date), "w", newline="", encoding="utf-8-sig")

wr = csv.writer(f)

wr.writerow(["date", "title", "where", "link"])

for i in result :
    wr.writerow(i)

f.close()
driver.quit()






