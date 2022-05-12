#if !defined FONCTIONS_MOTEURS_H
  #define FONCTIONS_MOTEURS_H
  #include <Arduino.h>
#include <Servo.h>
void arretTotal(Servo rouesGauches,Servo RouesDroites);
void StopPaireDeRoue(Servo paireDeRoue);
void arretTotal(Servo rouesGauches,Servo RouesDroites,int delai);

void TournerGauche(Servo rouesGauches,Servo RouesDroites );
void TournerDroite(Servo rouesGauches,Servo RouesDroites );
#endif
