

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import networkx as nx
from pyvis import network as net
import pandas as pd
import os
import locale

locale.setlocale(locale.LC_ALL, 'en_US.UTF-8')


# 폴더 파일 리스트
path = "classification/"
file_list = os.listdir(path)

# 데이터프레임 불러온 후 블랙홀 제거
token_df = pd.read_csv("classification/"+file_list[0],encoding='latin_1')
idx = token_df[token_df['From'].str.slice(start=0, stop=10) == "Black Hole"]['From'].index
token_df.drop(idx, inplace=True)
token_df['Value'] = token_df['Value'].astype('str').str.replace(',','').astype('float')

# print(token_df['Value'])

# From Group , To Group
From_group = token_df.groupby("From")
From_group_list = list(From_group.groups.keys())
To_group = token_df.groupby("To")
To_group_list = list(To_group.groups.keys())


# From이 To그룹에 있는지 확인, 있으면 엣지 추가
# To 그룹리스트 에서 검사한 주소 제거
def To_From_check(From: str):
    global Nodes, Values, Edges, Weighted_Edges, TokenID

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
                Edges.append((to_from_df.iloc[i]['From'], current_to))
                Weighted_Edges.append((to_from_df.iloc[i]['From'], current_to,to_from_df.iloc[i]['Value']))
                Values.append(to_from_df.iloc[i]['Value'])
                TokenID.append(to_from_df.iloc[i]['TokenID'])
                To_From_check(to_from_df.iloc[i]['From'])

        if current_to in From_group_list:
            From_group_list.remove(current_to)
            from_to_df = From_group.get_group(current_to)
            from_to_df_len = len(from_to_df)

            for i in range(from_to_df_len):
                From_To_check(from_to_df.iloc[i]['To'])


# To가 From그룹에 있는지 확인, 있으면 엣지 추가
# From 그룹리스트 에서 검사한 주소 제거
def From_To_check(To: str):
    global Nodes, Values, Edges, Weighted_Edges, TokenID

    if To not in Nodes:
        Nodes.append(To)
        current_from = To

        # to 그룹에 존재하는지 확인 (왼쪽이 있는지 확인) (*if문 지우기)
        if current_from in To_group_list:
            To_group_list.remove(current_from)

            # From에 해당되는 To 그룹 얻기
            to_from_df = To_group.get_group(current_from)

            to_from_df_len = len(to_from_df)

            # 행 개수 만큼 반복 한개씩 to_from 함수 진행
            for i in range(to_from_df_len):
                Edges.append((to_from_df.iloc[i]['From'], current_from))
                Weighted_Edges.append((to_from_df.iloc[i]['From'], current_from, to_from_df.iloc[i]['Value']))
                Values.append(to_from_df.iloc[i]['Value'])
                TokenID.append(to_from_df.iloc[i]['TokenID'])
                To_From_check(to_from_df.iloc[i]['From'])

        if current_from in From_group_list:
            From_group_list.remove(current_from)
            from_to_df = From_group.get_group(current_from)
            from_to_df_len = len(from_to_df)

            for i in range(from_to_df_len):
                From_To_check(from_to_df.iloc[i]['To'])


# From_list의 0 번째에서 From을 받아서 경로 체크
def Route_check(From: str):
    global Nodes, Edges, Weighted_Edges, TokenID, Values
    middle_node = From
    Nodes.append(middle_node)
    From_group_list.remove(middle_node)

    # from이 to 그룹에 존재하는지 확인
    if middle_node in To_group_list:
        To_group_list.remove(middle_node)

        # From에 해당되는 to 그룹 얻기
        to_from_df = To_group.get_group(middle_node)
        to_from_df_len = len(to_from_df)

        # 행 개수 만큼 반복 한개씩 to from을 진행
        for i in range(to_from_df_len):
            Edges.append((to_from_df.iloc[i]['From'],middle_node))
            Weighted_Edges.append((to_from_df.iloc[i]['From'], middle_node, to_from_df.iloc[i]['Value']))
            Values.append(to_from_df.iloc[i]['Value'])
            TokenID.append(to_from_df.iloc[i]['TokenID'])
            To_From_check(to_from_df.iloc[i]['From'])

    from_to_df = From_group.get_group(middle_node)
    from_to_df_len = len(from_to_df)

    for i in range(from_to_df_len):
        From_To_check(from_to_df.iloc[i]['To'])


def get_All_Route():
    All_Route = {}
    while(len(From_group_list) > 0):
        global Nodes,Values,Edges,Weighted_Edges,TokenID
        Nodes = []
        Values = []
        Edges = []
        Weighted_Edges = []
        TokenID = []

        Route_check(From_group_list[0])
        Route_len = len(Edges)
        Node_len = len(Nodes)

        current_Route = {'Node_len':Node_len,'Route len':Route_len,'Nodes':Nodes,'Edges':Edges,'Weighted_Edges':Weighted_Edges, 'Values':Values,'TokenID':TokenID}

        if str(current_Route['Node_len']) in All_Route:
            All_Route[str(current_Route['Node_len'])].append(current_Route)
        else:
            All_Route.setdefault(str(current_Route['Node_len']), [current_Route])
    return All_Route


# n개 이상인 경로 찾기
def Choose_Route_keys(num,All_Route: dict):
    Routes = All_Route
    Route_keys = map(int,list(Routes.keys()))

    choose_keys = sorted(filter(lambda x: x >= num,Route_keys),reverse=True)
    choose_keys = list(map(str,choose_keys))

    print("엣지",num,"이상인 경로: ",choose_keys)
    return choose_keys


