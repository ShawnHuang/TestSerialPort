/*
   MPU-6050
   MPU6050      Arduino
   VCC          ->  3.3V
   GND          ->  GND
   SCL          ->  SCL (uno: A5)
   SDA          ->  SDA (uno: A4)
*/

#include "Wire.h"
#include "I2Cdev.h"
#include "MPU6050.h"


MPU6050 accelgyro;
int16_t ax, ay, az;
int16_t gx, gy, gz;
int16_t reset_count;

void setup() {
  Serial.begin(9600);

  Wire.begin();
  Serial.println("Initializing I2C devices...");
  accelgyro.initialize();
  reset_count = 0;
}

void loop(){
  reset_count ++;
  accelgyro.getMotion6(&ax, &ay, &az, &gx, &gy, &gz);	
  
  char a_buffer[32], g_buffer[32];
  
  sprintf(a_buffer,"%i,%i,%i", ax, ay, az);
  sprintf(g_buffer,",%i,%i,%i", gx, gy, gz);
  
  Serial.print(a_buffer);
  Serial.print(g_buffer);
  
  if(reset_count > 1000) {    
    Serial.println("Reset FIFO of MPU!!");
    accelgyro.resetFIFO();
    reset_count = 0;
  }
  
}

