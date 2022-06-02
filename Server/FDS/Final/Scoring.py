from txnalyz_scoring import *

ta = txnalyz()
ta.read_csv('classification/Opensea Shared Storefront (ERC-1155).csv')
# ta.read_csv('testcase/test10.csv')

ta.init()
ta.init_route(3,mode='cycle')
ta.init_score_df()
ta.count_score_cycle()
ta.count_avg('2022-05-18')
# print(ta.cycle_route)
# print(ta.wallet_df)



# ta.init_route(3)

# ta.show_networkx_graph()
# ta.write_csv(ta.get_txlist(), 'wallet')




# 지갑관점 (5점 이상 이상징후거래)
# 일일 거래 횟수 (2회 이상: 1점, 5회 이상 3점, 10회 이상 : 7점)
# 거래 금액 달러 기준 ( 499~599 1점, 600~699 2점, 700~799 3점, 800~899, 900~999: 5점)
# 1000불 이상은 이상 징후거래로 판단
# 자전거래: 1회 3점, 금액 증가 횟수당 0.5점
# 순환거래: 지갑당 3점, 커뮤니티 내 가격형성 경우 5점
# 일방적 거래 2회 이상, 1회당 1점씩 부여