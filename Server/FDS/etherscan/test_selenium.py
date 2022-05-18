from web3 import Web3

infura_url = "https://mainnet.infura.io/v3/1c71753252df499cab6273d110ab7e52"
web3 = Web3(Web3.HTTPProvider(infura_url))
print("- Connection : ", web3.isConnected())

print("- Current Block No.: ", web3.eth.block_number)
balance = web3.eth.getTransaction
print("- balance : ", balance)
