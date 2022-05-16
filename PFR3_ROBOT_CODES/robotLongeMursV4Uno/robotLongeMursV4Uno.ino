/*---------------------------------------------------------------*/
/*--------------------------ROBOT ARDUINO------------------------*/
/*---------------------------------------------------------------*/
/* AVANT LA MISE EN MARCHE                                       */
/* PLACER LE ROBOT PRET D'UN MUR A LA GAUCHE DU ROBOT            */
/*---------------------------------------------------------------*/

/*ROLE DE LA CART UNO : MESURER CONTINUELLEMENT VIA LES 3 CAPTEURS
   ET NE COMMUNIQUER AVEC LA CARTE DUE QUE QUAND UNE DISTANCE
   CRITIQUE EST MESUREE
*/
/*
   FAIRE UN SYSTEME DE SITRIBUTION DE PAROLE POUR NE PAS ENVOYER PLUSIEURS INTERRUPTIONS A LA FOIS
*/

#include "fonctions_robot.h"
#include "fonctions_Moteurs.h"

Servo sg, sd;

float c1distance = 100;  //on met par défaut une valeur non critique pour ne pas entrer
float c2distance = 100;  //dans un if dès la mise en marche du robot
float c3distance = 30;
int diffLaterale = 0;

const int obstaclePin = 9;
const int plusDeMurPin = 10;
const int redresseDPin = 11;
const int redresseGPin = 12;

void setup() {
  Serial.begin(9600);

  pinMode(c1TrigPin, OUTPUT);
  pinMode(c1EchoPin, INPUT);
  pinMode(c2TrigPin, OUTPUT);
  pinMode(c2EchoPin, INPUT);
  pinMode(c3TrigPin, OUTPUT);
  pinMode(c3EchoPin, INPUT);

  pinMode(obstaclePin, OUTPUT);
  pinMode(plusDeMurPin, OUTPUT);
  pinMode(redresseDPin, OUTPUT);
  pinMode(redresseGPin, OUTPUT);

  //met les pins à HIGH car ils déclenchent interuption à leur passage à LOW
  digitalWrite(obstaclePin, HIGH);
  digitalWrite(plusDeMurPin, HIGH);
  digitalWrite(redresseDPin, HIGH);
  digitalWrite(redresseGPin, HIGH);

  /*
    //calcul temps de mesure capteurs (SANS CAPTEURS)
    long int t1 = millis();
    c1distance = lectureCapteurAvant();//693
    //c2distance = lectureCapteurAvantGauche();//693ms
    //c3distance = lectureCapteurLateral();//693
    long int t2 = millis();
    Serial.print("Time taken by the task: ");
    Serial.print(t2 - t1);
    Serial.println(" milliseconds");*/


}  //fin setup
void loop() {

  //on déclenche les capteurs et on lit leurs valeurs en continu
  //tant qu'aucun capteur ne détecte une distance critique
  /*
     TEST sans delay entre les mesures, a voir si ça ne parasite pas les mesures
  */

  c1distance = lectureCapteurAvant();
  c2distance = lectureCapteurAvantGauche();
  c3distance = lectureCapteurLateral();

  diffLaterale = c2distance - c3distance;
  /*Serial.println(c1distance);
    Serial.println(c2distance);
    Serial.println(c3distance);
    Serial.println(diffLaterale);
    Serial.println("");*/



  //evitement d'obstacle PRIORITAIRE
  if (c1distance < 20 && c1distance > 5 ) { //pour eviter les valeurs extremes en cas de non detection de mur
    digitalWrite(obstaclePin, LOW);//déclenche une interruption sur le programme principal de la DUE
    delay(50);
    digitalWrite(obstaclePin, HIGH);//prépare le prochain passage à HIGH
    //Serial.println("obstacle");
  }

  //si "plus de mur à gauche" alors tourne de 90° à gauche
  else if (c2distance >= 60 && c3distance >= 60) {
    digitalWrite(plusDeMurPin, LOW);
    delay(50);
    digitalWrite(plusDeMurPin, HIGH);
    //Serial.println("plus de mur");
  }

  //pour rester parallèle au mur
  /*
    RISQUE DE RALENTIR LE PROGRAMME
  */
  //declenche "redresseGauche" dans le programme de la DUE
  //diffLaterale < 15 pour éviter le cas où un seul capteur voit le mur après interruption "plusDeMur" dans le programme DUE
  else if ((diffLaterale > 0 && diffLaterale < 5) || c2distance > 30 || c3distance > 30 ) { //si positif, alors robot trop vers la droite
    digitalWrite(redresseGPin, LOW);
    delay(50);
    digitalWrite(redresseGPin, HIGH);
    //Serial.println("redresse gauche");
  }
  //declenche "redresseDroit" dans le programme de la DUE
  //diffLaterale < 15 pour la même raison
  else if ((diffLaterale < 0 && diffLaterale < 10) || c2distance < 15 || c3distance < 15) { //si négatif, alors robot trop vers la gauche
    digitalWrite(redresseDPin, LOW);
    delay(50);
    digitalWrite(redresseDPin, HIGH);
    //Serial.println("redresse droit");
  }

}//fin loop
