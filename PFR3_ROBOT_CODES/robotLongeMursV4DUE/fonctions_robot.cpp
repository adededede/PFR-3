#include "fonctions_robot.h"

const int bipPin = 8;

void affichageDansLeTerminal(float c1distance, float c2distance, float c3distance) {
  Serial.print("capteur 1 distance :");
  Serial.print(c1distance);
  Serial.print("cm\n");
  Serial.print("capteur 2 distance :");
  Serial.print(c2distance);
  Serial.print("cm\n");
  Serial.print("capteur lateral distance :");
  Serial.print(c3distance);
  Serial.print("cm\n");
}

void bipInitialisation(void) {
  digitalWrite(bipPin, HIGH);
  delay(500);
  digitalWrite(bipPin, LOW);
  delay(500);
  digitalWrite(bipPin, HIGH);
  delay(500);
  digitalWrite(bipPin, LOW);
  delay(500);
  digitalWrite(bipPin, HIGH);
  delay(500);
  digitalWrite(bipPin, LOW);
}

void bip(void){
  digitalWrite(bipPin, HIGH);
  delay(500);
  digitalWrite(bipPin, LOW);
}
