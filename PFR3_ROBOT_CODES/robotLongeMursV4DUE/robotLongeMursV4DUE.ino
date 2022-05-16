/*---------------------------------------------------------------*/
/*--------------------------ROBOT ARDUINO------------------------*/
/*---------------------------------------------------------------*/
/* AVANT LA MISE EN MARCHE                                       */
/* PLACER LE ROBOT PRET D'UN MUR A LA GAUCHE DU ROBOT            */
/*---------------------------------------------------------------*/

/*
   VERIFIER QUE PENDANT L EXECUTION D UNE INTERRUPTION IL N Y EN A PAS UNE AUTRE
*/

#include "fonctions_robot.h"
#include "fonctions_Moteurs.h"
Servo sg, sd;

int val = 0;
float c1distance = 100;//on met par défaut une valeur non critique pour ne pas entrer
float c2distance = 100;//dans un if dès la mise en marche du robot
float c3distance = 30;
//c1 : capteur avant
//c2 : capteur avant gauche
//c3 : capteur lateral

volatile bool isEvitementObstacle = false;
volatile bool isPlusDeMur = false;
volatile bool isRedresseDroit = false;
volatile bool isRedresseGauche = false;


void setup()
{
  Serial.begin(9600);

  sg.attach(9);//  paire droite
  sd.attach(10); // paire gauche (oui il y a inversion)

  pinMode(bipPin, OUTPUT); //pour le bip

  //déclaration des pins qui lisent les déclenchement d'interruptions
  pinMode(2, INPUT_PULLUP);
  pinMode(3, INPUT_PULLUP);
  pinMode(4, INPUT_PULLUP);
  pinMode(5, INPUT_PULLUP);
  //déclaration des interruptions
  attachInterrupt(digitalPinToInterrupt(2), evitementObstacle, FALLING);//quand pin 2 passe de état bas à état haut, execute arretUrgence
  attachInterrupt(digitalPinToInterrupt(3), plusDeMur,  FALLING);
  attachInterrupt(digitalPinToInterrupt(4), redresseDroit, FALLING);
  attachInterrupt(digitalPinToInterrupt(5), redresseGauche, FALLING);

  //faire 3 bips pour annoncer départ du robot
  bipInitialisation();
  
  //on fait avancer le robot tout droit
  arretTotal(sg, sd, 500);
  avancer(sd, sg, 1580);
}//fin setup

void loop() {
  if (isEvitementObstacle) {
    //arret
    arretTotal(sg, sd, 500);
    //tourne à droite pour éviter obstacle (tourne à 90° a droite)
    tournerDroite(sg, sd);
    //on remet le robot droit en marche avant
    avancer(sg, sd, 1580);
    //on prépare la prochaine interruption en cas de d'obstacle
    isEvitementObstacle = false;
  }
  if (isPlusDeMur) { //fonctionne si au moins un des deux capteurs voit le mur
    //arret
    arretTotal(sg, sd, 500);
    //tourne à gauche pour relonger le du mur (tourne à 90° a gauche)
    tournerGauche(sd, sg);
    //on remet le robot droit en marche avant
    avancer(sg, sd, 1580);
    delay(2000);
    //on prépare la prochaine interruption en cas d'abscence de mur
    isPlusDeMur = false;
  }
  else if (isRedresseDroit) {
    sd.writeMicroseconds(1650);
    delay(100);
    sd.writeMicroseconds(1580);
    //on prépare la prochaine interruption en cas de redressage à droite
    isRedresseDroit = false;
  }
  else if (isRedresseGauche) {
    sg.writeMicroseconds(1650);
    delay(130);
    sg.writeMicroseconds(1580);
    //on prépare la prochaine interruption en cas de redressage à gauche
    isRedresseGauche = false;
  }

}//fin loop

//interruptions
void evitementObstacle(void) {
  isEvitementObstacle = true;
}
void plusDeMur(void) {
  isPlusDeMur = true;
}
void redresseDroit(void) {
  isRedresseDroit = true;
}
void redresseGauche(void) {
  isRedresseGauche = true;
}
