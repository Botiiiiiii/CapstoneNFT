from django.db import models

# Create your models here.
class User(models.Model):
    address = models.CharField(max_length=50, db_collation='latin1_swedish_ci')
    publickey = models.CharField(db_column='publicKey', primary_key=True, max_length=150)  # Field name made lowercase.
    nickname = models.CharField(unique=True, max_length=10)
    alert = models.IntegerField(blank=True, null=True)
    token = models.CharField(max_length=45, blank=True, null=True)
    pw = models.CharField(max_length=45)
    privatekey = models.CharField(db_column='privateKey', max_length=150)  # Field name made lowercase.
    keystore = models.CharField(db_column='keyStore', max_length=150, blank=True, null=True)  # Field name made lowercase.
    profile_img = models.CharField(max_length=100, blank=True, null=True)

    class Meta:
        # managed = False
        db_table = 'user'