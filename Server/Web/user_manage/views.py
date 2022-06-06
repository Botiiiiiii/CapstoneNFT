import json
import os
from pyvis import network as net
import pandas as pd
from django.shortcuts import render
from django.http import HttpResponse
from .models import User

def index(request):
    # 컨텍스트 데이터프레임으로 넘겨주기
    # response_score = es.get(index='scorecheck_df', id=2)
    # wallet_score_df = pd.DataFrame(response_score['_source'])
    # wallet_score_df = pd.read_csv("./static/wallet/scorecheck_df.csv", encoding='utf-8')
    # wallet_score_df = wallet_score_df[['type','score','wallet','trade count','value sum','value average','single cycle number','multi cycle number']]
    # print(wallet_score_df)
    # wallet_score_df['wallet'] = wallet_score_df['wallet'].apply(lambda x: f'<a href="/table/wallet?wallet={x}">{x}</a>')
    # wallet_score_table = wallet_score_df.to_html(index=False,table_id='datatablesSimple',render_links=True,escape=False)

    users = User.objects.all().values()
    user_df = pd.DataFrame(users)
    user_df = user_df[['nickname', 'address', 'alert', 'profile_img']]
    user_df['address'] = user_df['address'].apply(lambda x: f'<a href="/manage/user?addr={x}">{x}</a>')
    # print(user_df)
    table = user_df.to_html(index=False,table_id='datatablesSimple',render_links=True,escape=False)
    # print(table)


    return render(request, 'user_manage/index.html',context={'user_table':table})

def user(request):
    if request.method == "GET" :
        user_addr = request.GET.get('addr', None)

        users = User.objects.all().values()
        user_df = pd.DataFrame(users)
        user_df = user_df[['nickname', 'address', 'alert', 'profile_img']]

        user_img = user_df[user_df.address == user_addr].profile_img.iloc[0]
        nickname = user_df[user_df.address == user_addr].nickname.iloc[0]
        alert = user_df[user_df.address == user_addr].alert.iloc[0]

        return render(request, 'user_manage/user.html', context={'user_profile_img':user_img, 'nickname':nickname, 'address':user_addr, 'alert':alert})
    elif request.method == "POST" and request.headers.get('x-requested-with') == 'XMLHttpRequest':
        req_data = json.loads(request.body)
        user_address = req_data['address']
        function_type = req_data['type']

        if function_type == 'block_user':
            user = User.objects.get(address = user_address)
            user.alert = 9
            user.save()

            return HttpResponse(json.dumps({'res': 'block'}), content_type="application/json")
        elif function_type == 'unblock_user':
            user = User.objects.get(address = user_address)
            user.alert = 0
            user.save()

            return HttpResponse(json.dumps({'res': 'unblock'}), content_type="application/json")


        
    