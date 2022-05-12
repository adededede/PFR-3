#include "fonctions_Moteurs.h"



void StopPaireDeRoue(Servo paireDeRoue){
  paireDeRoue.writeMicroseconds(1500);
  }


void arretTotal(Servo rouesGauches,Servo RouesDroites){
  StopPaireDeRoue(rouesGauches);
  StopPaireDeRoue(RouesDroites);
  delay(1000);
  }

void arretTotal(Servo rouesGauches,Servo RouesDroites,int delai){
  StopPaireDeRoue(rouesGauches);
  StopPaireDeRoue(RouesDroites);
  delay(delai);
  }

 void tournerGauche(Servo rouesGauches,Servo rouesDroites,int millisecondeRoueGauches ){
    rouesGauches.writeMicroseconds(millisecondeRoueGauches);
    rouesDroites.writeMicroseconds(1580);
 }

 void tournerDroite(Servo rouesDroites,Servo rouesGauches,int millisecondeRoueDroites){
    rouesDroites.writeMicroseconds(millisecondeRoueDroites);
    rouesGauches.writeMicroseconds(1580);
  
 }

 void avancer(Servo rouesDroites,Servo rouesGauches,int millisecondes){
  
  rouesDroites.writeMicroseconds(millisecondes);
  rouesGauches.writeMicroseconds(millisecondes);
  
 }


 
