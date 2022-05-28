from txnalyz import *

ta = txnalyz()
ta.read_csv('classification/AIMoonbirds.csv')
# ta.read_csv('testcase/test10.csv')

dlist = ['0x83c8f28c26bf6aaca652df1dbbe0e1b56f8baba2']

# while(True):
    # I = str(input('addr(\'e\' to exit) : '))
    # if(I == 'e'):
        # break
    # dlist.append(I)

ta.delete_node(dlist)

ta.init()
ta.init_route(3)

ta.show_networkx_graph()
ta.write_csv(ta.get_txlist(), 'baby_total')