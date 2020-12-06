import RPi.GPIO as GPIO
import time
from firebase import firebase

pinList = [17, 22, 23, 27]

SleepTimeL = 0.5
firebase = firebase.FirebaseApplication('https://sample-raspi-3fc0b.firebaseio.com',None)

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
for i in pinList: 
    GPIO.setup(i, GPIO.OUT) 
    GPIO.output(i, GPIO.HIGH)

def kidsRoomRelayOff():
  GPIO.output(17, GPIO.HIGH)

def kidsRoomRelayOn():
  GPIO.output(17, GPIO.LOW)
  
def BedRoomRelayOff():
  GPIO.output(27, GPIO.HIGH)

def BedRoomRelayOn():
  GPIO.output(27, GPIO.LOW)
  
def HallRelayOff():
  GPIO.output(22, GPIO.HIGH)

def HallRelayOn():
  GPIO.output(22, GPIO.LOW)

def BalconyRelayOff():
  GPIO.output(23, GPIO.HIGH)

def BalconyRelayOn():
  GPIO.output(23, GPIO.LOW)
  #time.sleep(SleepTimeL);
  #GPIO.cleanup()

while True:
  kidsRoom = firebase.get("sensor/Relay/KidsRoom", None);
  BedRoom = firebase.get("sensor/Relay/BedRoom", None);
  Hall = firebase.get("sensor/Relay/Hall", None);
  Balcony = firebase.get("sensor/Relay/Balcony", None);
  
  if kidsRoom == "ON":
    kidsRoomRelayOn()
  elif kidsRoom == "OFF":
    kidsRoomRelayOff()
    
  if BedRoom == "ON":
    BedRoomRelayOn()
  elif BedRoom == "OFF":
    BedRoomRelayOff()
  
  if Hall == "ON":
    HallRelayOn()
  elif Hall == "OFF":
    HallRelayOff()
    
  if Balcony == "ON":
    BalconyRelayOn()
  elif Balcony == "OFF":
    BalconyRelayOff()
