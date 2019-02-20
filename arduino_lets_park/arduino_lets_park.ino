#include <SoftwareSerial.h>
//시리얼 통신을 위한 객체선언
SoftwareSerial blueToothSerial(2, 3);//Rx, Tx

int distance[7];
char char_distance[7];

int set_delay = 20;
int count_1[10];
int count_2[10];


//초음파센서의 송신부를 trig번 핀으로 설정한다.
//초음파센서의 수신부를 echo번 핀으로 설정한다.
int echo1 = 12; 
int trig1 = 13; 
unsigned long duration1;
float distance1;

int echo2 = 10; 
int trig2 = 11; 
unsigned long duration2;
float distance2;

int echo3 = 8; 
int trig3 = 9; 
unsigned long duration3;
float distance3;

int echo4 = 6; 
int trig4 = 7; 
unsigned long duration4;
float distance4;

int echo5 = 4; 
int trig5 = 5; 
unsigned long duration5;
float distance5;

int echo6 = A2; 
int trig6 = A3; 
unsigned long duration6;
float distance6;


//실행시 가장 먼저 호출되는 함수이며, 최초 1회만 실행됩니다.
//변수를 선언하거나 초기화를 위한 코드를 포함합니다.
void setup() {
  // 초음파센서의 동작 상태를 확인하기 위하여 시리얼 통신을 설정합니다. (전송속도 9600bps)
  // 메뉴 Tool -> Serial Monitor 클릭
  Serial.begin(9600);//시리얼 모니터
  blueToothSerial.begin(9600);//블루투스 시리얼
  //blueToothSerial.println(F("AT+NAMElets_park"));

  // 초음파센서의 수신부로 연결된 핀을 INPUT으로 설정합니다.
  // 초음파센서의 송신부로 연결된 핀을 OUTPUT으로 설정합니다.
  pinMode(2, INPUT);
  pinMode(3, OUTPUT);

  pinMode(echo1, INPUT);
  pinMode(trig1, OUTPUT);

  pinMode(echo2, INPUT);
  pinMode(trig2, OUTPUT);

  pinMode(echo3, INPUT);
  pinMode(trig3, OUTPUT);

  pinMode(echo4, INPUT);
  pinMode(trig4, OUTPUT);

  pinMode(echo5, INPUT);
  pinMode(trig5, OUTPUT);

  pinMode(echo6, INPUT);
  pinMode(trig6, OUTPUT);

  delay(1000);
  Serial.println("loop start");
}


// setup() 함수가 호출된 이후, loop() 함수가 호출되며,
// 블록 안의 코드를 무한히 반복 실행됩니다.
void loop() {

  // 초음파 센서는 송신부와 수신부로 나뉘어 있으며,
  // 송신부터 수신까지의 시간을 기준으로 거리를 측정합니다.
  // 트리거로 연결된 핀이 송신부를 담당하며, 에코로 연결된 핀이 수신부를 담당합니다.
  // Trigger Signal Output
  digitalWrite(trig1, HIGH);// trigPin을 HIGH로 출력
  //20ms 동안 대기합니다.
  delay(set_delay);
  digitalWrite(trig1, LOW);// trigPin을 LOW로 출력
  // Echo Signal Input
  // 수신부의 초기 로직레벨을 HIGH로 설정하고, 반사된 초음파에 의하여 ROW 레벨로 바뀌기 전까지의 시간을 측정합니다.
  // 단위는 마이크로 초입니다.
  duration1 = pulseIn(echo1, HIGH);
  // 거리 계산
  /* echoPin 이 HIGH를 유지한 시간을 저장합니다.
            HIGH 였을 때 시간(초음파가 보냈다가 다시 들어온 시간)을
            가지고 거리를 계산합니다.
            */
  // 340은 초당 초음파(소리)의 속도, 10000은 밀리 세컨드를 세컨드로, 왕복 거리이므로 2로 나눠 줍니다.
  distance1 = ((float)(340 * duration1) / 10000 ) / 2;


  digitalWrite(trig2, HIGH);
  delay(set_delay);
  digitalWrite(trig2, LOW);
  duration2 = pulseIn(echo2, HIGH);
  distance2 = ((float)(340 * duration2) / 10000 ) / 2;


  digitalWrite(trig3, HIGH);
  delay(set_delay);
  digitalWrite(trig3, LOW);
  duration3 = pulseIn(echo3, HIGH);
  distance3 = ((float)(340 * duration3) / 10000 ) / 2;


  digitalWrite(trig4, HIGH);
  delay(set_delay);
  digitalWrite(trig4, LOW);
  duration4 = pulseIn(echo4, HIGH);
  distance4 = ((float)(340 * duration4) / 10000 ) / 2;


  digitalWrite(trig5, HIGH);
  delay(set_delay);
  digitalWrite(trig5, LOW);
  duration5 = pulseIn(echo5, HIGH);
  distance5 = ((float)(340 * duration5) / 10000 ) / 2;


  digitalWrite(trig6, HIGH);
  delay(set_delay);
  digitalWrite(trig6, LOW);
  duration6 = pulseIn(echo6, HIGH);
  distance6 = ((float)(340 * duration6) / 10000 ) / 2;


  distance[1] = distance1;
  distance[2] = distance2;
  distance[3] = distance3;
  distance[4] = distance4;
  distance[5] = distance5;
  distance[6] = distance6;


  for (int i = 1; i <= 6; i++) {
    //측정된 거리가 0cm 이상, 10cm 미만일 때 실행
    if (distance[i] >= 0 && distance[i] < 10) {
      count_1[i]++;
      count_2[i] = 0;
      if (count_1[i] >= 4) {
        count_1[i] = 0;
        char_distance[i] = 'o';
      }
    } else {
      count_1[i] = 0;
      count_2[i]++;
      if (count_2[i] >= 4) {
        count_2[i] = 0;
        char_distance[i] = 'x';
      }
    }
  }

  distance_print();

  String s = "/" + (String)char_distance[1] + (String)char_distance[2] + (String)char_distance[3] +
             (String)char_distance[4] + (String)char_distance[5] + (String)char_distance[6] + "/";

  blueToothSerial.print(s);

}


// 측정된 거리 값을 시리얼 모니터에 출력합니다.
void distance_print() {
  
  Serial.print(char_distance[1]);
  Serial.print(",");
  Serial.print(char_distance[2]);
  Serial.print(",");
  Serial.print(char_distance[3]);
  Serial.print(",");
  Serial.print(char_distance[4]);
  Serial.print(",");
  Serial.print(char_distance[5]);
  Serial.print(",");
  Serial.print(char_distance[6]);
  Serial.print(" >>>>>>>>>>      ");
  
  Serial.print(distance[1]);
  Serial.print("cm   ");
  Serial.print(distance[2]);
  Serial.print("cm   ");
  Serial.print(distance[3]);
  Serial.print("cm   ");
  Serial.print(distance[4]);
  Serial.print("cm   ");
  Serial.print(distance[5]);
  Serial.print("cm   ");
  Serial.print(distance[6]);
  Serial.println("cm   ");
}
