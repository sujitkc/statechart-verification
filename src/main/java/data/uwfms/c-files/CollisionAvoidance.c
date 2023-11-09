#include <klee/klee.h>
#include <stdio.h>
#include<stdbool.h>
#include<assert.h>

int CollisionAvoidance_Enabled_Speed;
int CollisionAvoidance_Warning;
bool CollisionAvoidance_BoolTRUE;
int CollisionAvoidance_AccelPedal;
int CollisionAvoidance_CA_Warning;
int CollisionAvoidance_Enabled_BreakPedal;
int CollisionAvoidance_CA_HVI;
int CollisionAvoidance_Enabled_Engaged_Set_Brake;
int CollisionAvoidance_Enabled_PRNDL_In;
int CollisionAvoidance_CA_Enabled;
int CollisionAvoidance_Enabled_Engaged_ThreatCA;
int state;
int event;
int e;
int Error;
int CollisionAvoidance_Disabled;
int CollisionAvoidance_Fail;
int CollisionAvoidance_Override;
int CollisionAvoidance_Enabled_Halt;
int CollisionAvoidance_Enabled_Engaged_Idle;
int CollisionAvoidance_Enabled_Engaged_Warn;
int CollisionAvoidance_Enabled_Disengaged;
int CollisionAvoidance_Enabled_Engaged_Avoid;
int CollisionAvoidance_Enabled_Engaged_Mitigate;
void makeSymbolic(){
klee_make_symbolic(&CollisionAvoidance_Enabled_Speed, sizeof CollisionAvoidance_Enabled_Speed,"CollisionAvoidance_Enabled_Speed");
klee_make_symbolic(&CollisionAvoidance_Enabled_PRNDL_In, sizeof CollisionAvoidance_Enabled_PRNDL_In, "CollisionAvoidance_Enabled_PRNDL_In");
klee_make_symbolic(&CollisionAvoidance_Enabled_Engaged_ThreatCA, sizeof CollisionAvoidance_Enabled_Engaged_ThreatCA, "CollisionAvoidance_Enabled_Engaged_ThreatCA");
klee_make_symbolic(&event, sizeof event,"event");
klee_make_symbolic(&CollisionAvoidance_AccelPedal, sizeof CollisionAvoidance_AccelPedal,"CollisionAvoidance_AccelPedal");
klee_make_symbolic(&CollisionAvoidance_CA_Enabled, sizeof CollisionAvoidance_CA_Enabled, "CollisionAvoidance_CA_Enabled");
klee_make_symbolic(&CollisionAvoidance_Enabled_BreakPedal, sizeof CollisionAvoidance_Enabled_BreakPedal, "CollisionAvoidance_Enabled_BreakPedal");
}
void run(int * events, int N){
e = 0;
Error = 1;
CollisionAvoidance_Disabled = 0;
CollisionAvoidance_Fail = 1;
CollisionAvoidance_Override = 2;
CollisionAvoidance_Enabled_Halt = 3;
CollisionAvoidance_Enabled_Engaged_Idle = 4;
CollisionAvoidance_Enabled_Engaged_Warn = 5;
CollisionAvoidance_Enabled_Disengaged = 6;
CollisionAvoidance_Enabled_Engaged_Avoid = 7;
CollisionAvoidance_Enabled_Engaged_Mitigate = 8;
state = CollisionAvoidance_Disabled;
for (int i = 0; i < N; i++) {
//event = events[i];
if ((state==CollisionAvoidance_Enabled_Engaged_Mitigate)) {

if (((event==Error)&&true)) {
state = CollisionAvoidance_Fail;
CollisionAvoidance_CA_HVI = 3;
CollisionAvoidance_Warning = 0;
//assert(false);--reached
}
else {
 //   assert(false);--reached

}
makeSymbolic();
if (((event==e)&&(CollisionAvoidance_Enabled_Speed==1))) {
state = CollisionAvoidance_Enabled_Halt;
CollisionAvoidance_CA_HVI = 1;
CollisionAvoidance_Warning = 4;
//ssert(false); --reached

}
else {
   // assert(false);--reached
}
if (((event==e)&&(((CollisionAvoidance_Enabled_Speed>0)&&(CollisionAvoidance_Enabled_Speed<=25))||(CollisionAvoidance_Enabled_PRNDL_In!=3)))) {
state = CollisionAvoidance_Enabled_Disengaged;
CollisionAvoidance_CA_HVI = 1;
CollisionAvoidance_Warning = 0;
//assert(false); --reached

}
else {
//assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_AccelPedal>=35))) {
state = CollisionAvoidance_Override;
CollisionAvoidance_CA_HVI = 4;
CollisionAvoidance_Warning = 0;
//assert(false); --reached

}
else {
//assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==2))) {
state = CollisionAvoidance_Enabled_Engaged_Avoid;
CollisionAvoidance_Warning = 2;
CollisionAvoidance_Enabled_Engaged_Set_Brake = 30;
//assert(false); --reached
}
else {
}
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==1))) {
state = CollisionAvoidance_Enabled_Engaged_Warn;
CollisionAvoidance_Warning = 1;
//assert(false); --reached

}
else {
//assert(false);-- reached

}
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==0))) {
state = CollisionAvoidance_Enabled_Engaged_Idle;
CollisionAvoidance_Warning = 0;
//assert(false); --reached

}
else {
//assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_CA_Enabled!=1))) {
state = CollisionAvoidance_Disabled;
CollisionAvoidance_CA_HVI = 0;
CollisionAvoidance_Warning = 0;
//assert(false); --reached

}
else {
//assert(false);--reached

}
}
else {
if ((state==CollisionAvoidance_Enabled_Engaged_Avoid)) {
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==1))) {
state = CollisionAvoidance_Enabled_Engaged_Warn;
CollisionAvoidance_Warning = 1;
//assert(false); --reached

}
else {
  //assert(false);--reached
}
if (((event==e)&&(((CollisionAvoidance_Enabled_Speed>0)&&(CollisionAvoidance_Enabled_Speed<=25))||(CollisionAvoidance_Enabled_PRNDL_In!=3)))) {
state = CollisionAvoidance_Enabled_Disengaged;
CollisionAvoidance_CA_HVI = 1;
CollisionAvoidance_Warning = 0;
//assert(false); --reached

}
else {
  //  assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==3))) {
state = CollisionAvoidance_Enabled_Engaged_Mitigate;
 // assert(false); --reached
CollisionAvoidance_Warning = 3;
CollisionAvoidance_Enabled_Engaged_Set_Brake = 80;
}
else {
   // assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_Enabled_Speed==1))) {
state = CollisionAvoidance_Enabled_Halt;
CollisionAvoidance_CA_HVI = 1;
CollisionAvoidance_Warning = 4;
//assert(false); --reached

}
else {
 // assert(false);--reached

}
if (((event==Error)&&true)) {
state = CollisionAvoidance_Fail;
CollisionAvoidance_CA_HVI = 3;
CollisionAvoidance_Warning = 0;
//assert(false);--reached

}
else {
//assert(false);--reached

}
makeSymbolic();
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==0))) {
state = CollisionAvoidance_Enabled_Engaged_Idle;
CollisionAvoidance_Warning = 0;
//assert(false); --reached

}
else {
//assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_CA_Enabled!=1))) {
state = CollisionAvoidance_Disabled;
CollisionAvoidance_CA_HVI = 0;
CollisionAvoidance_Warning = 0;
    //assert(false); --reached

}
else {
   // assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_AccelPedal>=35))) {
state = CollisionAvoidance_Override;
CollisionAvoidance_CA_HVI = 4;
CollisionAvoidance_Warning = 0;
    //assert(false); --reached

}
else {
    //assert(false);--reached

}
}
else {
if ((state==CollisionAvoidance_Enabled_Disengaged)) {
//assert(false); --reached

if (((event==e)&&(CollisionAvoidance_CA_Enabled!=1))) {
state = CollisionAvoidance_Disabled;

CollisionAvoidance_CA_HVI = 0;
CollisionAvoidance_Warning = 0;
    //  assert(false);--reached

}
else {
   // assert(false);--reached

}
if (((event==e)&&((CollisionAvoidance_Enabled_Speed>25)&&(CollisionAvoidance_Enabled_PRNDL_In==3)))) {
state = CollisionAvoidance_Enabled_Engaged_Idle;
CollisionAvoidance_CA_HVI = 2;
   // assert(false);--reached

}
else {
   // assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_AccelPedal>=35))) {
state = CollisionAvoidance_Override;
CollisionAvoidance_CA_HVI = 4;
CollisionAvoidance_Warning = 0;
     // assert(false);--reached

}
else {
   //   assert(false);--reached

}
if (((event==Error)&&true)) {
state = CollisionAvoidance_Fail;
CollisionAvoidance_CA_HVI = 3;
CollisionAvoidance_Warning = 0;
 //   assert(false);--reached

}
else {
  //  assert(false);--reached

}
}
else {
if ((state==CollisionAvoidance_Enabled_Engaged_Warn)) {
if (((event==e)&&(CollisionAvoidance_CA_Enabled!=1))) {
state = CollisionAvoidance_Disabled;
CollisionAvoidance_CA_HVI = 0;
CollisionAvoidance_Warning = 0;
  //  assert(false);--reached

}
else {
//    assert(false);--reached

}
if (((event==Error)&&true)) {
state = CollisionAvoidance_Fail;
CollisionAvoidance_CA_HVI = 3;
CollisionAvoidance_Warning = 0;
 //   assert(false);--reached

}
else {
   // assert(false);-reached

}
makeSymbolic();
if (((event==e)&&(CollisionAvoidance_Enabled_Speed==1))) {
state = CollisionAvoidance_Enabled_Halt;
CollisionAvoidance_CA_HVI = 1;
CollisionAvoidance_Warning = 4;
   // assert(false);--reached

}
else {
 //   assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==0))) {
state = CollisionAvoidance_Enabled_Engaged_Idle;
CollisionAvoidance_Warning = 0;
  //  assert(false);--reached

}
else {
    //assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==3))) {
state = CollisionAvoidance_Enabled_Engaged_Mitigate;
CollisionAvoidance_Warning = 3;
CollisionAvoidance_Enabled_Engaged_Set_Brake = 80;
//    assert(false); -reached

}
else {
 //   assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_AccelPedal>=35))) {
state = CollisionAvoidance_Override;
CollisionAvoidance_CA_HVI = 4;
CollisionAvoidance_Warning = 0;
   // assert(false); --reached

}
else {
   // assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==2))) {
state = CollisionAvoidance_Enabled_Engaged_Avoid;
CollisionAvoidance_Warning = 2;
CollisionAvoidance_Enabled_Engaged_Set_Brake = 30;
 //   assert(false);--reached

}
else {
    //assert(false);--reached

}
if (((event==e)&&(((CollisionAvoidance_Enabled_Speed>0)&&(CollisionAvoidance_Enabled_Speed<=25))||(CollisionAvoidance_Enabled_PRNDL_In!=3)))) {
state = CollisionAvoidance_Enabled_Disengaged;
CollisionAvoidance_CA_HVI = 1;
CollisionAvoidance_Warning = 0;
   // assert(false);--reached

}
else {
    //assert(false);--reached

}
}
else {
if ((state==CollisionAvoidance_Enabled_Engaged_Idle)) {

if (((event==Error)&&true)) {
state = CollisionAvoidance_Fail;
CollisionAvoidance_CA_HVI = 3;
CollisionAvoidance_Warning = 0;
//assert(false);--reached
}
else {
}
makeSymbolic();
if (((event==e)&&(CollisionAvoidance_CA_Enabled!=1))) {
state = CollisionAvoidance_Disabled;
//assert(false); --reached
CollisionAvoidance_CA_HVI = 0;
CollisionAvoidance_Warning = 0;

}
else {
}
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==3))) {
state = CollisionAvoidance_Enabled_Engaged_Mitigate;
 // assert(false);--reached
CollisionAvoidance_Warning = 3;
CollisionAvoidance_Enabled_Engaged_Set_Brake = 80;
}
else {
}
if (((event==e)&&(CollisionAvoidance_AccelPedal>=35))) {
state = CollisionAvoidance_Override;
CollisionAvoidance_CA_HVI = 4;
CollisionAvoidance_Warning = 0;
  //assert(false); --reached
}
else {
  //  assert(false);--reached

}
if (((event==e)&&(((CollisionAvoidance_Enabled_Speed>0)&&(CollisionAvoidance_Enabled_Speed<=25))||(CollisionAvoidance_Enabled_PRNDL_In!=3)))) {
state = CollisionAvoidance_Enabled_Disengaged;
CollisionAvoidance_CA_HVI = 1;
CollisionAvoidance_Warning = 0;
 //   assert(false); --reached

}
else {
   // assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==1))) {
 // assert(false); --reached
state = CollisionAvoidance_Enabled_Engaged_Warn;
CollisionAvoidance_Warning = 1;

}
else {
   //   assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_Enabled_Speed==1))) {
state = CollisionAvoidance_Enabled_Halt;
CollisionAvoidance_CA_HVI = 1;
CollisionAvoidance_Warning = 4;
     // assert(false); --reached

}
else {
    //  assert(false);--reached

}
if (((event==e)&&(CollisionAvoidance_Enabled_Engaged_ThreatCA==2))) {
state = CollisionAvoidance_Enabled_Engaged_Avoid;
CollisionAvoidance_Warning = 2;
CollisionAvoidance_Enabled_Engaged_Set_Brake = 30;
 // assert(false);--reached
}
else {
   // assert(false);--reached

}
}
else {
if ((state==CollisionAvoidance_Enabled_Halt)) {
  //  assert(false);--reached

if (((event==e)&&(CollisionAvoidance_Enabled_BreakPedal>10))) {
state = CollisionAvoidance_Enabled_Disengaged;
CollisionAvoidance_CA_HVI = 1;
CollisionAvoidance_Warning = 0;
    //assert(false); --reached

}
else {
   //assert(false); --reached

}
if (((event==e)&&(CollisionAvoidance_CA_Enabled!=1))) {
state = CollisionAvoidance_Disabled;
CollisionAvoidance_CA_HVI = 0;
CollisionAvoidance_Warning = 0;
  //  assert(false);--reached

}
else {
}
if (((event==Error)&&true)) {
state = CollisionAvoidance_Fail;
CollisionAvoidance_CA_HVI = 3;
CollisionAvoidance_Warning = 0;
   // assert(false); --reached

}
else {
}
if (((event==e)&&(CollisionAvoidance_AccelPedal>=35))) {
state = CollisionAvoidance_Override;
CollisionAvoidance_CA_HVI = 4;
CollisionAvoidance_Warning = 0;
  //assert(false); --reached
}
else {
  //assert(false); --reached
}
}
else {
if ((state==CollisionAvoidance_Override)) {
if (((event==Error)&&true)) {
state = CollisionAvoidance_Fail;
CollisionAvoidance_CA_HVI = 3;
  //assert(false);--reached
}
else {
  //assert(false);--reached
}
if (((event==e)&&(CollisionAvoidance_CA_Enabled!=1))) {
state = CollisionAvoidance_Disabled;
CollisionAvoidance_CA_HVI = 0;
CollisionAvoidance_Warning = 0;
 // assert(false); -- reached
}
else {
  //assert(false);--reached
}
makeSymbolic();

if (((event==e)&&(CollisionAvoidance_AccelPedal<35))) {
state = CollisionAvoidance_Enabled_Disengaged;
CollisionAvoidance_CA_HVI = 1;
  //assert(false);--reached
}
else {
  //assert(false);--reached
}
}
else {
if ((state==CollisionAvoidance_Fail)) {
  //assert(false);--reached
}
else {
if ((state==CollisionAvoidance_Disabled)) {
  //assert(false);-- reached
if (((event==e)&&(CollisionAvoidance_CA_Enabled==1))) {
state = CollisionAvoidance_Enabled_Disengaged;
  CollisionAvoidance_CA_HVI = 1;
  //assert(false);--reached
}
else {
  //assert(false);--reached
}
}
else {
  assert(false);
}
}
}
}
}
}
}
}
}
}
}

int main () {
int N = 11; int events[N];
klee_make_symbolic(events, sizeof events, "events");
//klee_assume((CollisionAvoidance_Enabled_PRNDL_In=1)|(CollisionAvoidance_Enabled_PRNDL_In=2)|(CollisionAvoidance_Enabled_PRNDL_In=3));
//  klee_assume((CollisionAvoidance_Enabled_Engaged_ThreatCA=1)|(CollisionAvoidance_Enabled_Engaged_ThreatCA=2)|(CollisionAvoidance_Enabled_Engaged_ThreatCA=3)|(CollisionAvoidance_Enabled_Engaged_ThreatCA=0));
//klee_make_symbolic(&state, sizeof state,"state");
//klee_make_symbolic(&CollisionAvoidance_CA_Enabled, sizeof CollisionAvoidance_CA_Enabled,"CollisionAvoidance_CA_Enabled");
//klee_assume((CollisionAvoidance_CA_Enabled==0));
makeSymbolic();
run(events, N);
return 0;
}
