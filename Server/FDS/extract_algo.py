import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import networkx as nx
from pyvis import network as net
import pandas as pd
import os
import locale
from collections import deque


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

dict_from = dict.fromkeys(From_group_list)

route_len = {}

def dfs(graph, start_node,keys):
    global check_bufferm
    visited = [[]]
    need_visited = deque()
    i=0
    need_visited.append(start_node)

    while need_visited:
        node = need_visited.popleft()

        if node not in visited:
            visited[i].append(node)
            if node in keys:

                if visited[i].count(node) == 2:
                    visited.append(visited[i][0:-1])
                    if str(len(visited[i])) in route_len:
                        route_len[str(len(visited[i]))].append(visited[i])
                    else:
                        route_len.setdefault(str(len(visited[i])), [visited[i]])
                    i += 1
                    continue

                need_visited.extend(graph[node])

                # for j in visited[i]:
                #     if j in graph[node]:
                #         if j in need_visited:
                #             need_visited.remove(j)

        if node not in keys:
            visited.append(visited[i][0:-1])
            if str(len(visited[i])) in route_len:
                route_len[str(len(visited[i]))].append(visited[i])
            else:
                route_len.setdefault(str(len(visited[i])), [visited[i]])
            i += 1

        check_buffer.add(node)

    del visited[i:]

    print(route_len)

    return visited




def Route_Extract():
    global check_buffer
    check_buffer = set([])
    for i in range(len(token_df)):
        from_address = token_df.iloc[i]['From']
        a = token_df['To'].loc[token_df['From'] == from_address]
        dict_from[from_address] = set(a)


    All_Route = {}

    for i in set(From_group_list):
        if i in check_buffer:
            continue
        All_Route.setdefault(i, dfs(dict_from, i, dict_from.keys()))

    return All_Route


get_route = Route_Extract()


def show_networkx_graph(Route,keys):
    sum_weight = 0
    length = 0
    G = nx.DiGraph()
    for key in keys:
        for i in range(len(Route[key])):
            G.add_weighted_edges_from(Route[key][i]['Weighted_Edges'])
            sum_weight += sum(Route[key][i]['Values'])
            length += len(Route[key][i]['Values'])
            # print(Route[key][i]['Weighted_Edges'])
        print(Route[key])

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

# print(dfs(dict_from,'0x12552987aadc1c9a233a604d2fb5eb615df21a64',dict_from.keys()))

# print(dict_from)