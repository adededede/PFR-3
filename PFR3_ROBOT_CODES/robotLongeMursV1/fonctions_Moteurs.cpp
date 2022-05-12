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

 void TournerGauche(Servo rouesGauches,Servo RouesDroites,int millisecondeRoueGauches ){
    rouesGauches.writeMicroseconds(millisecondeRoueGauches);
    RouesDroites.writeMicroseconds(1500);
 }

 void TournerDroite(Servo RouesDroites,Servo rouesGauches){
    RouesDroites.writeMicroseconds(millisecondeRoueGauches);
    rouesGauches.writeMicroseconds(1500);
  
 }
