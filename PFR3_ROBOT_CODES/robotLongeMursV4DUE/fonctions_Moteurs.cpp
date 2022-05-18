#include "fonctions_Moteurs.h"
#include <SimplyAtomic.h>
#define A_CH1 30// GAUCHE
#define B_CH1 28

#define A_CH2 22// DROIT 
#define B_CH2 24
// POUR MESURER LA VITESSE ON MESURE LE TEMPS ECOULE ENTRE 2 TRIGGER
//https://www.youtube.com/watch?v=HRaZLCBFVDE
// variables de stockage globale;
volatile int pos_i_gauche = 0;
volatile int pos_i_droit = 0;
volatile float velocity_i_droit = 0;
volatile float velocity_i_gauche = 0;
volatile long prevT_i_droit = 0 ;
volatile long prevT_i_gauche = 0 ;

void initEncodeurs(void) {
  pinMode(A_CH1, INPUT_PULLUP);
  pinMode(B_CH1, INPUT);

  pinMode(A_CH2, INPUT_PULLUP);
  pinMode(B_CH2, INPUT);
  /*
    attachInterrupt(digitalPinToInterrupt(A_CH1), readEncodeurGauche, RISING);
    attachInterrupt(digitalPinToInterrupt(A_CH2), readEncodeurDroit, RISING);
  */
}

void readEncodeurGauche(void) {
  int pos = 0;
  int velocity_gauche2 = velocity_i_gauche;
  ATOMIC() {
    pos = pos_i_gauche;
    velocity_gauche2;
  }
  int b_gauche = digitalRead(B_CH1);
  int increment = 0;
  if (b_gauche > 0) {
    increment = 1;
  } else {
    increment = 1;
  }
  pos_i_gauche  = pos_i_gauche + increment;

  long currT = micros();
  float deltaT = ((float)(currT - prevT_i_gauche)) / 1.0e6;
  velocity_i_gauche = increment / deltaT;
  prevT_i_gauche = currT;

  Serial.print("vitesse gauche " );
  Serial.println(velocity_gauche2);
}

void readEncodeurDroit(void) {
  int velocity_droite2;
  int pos = 0;
  ATOMIC() {
    pos = pos_i_droit;
    velocity_droite2 = velocity_i_droit;
  }

  int b_droit = digitalRead(B_CH1);
  int increment = 0;
  if (b_droit > 0) {
    increment = 1;
  } else {
    increment = 1;
  }
  pos_i_droit  = pos_i_droit + increment;

  long currT = micros();
  float deltaT = ((float) (currT - prevT_i_droit)) / 1.0e6;
  velocity_i_droit = increment / deltaT;
  prevT_i_droit = currT;

  Serial.print("vitesse droit " );
  Serial.println(velocity_droite2);
}

void StopPaireDeRoue(Servo paireDeRoue) {
  paireDeRoue.writeMicroseconds(1500);
}

void arretTotal(Servo rouesGauches, Servo RouesDroites) {
  StopPaireDeRoue(rouesGauches);
  StopPaireDeRoue(RouesDroites);
  delay(1000);
}

void arretTotal(Servo rouesGauches, Servo RouesDroites, int delai) {
  StopPaireDeRoue(rouesGauches);
  StopPaireDeRoue(RouesDroites);
  delay(delai);
}

void tournerGauche(Servo rouesGauches, Servo rouesDroites) {
  rouesGauches.writeMicroseconds(1500);
  rouesDroites.writeMicroseconds(1700);
  delay(600);
}

void tournerDroite(Servo rouesDroites, Servo rouesGauches) {
  rouesDroites.writeMicroseconds(1300);
  rouesGauches.writeMicroseconds(1600);
  delay(700);
}

void avancer(Servo rouesDroites, Servo rouesGauches, int millisecondes) {
  rouesDroites.writeMicroseconds(millisecondes);//1580
  rouesGauches.writeMicroseconds(millisecondes);
}

void reculer (Servo rouesDroites, Servo rouesGauches) {
  rouesDroites.writeMicroseconds(1300);
  rouesGauches.writeMicroseconds(1300);
  delay(1000);
}



void tournerGaucheBT(Servo rouesGauches, Servo rouesDroites){
   rouesGauches.writeMicroseconds(1700);
   rouesDroites.writeMicroseconds(1300);
  delay(500);
}




void tournerDroiteBT(Servo rouesGauches, Servo rouesDroites){
   rouesGauches.writeMicroseconds(1700);
   rouesDroites.writeMicroseconds(1300);
  delay(500);
}
