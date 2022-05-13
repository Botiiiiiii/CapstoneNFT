import numpy as np
from pyvis import network as net
import pandas as pd
import use_elasticsearch as els

'''
순환 경로 데이터 프레임 저장 부분 : 295번째 줄
지갑점수 데이터프레임 저장 부분 : 354번째 줄(미완)
'''


# 폴더 파일 리스트
token_name = "the_evolving_forest"

# 데이터프레임 불러온 후 블랙홀 제거
token_df = els.get_df(token_name)
idx = token_df[token_df['From'].str.slice(start=0, stop=10) == "Black Hole"]['From'].index
token_df.drop(idx, inplace=True)

# From Group, To Group
From_group = token_df.groupby("From")
From_group_list = list(From_group.groups.keys())
To_group = token_df.groupby("To")
To_group_list = list(To_group.groups.keys())

search_list = [] # 중복 탐색 방지용



def From_search(From: str,cycle_Nodes,cycle_Edges,cycle_Values,cycle_TokenID,cycle_TimeStamp,cycle_MarketPlace,cycle_repeat):
    global cycle_len, cycle_repeat_num
    global cycle_Node_list, cycle_Edge_list, cycle_Value_list, cycle_TokenID_list, cycle_TimeStamp_list, cycle_MarketPlace_list

    # 제자리로 돌아왔는지 확인
    if From in cycle_Nodes:
        current_Node_index = cycle_Nodes.index(From)
        if current_Node_index + 1 == len(cycle_Nodes): # 한 개 노드인지 확인
            current_Edge_index = cycle_Edges.index((From, From))
        else:
            current_Edge_index = cycle_Edges.index((cycle_Nodes[current_Node_index + 1],From))

        # 인덱스를 통해 순환이 시작되는 부분부터 저장
        cycle_Node_list.append(cycle_Nodes[current_Node_index:])
        cycle_Edge_list.append(cycle_Edges[current_Edge_index:])
        cycle_Value_list.append(cycle_Values[current_Edge_index:])
        cycle_TokenID_list.append(cycle_TokenID[current_Edge_index:])
        cycle_TimeStamp_list.append(cycle_TimeStamp[current_Edge_index:])
        cycle_MarketPlace_list.append(cycle_MarketPlace[current_Edge_index:])
        cycle_repeat_num.append(cycle_repeat[current_Edge_index:])
        cycle_len.append(len(set(cycle_Edges[current_Edge_index:])))
        return

    else:
        cycle_Nodes.append(From)

        # From의 From 확인
        if From in To_group.groups:

            to_from_df = To_group.get_group(From).reset_index().copy() # 그룹핑 데이터프레임 (리스트에 데이터 저장할 용도)
            unique_to_from_df = to_from_df.drop_duplicates(['From']) # 중복제거한 데이터프레임 ( From의 From 찾을 용도)

            # From의 From 찾고, 순환하지 않는 노드면 저장했던 엣지 지우기
            for from_address in unique_to_from_df['From']:

                # 엣지를 지우기 위해 리스트의 현재 인덱스 저장
                cycle_Edges_lastindex = len(cycle_Edges)
                check_cycle_Edge_list = sum(cycle_Edge_list,[]) #이미 저장한 엣지인지 확인하기 위한 리스트


                if (from_address,From) not in check_cycle_Edge_list:

                    # (From,To)가 같은 트랜잭션 저장
                    duple_row = to_from_df.index[to_from_df['From']== from_address].tolist()
                    duple_row_len = len(duple_row)

                    for df_index in duple_row:
                        cycle_Edges.append((to_from_df.iloc[df_index]['From'],From))
                        cycle_Values.append(to_from_df.iloc[df_index]['Value'])
                        cycle_TokenID.append(to_from_df.iloc[df_index]['TokenID'])
                        cycle_TimeStamp.append(to_from_df.iloc[df_index]['TimeStamp'])
                        cycle_MarketPlace.append(to_from_df.iloc[df_index]['Market Place'])
                        cycle_repeat.append(duple_row_len)


                    From_search(from_address,cycle_Nodes,cycle_Edges,cycle_Values,cycle_TokenID,cycle_TimeStamp,cycle_MarketPlace,cycle_repeat)

                    # 추가했던 부분 지우기
                    cycle_Edges = cycle_Edges[:cycle_Edges_lastindex]
                    cycle_Values = cycle_Values[:cycle_Edges_lastindex]
                    cycle_TokenID = cycle_TokenID[:cycle_Edges_lastindex]
                    cycle_TimeStamp = cycle_TimeStamp[:cycle_Edges_lastindex]
                    cycle_MarketPlace = cycle_MarketPlace[:cycle_Edges_lastindex]
                    cycle_repeat = cycle_repeat[:cycle_Edges_lastindex]

            cycle_Nodes.remove(From)
        else:
            cycle_Nodes.remove(From)


