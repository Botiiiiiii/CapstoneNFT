from django.db import models

# 파일 저장시 사용 (미구현)
class Document(models.Model):
    uploadedFile = models.ImageField(upload_to = "Uploaded_Files/")
    dateTimeOfUpload = models.DateTimeField(auto_now = True)
