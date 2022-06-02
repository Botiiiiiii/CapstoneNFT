import pandas as pd
import os

# from 열 카운트 후 평균
def from_group_count_avg(Group: pd.DataFrame):
    df = pd.DataFrame(Group)
    count_df = df.groupby("From")['From'].count().values
    return sum(count_df)/len(count_df)

# to 열 카운트 후 평균
def to_group_count_avg(Group: pd.DataFrame):
    df = pd.DataFrame(Group)
    count_df = df.groupby("To")['To'].count().values
    return sum(count_df)/len(count_df)

def to_group_count_len(Group: pd.DataFrame):
    df = pd.DataFrame(Group)
    count_df = df.groupby("To")['To'].count().values
    return len(count_df)

# tokenID 열 카운트 후 평균
def token_count_avg(Group: pd.DataFrame):
    df = pd.DataFrame(Group)
    count_df = df.groupby("TokenID")['TokenID'].count().values
    return sum(count_df)/len(count_df)

# Market Place 열에서 값 하나 추출하기
def get_market_name(Group: pd.DataFrame):
    return Group.values[0]


def get_market_average_dataframe(token_df : pd.DataFrame):

    market_group = token_df.groupby("Market Place")
    market_group_list = list(market_group.groups.keys())
    market_group_list.remove("None")

    market_average_df = pd.DataFrame()
    for market_name in market_group_list:
        market_date_group = market_group.get_group(market_name).groupby("TimeStamp")

        print(market_date_group)

        market_date_average = market_date_group.agg({"Market Place":[('Market Place',get_market_name)],"From":[('Sale Count Average',from_group_count_avg)],"To":[("Purchase Count Average",to_group_count_avg)],
                               "TokenID":[('Token count Average',token_count_avg)],'Value' :[('Value Average','mean')] })

        # print(market_date_average)

        # multiIndex 제거 후 컬럼 순서 재배치
        market_date_average.columns = market_date_average.columns.droplevel(0)
        market_date_average.reset_index(drop=False,inplace=True)
        market_date_average.rename(columns = {'TimeStamp' : 'Date'}, inplace = True)
        market_date_average = market_date_average.reindex(
            columns=['Market Place','Date','Sale Count Average','Purchase Count Average','Token count Average','Value Average'])

        market_average_df = market_average_df.append(market_date_average)

    market_average_df.to_csv("market_average_df.csv", index=False)
    return market_average_df

def get_minting_average_dataframe(token_df : pd.DataFrame,token_name):

    minting_date_group = token_df[token_df['From'].str.slice(start=0, stop=10) == "Black Hole"].groupby('TimeStamp')

    minting_date_average_df = minting_date_group.agg({"To": [("Minting Address Count",to_group_count_len),("Minting Average", to_group_count_avg)], "TokenID":[("Total Minting Count",len)] })

    minting_date_average_df.columns = minting_date_average_df.columns.droplevel(0)
    minting_date_average_df.reset_index(drop=False, inplace=True)
    minting_date_average_df.rename(columns={'TimeStamp': 'Date'}, inplace=True)


    '''minting_date_average_df = minting_date_average_df.reindex(
        columns=['Market Place', 'Date', 'Sale Count Average', 'Purchase Count Average', 'Token count Average',
                 'Value Average'])'''
    minting_date_average_df.to_csv("minting_date_average_df.csv", index=False)

def main():
    
    path = "classification/"
    file_list = os.listdir(path)

    token_df = pd.read_csv("classification/"+file_list[0],encoding= 'unicode_escape')
    token_name = file_list[0]

    # print(token_df)
    date_filter = lambda x: x[0:10]
    token_df['TimeStamp'] = token_df['Timestamp'].apply(date_filter)

    get_market_average_dataframe(token_df)
    # get_minting_average_dataframe(token_df,token_name)


main()
