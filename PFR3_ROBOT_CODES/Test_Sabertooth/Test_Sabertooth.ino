// Sweep Sample
// Copyright (c) 2012 Dimension Engineering LLC
// See license.txt for license details.

#include <Servo.h>


Servo sg, sd; // We'll name the Sabertooth servo channel objects ST1 and ST2.

void setup()
{
  Serial.begin(9600);

  sg.attach(9);//  paire gauche
  sd.attach(10); // paire droite

  sg.writeMicroseconds(1500);
  sd.writeMicroseconds(1500);
  delay(1000);

}

void loop() {
  sg.writeMicroseconds(1500);
  sd.writeMicroseconds(1500);
  delay(1000);
  sg.writeMicroseconds(2000); //1800 -> vers l'avant
  sd.writeMicroseconds(1000);
  delay(2000);
  sg.writeMicroseconds(1500);
  sd.writeMicroseconds(1500);
  delay(1500);
  sg.writeMicroseconds(1000);
  sd.writeMicroseconds(2000);
  delay(1000);
}
