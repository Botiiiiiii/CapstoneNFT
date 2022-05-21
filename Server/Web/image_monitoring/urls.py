from django.contrib.staticfiles.urls import staticfiles_urlpatterns

from . import views
from django.urls import path
from django.conf import settings
from django.conf.urls.static import static

app_name = "image_monitoring"

urlpatterns = [
    path("", views.uploadFile, name = "uploadFile"),
]

# media 경로 접근을 위해 추가
if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
else:
    urlpatterns += staticfiles_urlpatterns()
