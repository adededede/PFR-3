//https://www.roboindia.com/tutorials

#include <SoftwareSerial.h>

SoftwareSerial BTSerial(5, 6); // TX | RX

void setup()
{
  Serial.begin(9600);
  Serial.println("Enter AT commands:");
  BTSerial.begin(9600);       // HC-05 default speed in AT command more

}

void loop()
{
  /*------boucle pour recuperer donnee envoiyee bluetooth dans variable-----------*/
  int i = 0;
  char someChar[32] = {0};
  // when characters arrive over the serial port...
  if (Serial.available()) {
    do {
      someChar[i++] = Serial.read();
      delay(3);
    } while (Serial.available() > 0);
    BTSerial.println(someChar);
    Serial.println(someChar);
  }
  while (BTSerial.available())
    Serial.print((char)BTSerial.read());
  }
