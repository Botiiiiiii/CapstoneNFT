import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import networkx as nx
from pyvis import network as net
import pandas as pd
import os


# 폴더 파일 리스트
path = "classification/"
file_list = os.listdir(path)
token_name = file_list[len(file_list)-1][:-4] # .csv 제거

# 데이터프레임 불러온 후 블랙홀 제거
token_df = pd.read_csv("classification/"+token_name+'.csv',encoding='latin_1')
idx = token_df[token_df['From'].str.slice(start=0, stop=10) == "Black Hole"]['From'].index
token_df.drop(idx, inplace=True)

# From Group , To Group
From_group = token_df.groupby("From")
From_group_list = list(From_group.groups.keys())
To_group = token_df.groupby("To")
To_group_list = list(To_group.groups.keys())


# From만 찾아가서 제자리로 돌아오는 노드가 있는지 확인
def From_search(From: str,cycle_Nodes,cycle_Edges,cycle_Values,cycle_TokenID,cycle_TimeStamp,cycle_MarketPlace):
    global cycle_len
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

                cycle_Edges_lastindex = len(cycle_Edges) # 엣지를 지우기 위해 리스트의 현재 인덱스 저장
                check_cycle_Edge_list = sum(cycle_Edge_list,[]) #이미 저장한 엣지인지 확인하기 위한 리스트


                if (from_address,From) not in check_cycle_Edge_list:

                    for df_index in to_from_df.index[to_from_df['From']== from_address].tolist():
                        cycle_Edges.append((to_from_df.iloc[df_index]['From'],From))
                        cycle_Values.append(to_from_df.iloc[df_index]['Value'])
                        cycle_TokenID.append(to_from_df.iloc[df_index]['TokenID'])
                        cycle_TimeStamp.append(to_from_df.iloc[df_index]['TimeStamp'])
                        cycle_MarketPlace.append(to_from_df.iloc[df_index]['Market Place'])

                    From_search(from_address,cycle_Nodes,cycle_Edges,cycle_Values,cycle_TokenID,cycle_TimeStamp,cycle_MarketPlace)


                    cycle_Edges = cycle_Edges[:cycle_Edges_lastindex]
                    cycle_Values = cycle_Values[:cycle_Edges_lastindex]
                    cycle_TokenID = cycle_TokenID[:cycle_Edges_lastindex]
                    cycle_TimeStamp = cycle_TimeStamp[:cycle_Edges_lastindex]
                    cycle_MarketPlace = cycle_MarketPlace[:cycle_Edges_lastindex]

            cycle_Nodes.remove(From)
        else:
            cycle_Nodes.remove(From)

# From의 From
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
        check_cycle_Node_list = sum(cycle_Node_list,[])
        if From not in check_cycle_Node_list:
            cycle_Nodes = []
            cycle_Edges = []
            cycle_Values = []
            cycle_TokenID = []
            cycle_TimeStamp = []
            cycle_MarketPlace = []
            From_search(From,cycle_Nodes,cycle_Edges,cycle_Values,cycle_TokenID,cycle_TimeStamp,cycle_MarketPlace)

# To의 From
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
# From_list는 검사시마다 삭제하기에 0번부터 호출
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
        global cycle_len
        Nodes = []
        cycle_Node_list = []
        cycle_Edge_list = []
        cycle_Value_list = []
        cycle_TokenID_list = []
        cycle_TimeStamp_list = []
        cycle_MarketPlace_list = []
        cycle_len = []

        Route_check(From_group_list[0])

        num = num+1
        Route_num = num
        if not cycle_Node_list:
            continue
        current_Route = {'Route Number':Route_num,'Nodes':Nodes,'Cycle Nodes':cycle_Node_list,'Cycle Edges':cycle_Edge_list,
                         'Cycle Values':cycle_Value_list,'Cycle TokenID':cycle_TokenID_list,'Cycle TimeStamp':cycle_TimeStamp_list,
                         'Cycle Market Place':cycle_MarketPlace_list,'Cycle Length':cycle_len}

        All_Route.setdefault(str(current_Route['Route Number']), current_Route)
    return All_Route


# 엣지 n개 이상인 경로 찾기
def Choose_cycle_Route_keys(num,All_Route: dict):
    Routes = All_Route
    Route_keys = list(All_Route.keys())

    for key in Route_keys:
        if Routes[key]['Cycle Length'] < num:
            Route_keys.remove(key)

    return Route_keys


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
    cycle_Route_df = pd.DataFrame(columns=['Token Name','Route Number','Cycle Number','Cycle Length','TimeStamp','From','To','Value','TokenID','Market Place'])

    for key in All_Route_key:
        key = str(key)

        for num in range(len(Route[key]['Cycle Nodes'])):
            edge = np.array(Route[key]['Cycle Edges'][num])
            df = pd.DataFrame({
                'Token Name' : token_name,
                'Route Number' : key,
                'Cycle Number' : num+1,
                'Cycle Length' : Route[key]['Cycle Length'][num],
                'TimeStamp' : Route[key]['Cycle TimeStamp'][num],
                'From' : edge[:,:1].ravel(),
                'To' : edge[:,1:].ravel(),
                'Value': Route[key]['Cycle Values'][num],
                'TokenID': Route[key]['Cycle TokenID'][num],
                'Market Place': Route[key]['Cycle Market Place'][num],
            })
            cycle_Route_df = cycle_Route_df.append(df)

    return cycle_Route_df


def main():

    # 순환 경로 얻기
    cycle_Route = get_cycle_Route()

    # 순환 경로 데이터프레임 저장
    if not os.path.exists('cycle_route'):
        os.makedirs('cycle_route')
    cycle_Route_df = get_DataFrame_From_cycle_Route(cycle_Route)
    cycle_Route_df.to_csv("cycle_route/"+token_name+'.csv',index=False)

    # 순환 경로 그래프 그리기
    cycle_Route_key = cycle_Route.keys()
    pyvis_graph = get_Pyvis_From_cycle_Routes(cycle_Route, cycle_Route_key)

    pyvis_graph.repulsion(central_gravity=0)
    pyvis_graph.show_buttons(filter_=['physics'])

    pyvis_graph.show("cycle_route/"+token_name+".html")


main()
