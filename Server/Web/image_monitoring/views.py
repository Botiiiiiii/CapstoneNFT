import json
import time
import timeit

import numpy as np
from django.http import JsonResponse, HttpResponse
from django.shortcuts import render
from . import similar
from . import models
from PIL import Image
import cv2
from io import BytesIO
import base64

import requests

def uploadFile(request):
    # 비교하기 버튼 ajax request
    if request.method == "POST" and request.is_ajax():

        # file 받아오기
        file = request.FILES['file']

        url = 'http://34.64.110.121:8000/images/'

        files = {'file': file}


        s = timeit.default_timer()
        response = requests.post(url=url, files=files)
        e = timeit.default_timer()

        time = e - s

        s1 = timeit.default_timer()
        r = requests.post(url=url)
        e1 = timeit.default_timer()
        t1 = e1 - s1
        print(time, t1)

        different_img_str = "data:image/png;base64," + base64.b64encode(response.content).decode()
        # different_img_file = base64.b64encode(output.getvalue()).decode()
        #different_img_file = different_img_file.split('\n')[0]

        return HttpResponse(json.dumps({'dif_img':different_img_str,'time':time}),content_type="application/json")

    # 업로드 버튼 submit request(미구현)
    elif request.method == "POST":
        # 파일 저장
        file = request.FILES['image']
        document = models.Document(
            uploadedFile = file
        )
        document.save()
        documents = models.Document.objects.all()

        return render(request, "image_monitoring/image_monitoring.html", context = {
            "files": documents
        })
    else:
        documents = models.Document.objects.all()

        return render(request, "image_monitoring/image_monitoring.html", context={
            "files": documents})