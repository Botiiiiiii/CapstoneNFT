import json
import os
from pyvis import network as net
import pandas as pd
import matplotlib.pyplot as plt
from django.shortcuts import render
from django.http import HttpResponse
from txnalyz_scoring import *
from PIL import Image

from elasticsearch import Elasticsearch

es = Elasticsearch('http://192.168.65.128:9200/')

def index(request):
    # 컨텍스트 데이터프레임으로 넘겨주기
    # response_score = es.get(index='scorecheck_df', id=2)
    # wallet_score_df = pd.DataFrame(response_score['_source'])
    wallet_score_df = pd.read_csv(
        "C:/Users/82106/Documents/GitHub/CapstoneNFT/Server/Web/static/wallet/scorecheck_df.csv", encoding='utf-8')
    wallet_score_df = wallet_score_df[['type','score','wallet','trade count','value sum','value average','single cycle number','multi cycle number']]
    wallet_score_df['wallet'] = wallet_score_df['wallet'].apply(lambda x: f'<a href="/table/wallet?wallet={x}">{x}</a>')
    wallet_score_table = wallet_score_df.to_html(index=False,table_id='datatablesSimple',render_links=True,escape=False)


    return render(request, 'fds_monitoring/index.html',context={'wallet_score_table':wallet_score_table})

def wallet(request):
    if request.method == "GET" :
        wallet_name = request.GET.get('wallet', None)

        # 데이터프레임 생성
        # response_score = es.get(index='scorecheck_df', id=2)
        # wallet_score_df = pd.DataFrame(response_score['_source'])
        wallet_score_df = pd.read_csv("C:/Users/82106/Documents/GitHub/CapstoneNFT/Server/Web/static/wallet/scorecheck_df.csv", encoding='utf-8')
        wallet_info = wallet_score_df[wallet_score_df['wallet'] == wallet_name]

        trade = int(wallet_info['trade count'].agg('sum'))
        single_cycle = int(wallet_info['single cycle number'].agg('sum'))
        multi_cycle = int(wallet_info['multi cycle number'].agg('sum'))

        wallet_name = str(wallet_name)
        return render(request, 'fds_monitoring/wallet.html', context={'wallet':wallet_name,'tr':trade,'si':single_cycle,
                                                            'mu':multi_cycle})
    elif request.method == "POST" and request.headers.get('x-requested-with') == 'XMLHttpRequest':
        req_data = json.loads(request.body)
        wallet_name = req_data['wallet'][:-1]
        function_type = req_data['type']

        if function_type == 'trade':
            return_df = get_trade_df(wallet_name).copy()
            return_table = return_df.to_html(index=False, escape=False, table_id='datatablesSimple2')
            return_graph =get_pyvis_graph(return_df)

        if function_type == 'single':
            return_df = get_single_cycle(wallet_name).copy()
            return_table = return_df.to_html(index=False, escape=False, table_id='datatablesSimple2')
            return_graph =get_pyvis_graph(return_df)

        if function_type == 'multi':
            return_df = get_multi_cycle(wallet_name).copy()
            return_table = return_df.to_html(index=False,escape=False,table_id='datatablesSimple2')
            return_graph = get_pyvis_graph(return_df)


        return HttpResponse(
            json.dumps({'multi_table': return_table,'multi_graph':return_graph}),
            content_type="application/json")


