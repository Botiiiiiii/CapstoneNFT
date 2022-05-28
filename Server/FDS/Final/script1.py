from txnalyz import *

ta = txnalyz()
ta.read_csv('classification/AIMoonbirds.csv')
# ta.read_csv('testcase/test10.csv')
ta.init()
ta.init_route(3)

ta.show_networkx_graph()
ta.write_csv(ta.get_txlist(), 'baby_total')