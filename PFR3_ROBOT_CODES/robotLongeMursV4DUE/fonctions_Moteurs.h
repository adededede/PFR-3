#if !defined FONCTIONS_MOTEURS_H
  #define FONCTIONS_MOTEURS_H
  #include <Arduino.h>
#include <Servo.h>

void arretTotal(Servo rouesGauches,Servo RouesDroites);
void StopPaireDeRoue(Servo paireDeRoue);
void arretTotal(Servo rouesGauches,Servo RouesDroites,int delai);

void tournerGauche(Servo rouesGauches,Servo rouesDroites);
void tournerDroite(Servo rouesDroites,Servo rouesGauches);

void avancer(Servo rouesDroites,Servo rouesGauches,int millisecondes);

#endif
