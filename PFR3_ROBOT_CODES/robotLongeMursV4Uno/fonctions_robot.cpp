#include "fonctions_robot.h"

float lectureCapteurAvant(void) {
  digitalWrite(c1TrigPin, LOW);
  delayMicroseconds(2);
  //envoit un signal de 10 µs
  digitalWrite(c1TrigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(c1TrigPin, LOW);
  //mesure la durée du cycle émission ultrason / réception
  //et la traduit en distance réelle mesurée (en cm)
  float c1distance = pulseIn(c1EchoPin, HIGH) / 58.00; //commence un timer jusqu'à que c1EchoPin soit à HIGH
  return c1distance;
}

float lectureCapteurLateralAvant(void) {
  digitalWrite(c2TrigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(c2TrigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(c2TrigPin, LOW);
  float c2distance = pulseIn(c2EchoPin, HIGH) / 58.00;
  return c2distance;
}

float lectureCapteurLateralArriere(void) {
  digitalWrite(c3TrigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(c3TrigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(c3TrigPin, LOW);
  float c3distance = pulseIn(c3EchoPin, HIGH) / 58.00;
  return c3distance;
}