def To_From_check(From: str):
    global Nodes, cycle_Node_list

    if From not in Nodes:
        Nodes.append(From)
        current_to = From

        # to 그룹에 존재하는지 확인 (왼쪽이 있는지 확인)
        if current_to in To_group_list:
            To_group_list.remove(current_to)

            # From에 해당되는 To 그룹 얻기
            to_from_df = To_group.get_group(current_to)
            to_from_df_len = len(to_from_df)

            # 행 개수 만큼 반복 한개씩 to from을 진행
            for i in range(to_from_df_len):
                To_From_check(to_from_df.iloc[i]['From'])

        if current_to in From_group_list:
            From_group_list.remove(current_to)
            from_to_df = From_group.get_group(current_to)
            from_to_df_len = len(from_to_df)

            for i in range(from_to_df_len):
                From_To_check(from_to_df.iloc[i]['To'])
    else:
        # 중복 탐색 방지
        if From not in search_list:
            search_list.append(From)
            cycle_Nodes = []
            cycle_Edges = []
            cycle_repeat = []
            cycle_Values = []
            cycle_TokenID = []
            cycle_TimeStamp = []
            cycle_MarketPlace = []

            From_search(From,cycle_Nodes,cycle_Edges,cycle_Values,cycle_TokenID,cycle_TimeStamp,cycle_MarketPlace,cycle_repeat)


def From_To_check(To: str):
    global Nodes

    if To not in Nodes:
        Nodes.append(To)
        current_from = To

        if current_from in To_group_list:
            To_group_list.remove(current_from)

            # From에 해당되는 To 그룹 얻기
            to_from_df = To_group.get_group(current_from)
            to_from_df_len = len(to_from_df)

            # 행 개수 만큼 반복 한개씩 to_from 함수 진행
            for i in range(to_from_df_len):
                To_From_check(to_from_df.iloc[i]['From'])

        if current_from in From_group_list:
            From_group_list.remove(current_from)
            from_to_df = From_group.get_group(current_from)
            from_to_df_len = len(from_to_df)

            for i in range(from_to_df_len):
                From_To_check(from_to_df.iloc[i]['To'])


# From_list의 0 번째에서 From을 받아서 경로 체크
def Route_check(From: str):
    global Nodes
    middle_node = From
    Nodes.append(middle_node)
    From_group_list.remove(middle_node)

    # from이 to 그룹에 존재하는지 확인
    if middle_node in To_group_list:
        To_group_list.remove(middle_node)

        # From에 해당되는 to 그룹 얻기 (From의 From들)
        to_from_df = To_group.get_group(middle_node)
        to_from_df_len = len(to_from_df)

        # 행 개수 만큼 반복 한개씩 to from을 진행
        for i in range(to_from_df_len):
            To_From_check(to_from_df.iloc[i]['From'])

    from_to_df = From_group.get_group(middle_node)
    from_to_df_len = len(from_to_df)

    for i in range(from_to_df_len):
        From_To_check(from_to_df.iloc[i]['To'])


def get_cycle_Route():
    All_Route = {}
    num=0 # 경로 구분하기 위한 용도
    while(len(From_group_list) > 0):
        global Nodes
        global cycle_Node_list, cycle_Edge_list, cycle_Value_list, cycle_TokenID_list, cycle_TimeStamp_list, cycle_MarketPlace_list
        global cycle_len, cycle_repeat_num
        Nodes = []
        cycle_Node_list = []
        cycle_Edge_list = []
        cycle_Value_list = []
        cycle_TokenID_list = []
        cycle_TimeStamp_list = []
        cycle_MarketPlace_list = []
        cycle_len = []
        cycle_repeat_num = []

        Route_check(From_group_list[0])

        num = num+1
        Route_num = num
        if not cycle_Node_list:
            continue
        current_Route = {'Route Number':Route_num,'Nodes':Nodes,'Cycle Nodes':cycle_Node_list,'Cycle Edges':cycle_Edge_list,
                         'Cycle Values':cycle_Value_list,'Cycle TokenID':cycle_TokenID_list,'Cycle TimeStamp':cycle_TimeStamp_list,
                         'Cycle Market Place':cycle_MarketPlace_list,'Cycle Length':cycle_len, 'Cycle Repeat':cycle_repeat_num}

        All_Route.setdefault(str(current_Route['Route Number']), current_Route)
    search_list.clear()
    return All_Route


def get_Pyvis_From_cycle_Routes(Route,keys):
    pyvis_graph = net.Network(notebook=True, directed=True, height="750px", width="100%")

    for key in keys:
        for i in range(len(Route[key]['Cycle Nodes'])):
            pyvis_graph.add_nodes(Route[key]['Cycle Nodes'][i])

        for e in range(len(Route[key]['Cycle Edges'])):
            pyvis_graph.add_edges(Route[key]['Cycle Edges'][e])

    pyvis_graph.repulsion(central_gravity=0)
    pyvis_graph.show_buttons(filter_=['physics'])

    return pyvis_graph


