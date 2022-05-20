#if !defined FONCTIONS_ROBOT_H
#define FONCTIONS_ROBOT_H
#include <Arduino.h>

#include <Servo.h>

//declaration des pins
const int c1TrigPin = 8;
const int c1EchoPin = 3;
const int c2TrigPin = 4;
const int c2EchoPin = 5;
const int c3TrigPin = 6;
const int c3EchoPin = 7;

const int bipPin = 14;

const int interruptFromDUE = 2;

const int obstaclePin = 9;
const int plusDeMurPin = 10;
const int redresseDPin = 11;
const int redresseGPin = 12;
const int finPlusDeMurPin = 13;



float lectureCapteurAvant(void);
float lectureCapteurLateralAvant(void);
float lectureCapteurLateralArriere(void);
void affichageDansLeTerminal(float c1distance, float c2distance, float c3distance);
void bipInitialisation(void);
void bip(void);

#endif
