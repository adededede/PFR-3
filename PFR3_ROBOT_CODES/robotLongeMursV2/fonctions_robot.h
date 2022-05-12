#if !defined FONCTIONS_ROBOT_H
  #define FONCTIONS_ROBOT_H
  #include <Arduino.h>

#include <Servo.h>

//declaration des pins
const int c1TrigPin = 2;
const int c1EchoPin = 3;
const int c2TrigPin = 4;
const int c2EchoPin = 5;
const int c3TrigPin = 6;
const int c3EchoPin = 7;
const int bipPin = 8;


float lectureCapteurAvant(void);
float lectureCapteurAvantGauche(void);
float lectureCapteurLateral(void);
void affichageDansLeTerminal(float c1distance, float c2distance,float c3distance);
void bipInitialisation(void);
void bip(void);

#endif