def get_trade_df(wallet_name):
    token_df = pd.DataFrame()
    token_list = ['animal_society','ape_harbour_yachts','dogex','metavillains','the_evolving_forest']
    for token_name in token_list:
        # es_response = es.get(index=token_name, id=2)
        # df = pd.DataFrame(es_response['_source'])
        df = pd.read_csv("C:/Users/82106/Documents/GitHub/CapstoneNFT/Server/Web/static/token/" + token_name +'.csv', encoding='utf-8')
        token_df = token_df.append(df)

    token_df.reset_index(drop=True, inplace=True)

    # blackhole, 전송(value=0) 제거
    blackhole = token_df[token_df['From'].str.slice(start=0, stop=10) == "Black Hole"]['From'].index
    token_df.drop(blackhole, inplace=True)
    transfer = token_df[token_df['Value'] == 0].index
    token_df.drop(transfer, inplace=True)

    # From그룹핑, To그룹핑
    From_group = token_df.groupby('From')
    From_group_keys = list(From_group.groups.keys())
    To_group = token_df.groupby('To')
    To_group_keys = list(To_group.groups.keys())

    exist_From = False
    exist_To = False
    # 그룹핑에서 지갑 주소 조회
    if wallet_name in From_group_keys:
        wallet_From = From_group.get_group(wallet_name)
        wallet_From['Type'] = '판매'
        exist_From = True

    if wallet_name in To_group_keys:
        wallet_To = To_group.get_group(wallet_name)
        wallet_To['Type'] = '구매'
        exist_To = True

    if exist_From & exist_To:
        return_df = wallet_From.append(wallet_To)
        return_df = return_df[['Type','From','To','Value','TokenID']].copy()
    elif exist_From:
        return_df = wallet_From[['Type','From','To','Value','TokenID']].copy()
    elif exist_To:
        return_df = wallet_To[['Type','From','To','Value','TokenID']].copy()

    return return_df

def get_single_cycle(wallet_name):
    # df 불러오기
    # response_cycle = es.get(index='all_cycle_df', id=2)
    # cycle_df = pd.DataFrame(response_cycle['_source'])
    cycle_df = pd.read_csv("C:/Users/82106/Documents/GitHub/CapstoneNFT/Server/Web/static/cycle/all_cycle_df.csv" ,encoding= 'utf-8')
    cycle_df = cycle_df[cycle_df['From']==wallet_name].copy()

    # 자전 거래만 추출
    single_cycle = cycle_df[cycle_df['Cycle Length'] == 1].copy()
    single_cycle['Type'] = '자전'
    return_df = single_cycle[['Type','From','To','Value','TokenID']]
    return return_df


def get_multi_cycle(wallet_name):
    # df 불러오기
    # response_cycle = es.get(index='all_cycle_df', id=2)
    # cycle_df = pd.DataFrame(response_cycle['_source'])
    cycle_df = pd.read_csv("C:/Users/82106/Documents/GitHub/CapstoneNFT/Server/Web/static/cycle/all_cycle_df.csv",
                           encoding='utf-8')

    # 자전 거래 제거
    single_cycle_index = cycle_df[cycle_df['Cycle Length'] == 1].index
    cycle_df.drop(single_cycle_index, inplace=True)

    cycle_df.reset_index(drop=True,inplace=True)
    # From 그룹핑 (지갑이 속한 cycle 넘버 조회용도)
    cycle_groups = cycle_df.groupby('From')
    wallet_cycle_df = cycle_groups.get_group(wallet_name)

    wallet_cycle_key = list(wallet_cycle_df.groupby(['Route Number','Cycle Number','Token Name']).groups.keys())
    cycle_df['Type'] = ""

    # print(wallet_cycle_key)
    return_df = pd.DataFrame()
    for i in range(len(wallet_cycle_key)):
        cycle = cycle_df[(cycle_df['Route Number'] == wallet_cycle_key[i][0]) &
                         (cycle_df['Cycle Number'] == wallet_cycle_key[i][1]) & 
                         (cycle_df['Token Name'] == wallet_cycle_key[i][2])].copy()

        cycle['Type'] = '순환'+str(i+1)
        return_df = return_df.append(cycle)

    return return_df[['Type','From','To','Value','TokenID']]

def get_pyvis_graph(df:pd.DataFrame):
    ta = txnalyz()
    ta.read_df(df)
    ta.init()
    ta.init_route(1)
    ta.show_networkx_graph()
    ta.networkx_png()

    return ta.show_networkx_graph()
