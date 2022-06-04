
import cv2
from os import path as path
import os
import glob

from skimage.metrics import structural_similarity as compare_ssim

'''
'''

# TODO
# 이미지 크기를 조정하는 함수

'''
이미지 비교 성능
* compare_ssim() 함수 사용
  ㄴ 이미지가 어느정도로 유사한지, 어느 부위가 다른지를 확인할 수 있음
두 이미지 차이 %를 구하기 위해서는 Mean squared error 혹은 ssim을 사용할 수 있음
- SSIM : 원본이미지와 왜곡된 이미지가 있을 때, 두 이미지의 휘도, 대비, 구조를 비교
- MSE : 원본 이미지, 복원 이미지가 있으면 두 이미지 픽셀 값들의 차이 측정값   
'''

IMG_DIR = 'sample_image\*'
TARGET_IMAGE = 'target_image\diff.png'
TARGET_IMAGE_NAME = path.splitext(path.basename(TARGET_IMAGE))[0]


class SimilarImage:
    def __init__(self, path, name, score, thresh):
        self.path = path
        self.name = name
        self.score = score
        self.thresh = thresh

    def printr(self):
        print(self.path)
        print(self.name)
        print(self.score)
        print(self.thresh)


def getOriginalImages():
    # 폴더 경로에 있는 모든 이미지 리스트를 가져옴
    imagePathList = glob.glob(IMG_DIR) #glob: 파일들의 리스트 뽑을 때 경로명을 통해 손쉽게 가능
    # imagePathList = [os.path.join(IMG_DIR,imgName) for imgName in imageList]

    imageList = []

    # 이미지 경로를 불러옴
    for i in imagePathList:
        imageList.append(i)

    return imageList


def getTargetImage():

    image = cv2.imread(TARGET_IMAGE)

    return image


# imgList : grayscale된 이미지 리스트
# targetImage : 비교 대상인 이미지
def compareImage(imgList, targetImg):
    similarImgList = []

    # 먼저 target image를 gray로 변경
    grayA = cv2.cvtColor(targetImg, cv2.COLOR_BGR2GRAY)

    for i, img in enumerate(imgList):

        # 이미지를 읽어와 회색으로 변환
        tmpImg = cv2.imread(img)
        grayImg = cv2.cvtColor(tmpImg, cv2.COLOR_BGR2GRAY)

        # 이미지 유사도를 비교함
        score, diff = compare_ssim(grayA, grayImg, full=True) #반환값: 1. 두 이미지의 유사도 2. 실제 각 픽셀의 차이를 담은 2차원 배열

        # print(f'유사도(SSIM): {score:.6f}')

        # 만약 유사도가 0.9 이상인 경우 배열에 저장
        if score < 0.9: continue

        # 차이점 발견
        diff = (diff * 255).astype('uint8') #배열의 원소는 0~1사이의 값을 가지고 있음, 255를 곱해서 그레이스케일 이미지로 변환 가능
        thresh = cv2.threshold(diff, 0, 255,
                               cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
        #threshold: 특정 임계치를 기준으로 값을 바꿈
        # cv2.imshow("Debug", cv2.resize(thresh, (960, 540)))
        # cv2.waitKey(0)

        # 차이점 추가
        imgMeta = SimilarImage(img, path.splitext(path.basename(img))[0], score, thresh)
        similarImgList.append(imgMeta)

    return similarImgList

def saveSimilarImgList(targetImage, similarImgList):
    for simImg in similarImgList:
        imageCopy = targetImage.copy()

        # 이미지의 차이점을 색칠
        imageCopy[simImg.thresh == 255] = [0, 0, 255]


        scoreStr = str(round(simImg.score, 2))
        diffImgPath = path.join('diff_image', TARGET_IMAGE_NAME, '[' + scoreStr + ']' + simImg.name + '.png')

        # print('save path: ' + diffImgPath)
        os.makedirs(path.join('diff_image', TARGET_IMAGE_NAME), exist_ok=True)
        cv2.imwrite(diffImgPath, imageCopy)

        # cv2.imshow("Difference", cv2.resize(imageCopy, (960, 540)))
        # cv2.waitKey(0)

    return


def main():
    # 이미지 리스트를 받아옴
    imageList = getOriginalImages()

    # print(imageList)

    # 비교할 이미지를 수신
    targetImage = getTargetImage()

    # 각 이미지와 비교함
    # return : 유사한 흑백 이미지 리스트
    similarImgList = compareImage(imageList, targetImage)

    # 비슷한 이미지를 저장함
    saveSimilarImgList(targetImage, similarImgList)

    # 가장 높은 유사도를 가진 이미지를 보여줌

main()