# 경로,key로 그래프 노드, 엣지 넣기
def get_Pyvis_From_Routes(Route,keys):
    pyvis_graph = net.Network(notebook=True, directed=True, height="750px", width="100%")
    sum_weight = 0
    length = 0

    for key in keys:
        for i in range(len(Route[key])):
            sum_weight += sum(Route[key][i]['Values'])
            length += len(Route[key][i]['Values'])

    avg = sum_weight/length

    # 경로들 리스트에서 꺼내서 pyvis에 넣기
    for key in keys:
        for i in range(len(Route[key])):
            pyvis_graph.add_nodes(Route[key][i]['Nodes'])
            pyvis_graph.add_edges(Route[key][i]['Weighted_Edges'])
            print('경로 : ',Route[key][i]['Edges'],' 거래금액 : ',Route[key][i]['Values'],' 토큰ID : ',Route[key][i]['TokenID'])
            print('-----------------------')
            print(Route[key][i])
    pyvis_graph.repulsion(central_gravity=0)
    pyvis_graph.show_buttons(filter_=['physics'])

    return pyvis_graph


def show_networkx_graph(Route:dict ,keys):
    sum_weight = 0
    length = 0
    G = nx.DiGraph()

    for key in keys:
        for i in range(len(Route[key])):
            G.add_weighted_edges_from(Route[key][i]['Weighted_Edges'])
            sum_weight += sum(Route[key][i]['Values'])
            length += len(Route[key][i]['Values'])
            # print(Route[key][i]['Weighted_Edges'])
        # print(Route[key])

    avg = sum_weight/length
    # print(avg)
    edges = G.edges
    weights = [G[u][v]['weight']*3.0/avg for u, v in edges]
    # print(weights)
    plt.figure(figsize=(25, 25))
    pos = nx.spring_layout(G, k=0.2)
    d = dict(G.degree)

    n_data = [v * 1000 for v in d.values()]
    nx.draw_networkx_edges(G, pos,width=0.5,arrows=True, arrowstyle='->', arrowsize=5, )
    nx.draw(G, pos, width = weights, with_labels=True, font_size=6, linewidths=0.5,
            edge_color="black", edgecolors='gray', node_size=n_data, node_color=n_data)
    plt.show()

    draw_graph3(G)


def draw_graph3(networkx_graph,notebook=True,output_filename=file_list[0]+'.html',show_buttons=True,only_physics_buttons=False,
                height="750px",width="100%",bgcolor=None,font_color=None,pyvis_options=None):
    """
    This function accepts a networkx graph object,
    converts it to a pyvis network object preserving its node and edge attributes,
    and both returns and saves a dynamic network visualization.
    Valid node attributes include:
        "size", "value", "title", "x", "y", "label", "color".
        (For more info: https://pyvis.readthedocs.io/en/latest/documentation.html#pyvis.network.Network.add_node)
    Valid edge attributes include:
        "arrowStrikethrough", "hidden", "physics", "title", "value", "width"
        (For more info: https://pyvis.readthedocs.io/en/latest/documentation.html#pyvis.network.Network.add_edge)
    Args:
        networkx_graph: The graph to convert and display
        notebook: Display in Jupyter?
        output_filename: Where to save the converted network
        show_buttons: Show buttons in saved version of network?
        only_physics_buttons: Show only buttons controlling physics of network?
        height: height in px or %, e.g, "750px" or "100%
        width: width in px or %, e.g, "750px" or "100%
        bgcolor: background color, e.g., "black" or "#222222"
        font_color: font color,  e.g., "black" or "#222222"
        pyvis_options: provide pyvis-specific options (https://pyvis.readthedocs.io/en/latest/documentation.html#pyvis.options.Options.set)
    """

    # import
    from pyvis import network as net

    # make a pyvis network
    network_class_parameters = {"notebook": notebook, "height": height, "width": width, "bgcolor": bgcolor, "font_color": font_color}
    pyvis_graph = net.Network(**{parameter_name: parameter_value for parameter_name, parameter_value in network_class_parameters.items() if parameter_value})

    # for each node and its attributes in the networkx graph
    for node,node_attrs in networkx_graph.nodes(data=True):
        pyvis_graph.add_node(node,**node_attrs)

    # for each edge and its attributes in the networkx graph
    for source,target,edge_attrs in networkx_graph.edges(data=True):
        # if value/width not specified directly, and weight is specified, set 'value' to 'weight'
        if not 'value' in edge_attrs and not 'width' in edge_attrs and 'weight' in edge_attrs:
            # place at key 'value' the weight of the edge
            edge_attrs['value']=edge_attrs['weight']
        # add the edge
        pyvis_graph.add_edge(source,target,**edge_attrs)

    # turn buttons on
    if show_buttons:
        if only_physics_buttons:
            pyvis_graph.show_buttons(filter_=['physics'])
        else:
            pyvis_graph.show_buttons()

    # pyvis-specific options
    if pyvis_options:
        pyvis_graph.set_options(pyvis_options)

    # return and also save
    return pyvis_graph.show(output_filename)


def main():
    # 전체 경로 얻기
    All_Route = get_All_Route()
    print(All_Route)
    # 전체 경로 키
    All_Route_keys = sorted(map(int,list(All_Route.keys())),reverse=True)
    All_Route_keys = list(map(str,(All_Route_keys)))

    # 전체 딕셔너리에 키 값이 3 이상인 것들 뽑아내기(키 값은 엣지 갯수, 벨류는 경로 딕셔너리가 들어간 리스트)
    choose_Route_keys = Choose_Route_keys(10,All_Route)
    # pyvis_graph = get_Pyvis_From_Routes(All_Route,choose_Route_keys)
    #
    #
    # pyvis_graph.repulsion(central_gravity=0)
    # pyvis_graph.show_buttons(filter_=['physics'])
    #
    # pyvis_graph.show("d1.html")
    show_networkx_graph(All_Route,choose_Route_keys)

main()