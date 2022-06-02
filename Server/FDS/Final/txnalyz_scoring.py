import pandas as pd
import os
import networkx as nx
from pyvis import network as net

class txnalyz:
    def __init__(self):
        self.node_conn = {}
        self.node_list = []
        self.root_list = []
        self.cycleroot_list = []
        self.cycle_list = []
        self.check_route = []
        self.node_route = []
        self.cycle_route = []
        self.selected_csv = ''
        
    # def init(self, path):
        # self.read_csv(path)
        # self.init_nodelist()
        # self.init_nodeconnect()
        # self.init_rootlist()
        
    def init(self):
        self.init_nodelist()
        self.init_nodeconnect()
        self.init_rootlist()
        self.init_nodehop()
        
    def is_empty(self, List):
        if(len(List) == 0):
            return True
        else:
            return False
    
    def read_csv(self, path):
        self.selected_csv = path.split('/')[-1]
        self.token_df = pd.read_csv(path ,encoding= 'unicode_escape')
        idx = self.token_df[self.token_df['From'].str.slice(start=0, stop=10) == "Black Hole"]['From'].index
        self.token_df.drop(idx, inplace=True)
        self.token_df['Value'] = self.token_df['Value'].astype('str').str.replace(',','').astype('float')
        self.token_df['TokenID'] = self.token_df['TokenID'].astype('str').str.replace('ID','')

    def init_score_df(self):
        self.wallet_df = pd.DataFrame(self.node_list, columns=['wallet'])
        self.wallet_df["week trade count"] = 0
        self.wallet_df["week value sum"] = 0
        self.wallet_df["week value average"] = 0
        self.wallet_df["day trade count"] = 0
        self.wallet_df["day value sum"] = 0
        self.wallet_df["day value average"] = 0
        self.wallet_df["single cycle number"] = 0
        self.wallet_df["multi cycle number"] = 0
        self.wallet_df["cycle wallets number"] = 0
        self.wallet_df["score"] = 0

    def count_score_cycle(self):
        # 2차원 리스트 중복 제거
        self.cycle_route = list(set(map(tuple,self.cycle_route)))
        # 점수 부여
        for n in self.cycle_route:
            if (len(n) == 1):
                self.wallet_df['single cycle number'][self.wallet_df['wallet'] == n[0]] += 1
                self.wallet_df['score'][self.wallet_df['wallet'] == n[0]] += 1
            else:
                for j in n:
                    self.wallet_df['multi cycle number'][self.wallet_df['wallet'] == j] += 1
                    self.wallet_df['cycle wallets number'][self.wallet_df['wallet'] == j] += len(n)
                    self.wallet_df['score'][self.wallet_df['wallet'] == j] += 3

    def count_avg(self, date):
        # date 형식: XXXX-XX-XX
        index_list =[]
        date_filter = lambda x: x[0:10]
        tmp_df = self.token_df
        # print(tmp_df['Timestamp'].dtypes)
        tmp_df['Timestamp'] = self.token_df['Timestamp'].astype(str).apply(date_filter)
        tmp_df['Timestamp'] = pd.to_datetime(tmp_df['Timestamp'])


        filtered_df = tmp_df.loc[tmp_df['Timestamp'] == date]



        for i in self.wallet_df['wallet']:
            index_list.extend(tmp_df.index[filtered_df['From'] == i].tolist())
            index_list.extend(tmp_df.index[filtered_df['To'] == i].tolist())
            value_index = list(set(index_list))
            # 금액 합계
            value_sum = tmp_df['Value'].loc[value_index].sum()

            # 금액 스코어링 (ETH 기준) ( 달러로 500, 600, 700, 800, 900)
            if( 0.275 <= value_sum < 0.33 ):
                self.wallet_df['score'][self.wallet_df['wallet'] == i] += 1
            elif( 0.33 <= value_sum < 0.385):
                self.wallet_df['score'][self.wallet_df['wallet'] == i] += 2
            elif ( 0.385 <= value_sum < 0.44 ):
                self.wallet_df['score'][self.wallet_df['wallet'] == i] += 3
            elif ( 0.44 <= value_sum < 0.495):
                self.wallet_df['score'][self.wallet_df['wallet'] == i] += 4
            elif ( 0.495 <= value_sum < 0.55):
                self.wallet_df['score'][self.wallet_df['wallet'] == i] += 5
            # 1000달러 이상일 경우 warning
            # else:
                # warning
            self.wallet_df["day value sum"][self.wallet_df['wallet'] == i] = value_sum
            index_list = []

        self.wallet_df.to_csv("wallet.csv", index=False)


        print(self.wallet_df)

        # market_date_average = market_date_group.agg({"Market Place": [('Market Place', get_market_name)],
        #                                              "From": [('Sale Count Average', from_group_count_avg)],
        #                                              "To": [("Purchase Count Average", to_group_count_avg)],
        #                                              "TokenID": [('Token count Average', token_count_avg)],
        #                                              'Value': [('Value Average', 'mean')]})


    def delete_node(self, delete_list):
        for addr in delete_list:
            l = list(self.token_df[(self.token_df['From'] == addr) | (self.token_df['To'] == addr)].index)
            self.token_df = self.token_df.drop(l)
        self.token_df = self.token_df.reset_index(drop = True)
        
    def init_nodelist(self):
        self.node_list = []
        self.From_group = self.token_df.groupby("From")
        From_group_list = list(self.From_group.groups.keys())
        self.To_group = self.token_df.groupby("To")
        To_group_list = list(self.To_group.groups.keys())
        self.node_list = list(set(From_group_list+To_group_list))

    def init_nodeconnect(self):
        # 노드 연결 정보 초기화
        self.node_conn = {}
        for addr in self.node_list:
            self.node_conn[addr] = {'From' : set([]), 'To' : set([]), 'Max_hop' : 0}
            
        # 각 노드 연결 정보 설정
        for i in list(self.token_df.index):
            self.node_conn[self.token_df.at[i, 'From']]['To'].add(self.token_df.at[i, 'To'])
            self.node_conn[self.token_df.at[i, 'To']]['From'].add(self.token_df.at[i, 'From'])

        # set to list
        for addr in self.node_list:
            self.node_conn[addr]['From'] = list(self.node_conn[addr]['From'])
            self.node_conn[addr]['To'] = list(self.node_conn[addr]['To'])
            
    def init_rootlist(self):
        self.root_list = []
        for addr in self.node_list:
            # if(len(self.node_conn[addr]['From']) == 0):
            if(self.is_empty(self.node_conn[addr]['From'])):
                self.root_list.append(addr)
                
    def __update_nodehop__(self, addr, stack):
        h = 0
        
        for n in self.node_conn[addr]['To']:
            # 순환 탐색
            if(stack.count(n)):
                self.cycle_list.append(stack[0])
                self.cycle_route.append(stack[stack.index(n):]+[addr])
            # 만약 To의 Max_hop이 0이면 update_nodehop 실행
            if(stack.count(n) == 0 and self.node_conn[n]['Max_hop'] == 0):
                self.__update_nodehop__(n, stack+[addr])
            # h 갱신
            if(self.node_conn[n]['Max_hop'] > h): h = self.node_conn[n]['Max_hop']
        
        # Max_hop 갱신
        self.node_conn[addr]['Max_hop'] = h+1
        
    def __update_nodehop_rev__(self, addr, stack, h):
        # Max_hop 갱신
        self.node_conn[addr]['Max_hop'] = h+1

        for n in self.node_conn[addr]['From']:
            # 순환 루트 노드 탐색하기
            if(stack.count(n)):
                self.cycle_list.append(addr)
                self.cycle_route.append(stack[stack.index(n):]+[addr])
            # From의 Max_hop이 0이면, 또는 addr의 Max_hop이 From의 Max_hop보다 같거나 큰 경우 update_nodehop_rev 실행
            elif(self.node_conn[n]['Max_hop'] == 0 or self.node_conn[addr]['Max_hop'] >= self.node_conn[n]['Max_hop']):
                self.__update_nodehop_rev__(n, stack+[addr], h+1)
                
    def init_nodehop(self):
        # root 노드 기준으로 Max_hop 갱신해주기
        for addr in self.root_list:
            self.__update_nodehop__(addr, [])
            
        # root 노드 없을 때 순환 노드 Max_hop 갱신해주기
        for addr in self.node_list:
            if(self.is_empty(self.node_conn[addr]['To']) and self.node_conn[addr]['Max_hop'] == 0):
                self.__update_nodehop_rev__(addr, [], 0)

        # 순환 탐색하기
        for addr in self.node_list:
            if(self.node_conn[addr]['Max_hop'] == 0):
                self.__update_nodehop__(addr, [])  

        self.cycle_list = list(set(self.cycle_list))

    def get_node(self, n, mode='total'):
        rlist = []
        
        if(mode == 'total'):
            for addr in list(set(self.root_list+self.cycle_list)):
            # for addr in self.cycle_list:
                if(self.node_conn[addr]['Max_hop'] >= n): 
                    rlist.append(addr)
        elif(mode == 'cycle'):
            # for addr in list(set(self.root_list+self.cycle_list)):
            for addr in self.cycle_list:
                if(self.node_conn[addr]['Max_hop'] >= n): 
                    rlist.append(addr)
        
        return rlist

    def dfs_node(self, addr, n):
        if(self.check_route.count(addr)):
            return
        else:
            self.check_route.append(addr)

        if(self.node_conn[addr]['Max_hop'] == 1): 
            for to in self.node_conn[addr]['To']:
                # print([addr, to])
                self.node_route.append([addr, to, float(self.token_df[(self.token_df.From == addr) & (self.token_df.To == to)].loc[:, ['Value']].sum())])
            return
        if(self.node_conn[addr]['Max_hop'] < n): 
            return
        for to in self.node_conn[addr]['To']:
            # print([addr, to])
            self.node_route.append([addr, to, float(self.token_df[(self.token_df.From == addr) & (self.token_df.To == to)].loc[:, ['Value']].sum())])
            if(self.node_conn[to]['Max_hop'] < self.node_conn[addr]['Max_hop']):
                self.dfs_node(to, n-1)
        
    def init_route(self, n, mode='total'):
        l = self.get_node(n, mode=mode)
        for i in l:
            self.dfs_node(i, n)

    def get_txlist(self):
        txlist = []
    
        for ed in self.node_route:
            txlist += list(self.token_df[(self.token_df['From'] == ed[0]) & (self.token_df['To'] == ed[1])]['Txn Hash'])
            
        return txlist
            
    def write_csv(self, txlist, fname):
        df = pd.DataFrame()
    
        for tx in txlist:
            df = pd.concat([df, self.token_df[self.token_df['Txn Hash'] == tx]])
        
        df.to_csv('result/' + fname, header = True, index = False)
            
    def draw_graph3(self, networkx_graph,notebook=True, show_buttons=True,only_physics_buttons=False,
                height="750px",width="100%",bgcolor=None,font_color=None,pyvis_options=None):        
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
            print('source : ', source)
            print('target : ', target)
            
        # print(pyvis_graph)

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
        return pyvis_graph.show(self.selected_csv.split('.')[0]+'.html')

    def show_networkx_graph(self):
        sum_weight = 0
        length = 0
        G = nx.DiGraph()

        
        for n in self.node_route:
            G.add_weighted_edges_from([n])
            # sum_weight += n[0][2]
            # length += 1

        # avg = sum_weight/length
        # print(avg)
        # edges = G.edges
        # weights = [G[u][v]['weight']*3.0/avg for u, v in edges]
        # print(weights)
        # plt.figure(figsize=(25, 25))
        # pos = nx.spring_layout(G, k=0.2)
        # d = dict(G.degree)

        # n_data = [v * 1000 for v in d.values()]
        # nx.draw_networkx_edges(G, pos,width=0.5,arrows=True, arrowstyle='->', arrowsize=5, )
        # nx.draw(G, pos, width = weights, with_labels=True, font_size=6, linewidths=0.5,
        # edge_color="black", edgecolors='gray', node_size=n_data, node_color=n_data)
        # plt.show()

        self.draw_graph3(G)