/*---------------------------------------------------------------*/
/*--------------------------ROBOT ARDUINO------------------------*/
/*---------------------------------------------------------------*/
/* AVANT LA MISE EN MARCHE                                       */
/* PLACER LE ROBOT PRET D'UN MUR A LA GAUCHE DU ROBOT            */
/*---------------------------------------------------------------*/

#include "fonctions_robot.h"
#include "fonctions_Moteurs.h"
Servo sg, sd;

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

  pinMode(c1TrigPin, OUTPUT);
  pinMode(c1EchoPin, INPUT);
  pinMode(c2TrigPin, OUTPUT);
  pinMode(c2EchoPin, INPUT);
  pinMode(c3TrigPin, OUTPUT);
  pinMode(c3EchoPin, INPUT);

  pinMode(bipPin, OUTPUT); //pour le bip

  //déclaration des interruptions
  attachInterrupt(digitalPinToInterrupt(11), evitementObstacle, RISING);//quand pin 3 passe de état bas à état haut, execute arretUrgence
  attachInterrupt(digitalPinToInterrupt(12), plusDeMur,  RISING);
  attachInterrupt(digitalPinToInterrupt(13), redresseDroit, RISING);
  attachInterrupt(digitalPinToInterrupt(14), redresseGauche, RISING);

  //faire 3 bips pour annoncer départ du robot
  bipInitialisation();

  //on fait avancer le robot tout droit
  avancer(sd, sg, 1580);
}//fin setup

void loop() {

  if(isEvitementObstacle){
    //arret
    arretTotal(sg, sd, 500);
    //tourne à droite pour éviter obstacle (tourne à 90° a droite)
    sg.writeMicroseconds(1300);
    sd.writeMicroseconds(1600);
    delay(650);
    //on remet le robot droit en marche avant
    avancer(sg, sd, 1580);
    //on prépare la prochaine interruption en cas de d'obstacle
    isEvitementObstacle=false;
  }
  else if(isPlusDeMur){//fonctionne si au moins un des deux capteurs voit le mur
    //arret
    arretTotal(sg, sd, 500);
    //tourne à gauche pour relonger le du mur (tourne à 90° a gauche)
    sd.writeMicroseconds(1500);
    sg.writeMicroseconds(1700);
    delay(900);
    //on remet le robot droit en marche avant
    avancer(sg, sd, 1580);
    //on prépare la prochaine interruption en cas d'abscence de mur
    isPlusDeMur=false;
  }
  else if(isRedresseDroit){
    sd.writeMicroseconds(1650);
    delay(100);
    sd.writeMicroseconds(1580);
    //on prépare la prochaine interruption en cas de redressage à droite
    isRedresseDroit=false;
  }
  else if(isRedresseGauche){
    sg.writeMicroseconds(1650);
    delay(100);
    sg.writeMicroseconds(1580);
    //on prépare la prochaine interruption en cas de redressage à gauche
    isRedresseGauche=false;
  }
}//fin loop

//interruptions
void evitementObstacle(void) {
  isEvitementObstacle = true;
}
void plusDeMur(void) {
  isPlusDeMur=true;
}
void redresseDroit(void){
  isRedresseDroit=true;
}
void redresseGauche(void){
  isRedresseGauche=true;
}