def get_DataFrame_From_cycle_Route(Route):

    All_Route_key = sorted(map(int,list(Route.keys())),reverse=False)
    cycle_Route_df = pd.DataFrame(columns=['Token Name','Route Number','Cycle Number','Cycle Length','Cycle Repeat',
                                           'TimeStamp','From','To','Value','TokenID','Market Place'])

    for key in All_Route_key:
        key = str(key)

        for num in range(len(Route[key]['Cycle Nodes'])):
            edge = np.array(Route[key]['Cycle Edges'][num])
            df = pd.DataFrame({
                'Token Name' : token_name,
                'Route Number' : key,
                'Cycle Number' : num+1,
                'Cycle Length' : Route[key]['Cycle Length'][num],
                'Cycle Repeat' : Route[key]['Cycle Repeat'][num],
                'TimeStamp' : Route[key]['Cycle TimeStamp'][num],
                'From' : edge[:,:1].ravel(),
                'To' : edge[:,1:].ravel(),
                'Value': Route[key]['Cycle Values'][num],
                'TokenID': Route[key]['Cycle TokenID'][num],
                'Market Place': Route[key]['Cycle Market Place'][num],
            })
            cycle_Route_df = cycle_Route_df.append(df)

    return cycle_Route_df


def count(agg:pd.Series):
    agg = agg[agg != 0]
    return len(agg)


# 점수 측정(미완)
def count_score(agg:pd.Series):
    one_index = agg[agg < 5].index
    #two_index = agg[agg < 10 and agg >= 5].index
    three_index = agg[agg >= 10].index

    agg[one_index] = 1
    #agg[two_index] = 2
    agg[three_index] = 3

    print(agg)

def avg_score(agg:pd.Series):
    print(agg, "1")

def single_cycle_score(agg: pd.Series):
    print(agg, "1")

def multi_cycle_score(agg: pd.Series):
    print(agg, "1")

def cycle_walletnum_score(agg: pd.Series):
    print(agg,"1")



def get_wallet_score():

    from_wallet_df = From_group['Value'].agg([count, 'sum']).reset_index(drop=False)
    to_wallet_df = To_group['Value'].agg([count, 'sum']).reset_index(drop=False)

    # 컬럼명 변경
    from_wallet_df = from_wallet_df.rename(columns={'From': 'wallet'})
    to_wallet_df = to_wallet_df.rename(columns={'To': 'wallet'})
    append_df = from_wallet_df.append(to_wallet_df)


    # 중복행 합치기
    wallet_df = append_df.groupby("wallet").agg("sum").reset_index(drop=False)
    wallet_df["avg"] = wallet_df["sum"].div(wallet_df["count"].values, axis=0)

    wallet_df["single cycle number"] = 0
    wallet_df["multi cycle number"] = 0
    wallet_df["cycle wallets number"] = 0

    # 순환 경로 데이터프레임 저장
    cycle_Route = get_cycle_Route()
    cycle_Route_df = get_DataFrame_From_cycle_Route(cycle_Route)


    cycle_Route_groups = cycle_Route_df.groupby(['Route Number','Cycle Number'])
    cycle_Route_keys = cycle_Route_groups.groups.keys()

    # 인덱스 부분
    # wallet columns index
    single_cycle_i = wallet_df.columns.get_loc("single cycle number")
    multi_cycle_i = wallet_df.columns.get_loc("multi cycle number")
    cycle_wallets_i = wallet_df.columns.get_loc("cycle wallets number")

    # cycle columns index
    cycle_legth_i = cycle_Route_df.columns.get_loc("Cycle Length")
    cycle_repeat_i = cycle_Route_df.columns.get_loc("Cycle Repeat")

    for key in cycle_Route_keys:
        # 그룹핑 후 중복 거래 제거 drop duplicates 때문에 copy 사용 (원본 수정 안될수도 있다는 오류때문)
        cycle_df = cycle_Route_groups.get_group(key).reset_index().copy()
        cycle_df_nonduple =cycle_df.drop_duplicates(subset=['From','To']).copy() # 중복제거

        # 인덱스로 접근하여 해당 값 저장 하기
        for i in range(len(cycle_df_nonduple)) :
            index = int(wallet_df[wallet_df['wallet'] == cycle_df_nonduple['From'].iloc[i]].index[0])

            if cycle_df_nonduple.iloc[i,cycle_legth_i] == 1 :
                wallet_df.iloc[index,single_cycle_i] = cycle_df.iloc[i,cycle_repeat_i]
            else:
                wallet_df.iloc[index, multi_cycle_i] += 1
                wallet_df.iloc[index, cycle_wallets_i] += cycle_df.iloc[i,cycle_repeat_i]

    wallet_df.to_csv("123.csv")


get_wallet_score()
