import RPi.GPIO as GPIO
import time

from firebase import firebase

firebase = firebase.FirebaseApplication('https://sample-raspi-3fc0b.firebaseio.com',None)

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
BUZZER= 26
buzzState = False
GPIO.setup(BUZZER, GPIO.OUT)

print("buzzer")


while True:
    alarm = firebase.get("sensor/Buzzer/Alarm", None);
    
    if alarm == "OFF":
        GPIO.output(BUZZER, False)
    elif alarm == "ON":
        GPIO.output(BUZZER, True)
