int etatBouton;
int brochePotar = A0;
int valPotar;
float delai;

void setup() {
  // put your setup code here, to run once:
  pinMode(2,OUTPUT);
  pinMode(3,INPUT);
  digitalWrite(3,HIGH);
}

void loop() {
  // put your main code here, to run repeatedly:
  valPotar = analogRead(brochePotar);
  delai = map(valPotar,0,1023,0.1,15);
  
  etatBouton=digitalRead(3);
  if(!etatBouton){
    digitalWrite(2,HIGH);
    delay(delai);
    digitalWrite(2,LOW);
    delay(delai);
  }
}
