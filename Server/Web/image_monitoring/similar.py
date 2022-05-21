import timeit
import cv2
import os
import glob
from skimage.metrics import structural_similarity as compare_ssim
import numpy as np
# TODO

# 상위 경로 이동해서 이미지 디렉토리 가져오기
IMG_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))+"/media/Uploaded_Files/*"

# 이미지 파일 이름만 잘라내기 위해 경로 길이 저장
IMG_DIR_LEN = len(IMG_DIR) - 1

class SimilarImage:
    def __init__(self, path, name, score, thresh):
        self.path = path
        self.name = name
        self.score = score
        self.thresh = thresh

def getOriginalImages():
    # 폴더 경로에 있는 모든 이미지 리스트 가져오기
    imagePathList = glob.glob(IMG_DIR)
    imageList = []
    
    # 이미지 경로를 불러오기
    for i in imagePathList:
        imageList.append(i)

    return imageList


# targetImage : 비교 대상인 이미지
# size : resize 할  이미지 크기
def compareImage(targetImg,size):
    imgList = getOriginalImages()
    
    similarImgList = []
    differentImgList = []
    scorelist = []

    # target image를 gray로 변경
    grayA = cv2.cvtColor(targetImg, cv2.COLOR_BGR2GRAY)
    img_num = len(imgList)

    start = timeit.default_timer()
    for i, img in enumerate(imgList):

        # 이미지를 읽어와 회색으로 변환
        tmpImg = cv2.imread(img)

        # 파일명 한글인 경우 np로 읽고 cv로 변환
        if tmpImg is None:
            Img_array = np.fromfile(img, np.uint8)
            tmpImg = cv2.imdecode(Img_array, cv2.IMREAD_COLOR)

        tmpImg = cv2.resize(tmpImg,size)
        grayImg = cv2.cvtColor(tmpImg, cv2.COLOR_BGR2GRAY)

        # 이미지 유사도 비교
        score, diff = compare_ssim(grayA, grayImg, full=True)

        # 차이점 발견
        diff = (diff * 255).astype('uint8')
        thresh = cv2.threshold(diff, 0, 255,
                               cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
        # 유사도, 이미지 리스트 저장
        scorelist.append(score)
        similarImgList.append(img[IMG_DIR_LEN:])

        # 차이점 표시한 이미지 리스트 저장
        different_img = targetImg.copy()
        different_img[thresh == 255] = [0, 255, 255]
        differentImgList.append(different_img)

    end = timeit.default_timer()
    time = end - start
    # 유사도 제일 높은 이미지 리턴
    max_index = scorelist.index(max(scorelist))
    return (similarImgList[max_index], scorelist[max_index], differentImgList[max_index], img_num, time)

