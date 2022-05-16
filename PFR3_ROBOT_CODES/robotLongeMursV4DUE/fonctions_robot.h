#if !defined FONCTIONS_ROBOT_H
  #define FONCTIONS_ROBOT_H
  #include <Arduino.h>

#include <Servo.h>

const int bipPin = 8;

float lectureCapteurAvant(void);
float lectureCapteurAvantGauche(void);
float lectureCapteurLateral(void);
void affichageDansLeTerminal(float c1distance, float c2distance,float c3distance);
void bipInitialisation(void);
void bip(void);

#endif
