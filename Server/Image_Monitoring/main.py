from typing import Optional
from fastapi import FastAPI, File, UploadFile, Form
from fastapi.responses import FileResponse


from PIL import Image
import numpy as np
from os import path as path

from vgg_feature_extractor import FeatureExtractor
from init_settings import Init
from fastapi.staticfiles import StaticFiles
import matplotlib.pyplot as plt
import uuid as uuid
import io

import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

# # 학습에 사용되는 이미지 디렉토리
# # 현재 테스트에서는 bored apes yacht club nft 이미지 만개를 사용함
# IMG_DIR = 'bayc\*'

# 추출된 피쳐가 저장되는 디렉토리
FEATURE_DIR = 'static/feature/*'


app = FastAPI()
app.mount("/static", StaticFiles(directory="static"), name="static")


# 각 이미지에서 피쳐를 추출함
# getImageFeature(IMG_DIR, img_paths)

fe = FeatureExtractor()



def searchImage(img, fileid=0000):
    features, img_paths, np_paths = Init.loadFeatures(FEATURE_DIR)
    def getTitle(score):

        if (score <= 0.2):
            return 'same'

        elif(score <= 0.8):
            return 'suspicious'

        else:
            return 'different'

    # 피쳐 추출
    query = fe.extract(img)

    # 이미지 사이의 similarity를 계산함
    dists = np.linalg.norm(features - query, axis=1)

    # 가장 가까운 거리 10개를 가져옴
    ids = np.argsort(dists)[:10]
    scores = [(dists[id], img_paths[id], np_paths[id]) for id in ids]
    fig = plt.figure(figsize=(8, 8))

    # original 사진을 결과값에 포함
    axes = []
    axes.append(fig.add_subplot(2, 5, 1))

    axes[-1].set_title('original')

    plt.axis('off')
    plt.imshow(img)

    for a in range(9):
        score = scores[a]
        axes.append(fig.add_subplot(2, 5, a+2))

        subplot_title = getTitle(score[0])
        axes[-1].set_title(subplot_title)

        plt.axis('off')
        plt.imshow(Image.open(score[1]))

    # fig.tight_layout()
    # plt.show()features, img_paths = Init.loadFeatures(FEATURE_DIR)x
    plt.savefig(f'static/result/{fileid}-result.png')

def searchImage_score(img, fileid):
    features, img_paths, np_paths = Init.loadFeatures(FEATURE_DIR)
    def getTitle(score):

        if (score <= 0.2):
            return 'same'

        elif(score <= 0.8):
            return 'suspicious'

        else:
            return 'different'

    # 피쳐 추출
    query = fe.extract(img)

    # 이미지 사이의 similarity를 계산함
    dists = np.linalg.norm(features - query, axis=1)

    # 가장 가까운 거리 10개를 가져옴
    ids = np.argsort(dists)[:2]
    scores = [(dists[id], img_paths[id],np_paths[id]) for id in ids]
    print(scores[0])
    score = scores[0][0]
    feature_path = f"feature/{fileid}.npy"

    if(getTitle(score)=='different'):
        np.save(feature_path, query)

    return (score * 100), scores[0][2]

@app.get("/")
async def read_root():
    return {"Hello": "World"}


@app.post("/images/")
async def search_nft_image(file: UploadFile = File(...)):
    print(file.file)
    fileid = uuid.uuid4()

    contents = await file.read()  # <-- Important!

    img = Image.open(io.BytesIO(contents))
    searchImage(img, fileid=fileid)
    path = f'static\\result\\{fileid}-result.png'

    return FileResponse(path)

    # return {"Hello": "World"}

@app.post("/scoring/")
async def search_nft_image(file: UploadFile = File(...), token_id: str = Form(...)):
    print(file.file)
    fileid = token_id

    contents = await file.read()  # <-- Important!

    img = Image.open(io.BytesIO(contents))
    score, sim_id = searchImage_score(img, fileid=fileid)

    return {"score": score, "token_id": sim_id}

    # return {"Hello": "World"}