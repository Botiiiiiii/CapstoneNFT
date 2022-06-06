from django.contrib.staticfiles.urls import staticfiles_urlpatterns
from . import views
from django.urls import path
from django.conf import settings
from django.conf.urls.static import static

app_name = "user_manage"

urlpatterns = [
    path("", views.index, name = "index"),
    path("user/", views.user, name = "index")
]
