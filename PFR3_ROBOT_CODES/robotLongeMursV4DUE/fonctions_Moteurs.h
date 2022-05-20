#if !defined FONCTIONS_MOTEURS_H
#define FONCTIONS_MOTEURS_H
#include <Arduino.h>
#include <Servo.h>

void initEncodeurs(void);

void readEncodeurGauche(void);
void readEncodeurDroit(void);

void arretTotal(Servo rouesGauches, Servo RouesDroites);
void StopPaireDeRoue(Servo paireDeRoue);
void arretTotal(Servo rouesGauches, Servo RouesDroites, int delai);

void avancer(Servo rouesDroites, Servo rouesGauches, int millisecondes);
void reculer (Servo rouesDroites, Servo rouesGauches);

void tournerGauche90(Servo rouesGauches, Servo rouesDroites);
void tournerDroite90(Servo rouesGauches, Servo rouesDroites);

void tournerGauche_petit_BT(Servo rouesGauches, Servo rouesDroites);
void tournerDroite_petit_BT(Servo rouesGauches, Servo rouesDroites);

#endif
