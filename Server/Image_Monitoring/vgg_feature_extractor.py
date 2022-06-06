import numpy as np
from os import path as path
import glob
import tensorflow as tf
from tensorflow import keras
from keras_preprocessing import image
from keras.applications.vgg16 import VGG16, preprocess_input
from keras.models import Model
from pathlib import Path
from PIL import Image
import matplotlib.pyplot as plt

# 학습에 사용되는 이미지 디렉토리
# 현재 테스트에서는 bored apes yacht club nft 이미지 만개를 사용함
IMG_DIR = 'bayc\*'

# 추출된 피쳐가 저장되는 디렉토리
FEATURE_DIR = 'feature\*'


# 검색할 이미지를 설정함
TARGET_IMAGE = 'target_image\\kitty.jpg'


class FeatureExtractor:
    '''
    Ref.
        https://github.com/matsui528/sis

        - 해당 코드를 참고하여 개발
    '''

    def __init__(self):
        self.__gpu_settings()

        # Use VGG-16 as the architecture and ImageNet for the weight
        # 피쳐 추출은 VGG-16을 활용하였으며, imagenet의 가중치를 활용함
        base_model = VGG16(weights='imagenet')

        self.model = Model(inputs=base_model.input,
                           outputs=base_model.get_layer('fc1').output)

    def __gpu_settings(self):
        gpus = tf.config.experimental.list_physical_devices('GPU')
        if gpus:
            try:
                # Restrict TensorFlow to only use the fourth GPU
                tf.config.experimental.set_visible_devices(gpus[0], 'GPU')

                # Currently, memory growth needs to be the same across GPUs
                for gpu in gpus:
                    tf.config.experimental.set_memory_growth(gpu, True)
                logical_gpus = tf.config.experimental.list_logical_devices(
                    'GPU')
                print(len(gpus), "Physical GPUs,", len(
                    logical_gpus), "Logical GPUs")
            except RuntimeError as e:
                # Memory growth must be set before GPUs have been initialized
                print(e)

    def extract(self, img):

        # 이미지 사이즈를 224, 224로 조정
        img = img.resize((224, 224))
        img = img.convert('RGB')

        # 이미지 피쳐 추출
        x = image.img_to_array(img)
        x = np.expand_dims(x, axis=0)
        x = preprocess_input(x)

        feature = self.model.predict(x)[0]
        return feature / np.linalg.norm(feature)


# 결과값 출력
def searchImage(img):

    # 피쳐 추출
    query = fe.extract(img)

    # 이미지 사이의 similarity를 계산함
    dists = np.linalg.norm(features - query, axis=1)

    # 가장 가까운 거리 10개를 가져옴
    ids = np.argsort(dists)[:10]
    scores = [(dists[id], img_paths[id]) for id in ids]
    fig = plt.figure(figsize=(8, 8))

    axes = []
    for a in range(10):
        score = scores[a]
        axes.append(fig.add_subplot(2, 5, a+1))
        subplot_title = str(score[0])
        axes[-1].set_title(subplot_title)

        plt.axis('off')
        plt.imshow(Image.open(score[1]))

    fig.tight_layout()
    plt.show()


if __name__ == "__main__":

    from init_settings import Init

    # feature extractor를 객체화
    fe = FeatureExtractor()
    print("--")
    # 각 이미지에서 피쳐를 추출함
    # 추출한 feature는 feature directory에 npy 형태로 저장
    Init.getImageFeature(IMG_DIR)

    # 피쳐파일로부터 피쳐를 로드함
    # 한번만 진행
    features, img_paths = Init.loadFeatures()

    # 쿼리할 이미지 수신
    img = Image.open(TARGET_IMAGE)

    searchImage(img)