#include "fonctions_Moteurs.h"

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
  rouesDroites.writeMicroseconds(millisecondes);
  rouesGauches.writeMicroseconds(millisecondes);
}
