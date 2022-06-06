from pathlib import Path
import glob
import numpy as np
from os import path as path
from vgg_feature_extractor import FeatureExtractor as fe
from PIL import Image

class Init:
    # 학습에 활용할 (기존의 NFT) 이미지 경로를 불러옴
    @classmethod
    def __getSampleImagesPath(cls, imageDirectory):

        # 폴더 경로에 있는 모든 이미지 리스트를 가져옴
        imagePathList = glob.glob(imageDirectory)

        img_paths = []

        # 이미지 경로를 불러옴
        for i in imagePathList:
            img_paths.append(i)

        return img_paths

    # 각 이미지에서 피쳐를 추출함
    @classmethod
    def getImageFeature(cls, imageDirectory):

        img_paths = cls.__getSampleImagesPath(imageDirectory)

        for img_path in sorted(img_paths):
            print(img_path)
            img_name = path.splitext(path.basename(img_path))[0]

            # npy 형식으로 파일을 저장함
            feature_path = f"static/feature/{img_name}.npy"

            # 만약 피쳐 파일이 존재하면 넘긴다
            if(path.isfile(feature_path)):
                continue

            # 이미지 피쳐를 추출함
            fea = fe()
            feature = fea.extract(img=Image.open(img_path))

            np.save(feature_path, feature)

    # feature 들을 불러옴
    @classmethod
    def loadFeatures(cls, feature_dir):
        # 폴더 경로에 있는 모든 이미지 리스트를 가져옴
        featurePathList = glob.glob(feature_dir)

        features = []
        img_paths = []
        np_paths = []

        # 이미지 경로를 불러옴
        for feature in featurePathList:
            print(feature)
            saved_feature = np.load(feature)
            features.append(saved_feature)

            img_paths.append(Path("./static/bayc") /
                             (path.basename(feature).split('.')[0] + ".png"))
            np_paths.append(path.basename(feature).split('.')[0])

        return features, img_paths, np_paths
