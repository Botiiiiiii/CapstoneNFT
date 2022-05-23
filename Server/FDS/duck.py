import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import networkx as nx
from pyvis import network as net
import pandas as pd
import os
import locale


class node_info():
    From = []
    To = []


# 폴더 파일 리스트
path = "classification/"
file_list = os.listdir(path)

# 데이터프레임 불러온 후 블랙홀 제거
token_df = pd.read_csv("classification/" + file_list[0], encoding='latin_1')
idx = token_df[token_df['From'].str.slice(start=0, stop=10) == "Black Hole"]['From'].index
token_df.drop(idx, inplace=True)
token_df['Value'] = token_df['Value'].astype('str').str.replace(',', '').astype('float')
token_df['TokenID'] = token_df['TokenID'].fillna(-1).astype('int64').replace({-1: None})

# From Group , To Group
From_group = token_df.groupby("From")
From_group_list = list(From_group.groups.keys())
To_group = token_df.groupby("To")
To_group_list = list(To_group.groups.keys())

Node_list = list(set(From_group_list + To_group_list))

node_route = []

# 노드 연결 정보 초기화
node_conn = {}
for addr in Node_list:
    # print(addr)
    node_conn[addr] = {'From': set([]), 'To': set([]), 'Max_hop': 0}

for i in range(token_df.From.count()):
    node_conn[token_df.at[i, 'From']]['To'].add(token_df.at[i, 'To'])

    node_conn[token_df.at[i, 'To']]['From'].add(token_df.at[i, 'From'])

for addr in Node_list:
    node_conn[addr]['From'] = list(node_conn[addr]['From'])
    node_conn[addr]['To'] = list(node_conn[addr]['To'])

# 루트 노드 찾기
Root_list = []
for addr in Node_list:
    if (len(node_conn[addr]['From']) == 0):
        Root_list.append(addr)


def update_nodehop(addr, stack):
    h = 0

    for n in node_conn[addr]['To']:
        # 만약 To의 Max_hop이 0이면 update_nodehop 실행
        if (stack.count(n) == 0 and node_conn[n]['Max_hop'] == 0):
            update_nodehop(n, stack + [addr])
        # h 갱신
        if (node_conn[n]['Max_hop'] > h): h = node_conn[n]['Max_hop']

    node_conn[addr]['Max_hop'] = h + 1


# dfs로 Max_hop 갱신해주기
for addr in Root_list:
    update_nodehop(addr, [])

Circle_list = []
# 순환 탐색하기
for addr in Node_list:
    if (node_conn[addr]['Max_hop'] == 0):
        update_nodehop(addr, [])
        Circle_list.append(addr)


def get_node(n):
    rlist = []

    for addr in Root_list + Circle_list:
        if (node_conn[addr]['Max_hop'] >= n):
            # print(addr + ' : ' + str(node_conn[addr]))
            rlist.append(addr)

    return rlist


def dfs_node(addr):
    if (node_conn[addr]['Max_hop'] == 1): return
    for to in node_conn[addr]['To']:
        node_route.append([[addr, to, float(token_df[(token_df.From == addr) & (token_df.To == to)].loc[:, ['Value']].sum())]])
        dfs_node(to)


for i in get_node(4):
    dfs_node(i)

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
        pyvis_options: provide pyvis-specific options (https://pyvis.readthedocs.io/en/latest/documentation.html#pyvis.options.Options.set  )
        
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

def show_networkx_graph():
    sum_weight = 0
    length = 0
    G = nx.DiGraph()

    
    for n in node_route:
        print(n)
        G.add_weighted_edges_from(n)
        sum_weight += n[0][2]
        length += 1

    avg = sum_weight/length
    print(avg)
    edges = G.edges
    weights = [G[u][v]['weight']*3.0/avg for u, v in edges]
    print(weights)
    plt.figure(figsize=(25, 25))
    pos = nx.spring_layout(G, k=0.2)
    d = dict(G.degree)

    n_data = [v * 1000 for v in d.values()]
    nx.draw_networkx_edges(G, pos,width=0.5,arrows=True, arrowstyle='->', arrowsize=5, )
    nx.draw(G, pos, width = weights, with_labels=True, font_size=6, linewidths=0.5,
    edge_color="black", edgecolors='gray', node_size=n_data, node_color=n_data)
    plt.show()

    draw_graph3(G)

show_networkx_graph()
