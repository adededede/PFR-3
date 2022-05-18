/*---------------------------------------------------------------*/
/*--------------------------ROBOT ARDUINO------------------------*/
/*---------------------------------------------------------------*/
/* AVANT LA MISE EN MARCHE                                       */
/* PLACER LE ROBOT PRET D'UN MUR A LA GAUCHE DU ROBOT            */
/*---------------------------------------------------------------*/

#include "fonctions_robot.h"
#include "fonctions_Moteurs.h"

//déclare sd et sg de type Servo
Servo sg, sd;

//variables globales
float c1distance = 100;//on met par défaut une valeur non critique pour ne pas entrer
float c2distance = 100;//dans un if dès la mise en marche du robot
float c3distance = 30;

volatile bool isEvitementObstacle = false;//volatile car ces variables peuvent etre modifiees par une ISR(interrupt service routine)
volatile bool isPlusDeMur = false;
volatile bool isRedresseDroit = false;
volatile bool isRedresseGauche = false;
volatile bool finDeMur = false;

unsigned long startTime;
unsigned long currentTime;
const unsigned long period = 1000;

void setup()
{
  noInterrupts();
  //initialise le timer
  startTime = millis();

  //initialisation de la communication avec le moniteur série
  Serial.begin(9600);
  Serial.println("setup");
  //pin utilisee pour le bip
  pinMode(bipPin, OUTPUT);
  //déclaration des pins qui lisent les déclenchement d'interruptions
  pinMode(2, INPUT_PULLUP);//INPUT_PULLUP ; utilisation résistance interne
  pinMode(3, INPUT_PULLUP);
  pinMode(4, INPUT_PULLUP);
  pinMode(5, INPUT_PULLUP);
  pinMode(6, INPUT_PULLUP);
  //pin qui déclenchent les interruptions chez la UNO
  pinMode(13, OUTPUT);
  digitalWrite(13, HIGH); //déclenche interruption en passant à LOW

  //déclaration des interruptions
  attachInterrupt(digitalPinToInterrupt(2), evitementObstacle, FALLING);//quand pin 2 passe de HIGH à LOW execute l'ISR arretUrgence
  attachInterrupt(digitalPinToInterrupt(3), plusDeMur,  FALLING);
  attachInterrupt(digitalPinToInterrupt(4), redresseDroit, FALLING);
  attachInterrupt(digitalPinToInterrupt(5), redresseGauche, FALLING);
  attachInterrupt(digitalPinToInterrupt(6), finPlusDeMur, FALLING);

  //faire 3 bips pour annoncer départ du robot
  //bipInitialisation();

  //servo.attach() positionne moteurs à la derniere valeur utilisee via servo.write();
  sg.write(1500);//positionne les roues à l'arret
  sd.write(1500);//1000 2000 1500
  sg.attach(9);//  paire droite (CH2)
  sd.attach(10); // paire gauche (CH1)(oui il y a inversion)

  //on fait avancer le robot tout droit
  avancer(sd, sg, 1600);

  interrupts();
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

  else if (isPlusDeMur) {
    //arret
    arretTotal(sg, sd, 500);
    //tourne à gauche pour relonger le du mur (tourne à 90° a gauche)
    tournerGauche(sd, sg);
    //on enleve interruption plus de mur le temps que le robot reviennent pres du mur
    //apres avoir tourne a gauche
    detachInterrupt(digitalPinToInterrupt(3));
    //previent la UNO qu'il doit scruter la fin de plus de mur
    digitalWrite(13, LOW);
    delay(50);
    digitalWrite(13, HIGH);
    delay(500);
    //on remet le robot droit en marche avant
    avancer(sg, sd, 1580);
    //on prépare la prochaine interruption en cas d'abscence de mur
    isPlusDeMur = false;
  }

  else if (isRedresseDroit) {
    sd.writeMicroseconds(1650);
    delay(80);
    sd.writeMicroseconds(1580);
    //on prépare la prochaine interruption en cas de redressage à droite
    isRedresseDroit = false;
  }

  else if (isRedresseGauche) {
    sg.writeMicroseconds(1650);
    delay(150);
    sg.writeMicroseconds(1580);
    //on prépare la prochaine interruption en cas de redressage à gauche
    isRedresseGauche = false;
  }

  else if (finDeMur) {
    attachInterrupt(digitalPinToInterrupt(3), plusDeMur,  FALLING);
    finDeMur = false;
  }
  /*
     SUPPRIMER LES DELAY POUR QUE L'ENVOI SOIT PRECIS EN PERIODE
  */
  /*//envoi de données périodiquement
    currentTime = millis();
    if (currentTime - startTime >= period)//si periode écoulee
    {
    //envoi données
    startTime = currentTime;//remise a 0 du timer
    }*/

}
/*//fin loop*/

//ISR
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
void finPlusDeMur(void) {
  finDeMur = true;
}
