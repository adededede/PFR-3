#include "fonctions_robot.h"


float lectureCapteurAvant(void) {
  digitalWrite(c1TrigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(c1TrigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(c1TrigPin, LOW);
  float c1distance = pulseIn(c1EchoPin, HIGH) / 58.00; //commence un timer jusqu'à que c1EchoPin soit à HIGH
  return c1distance;
}

float lectureCapteurLateralAvant(void) {
  //delayMicroseconds(2);
  digitalWrite(c2TrigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(c2TrigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(c2TrigPin, LOW);
  float c2distance = pulseIn(c2EchoPin, HIGH) / 58.00;
  return c2distance;
}

float lectureCapteurLateralArriere(void) {
  //delayMicroseconds(2);
  digitalWrite(c3TrigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(c3TrigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(c3TrigPin, LOW);
  float c3distance = pulseIn(c3EchoPin, HIGH) / 58.00;
  return c3distance;
}

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
