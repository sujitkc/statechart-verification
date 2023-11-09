#include <klee/klee.h>
#include <stdio.h>
#include<stdbool.h>
#include<assert.h>


void non_det (bool b) {
	assert(b);
}

void stuck_spec (bool b) {
	assert(b);
}
int ParkAssist_minusone;
int ParkAssist_Speed;
int ParkAssist_Enabled_Engaged_set_Brake;
int ParkAssist_Enabled_set_Throttle;
int ParkAssist_Enabled_SteerIn;
int ParkAssist_PA_Enabled;
int ParkAssist_Enabled_set_SteerOut;
int ParkAssist_PA_HVI;
int ParkAssist_Enabled_SpaceFound;
int ParkAssist_Enabled_AccelPedal;
int ParkAssist_Enabled_Accepted;
int ParkAssist_Enabled_PRNDL_In;
int state;
int event;
int e;
int Next;
int No_event;
int Error;
int ParkAssist_Fail;
int ParkAssist_Enabled_Engaged_Stop2;
int ParkAssist_Enabled_Searching;
int ParkAssist_Enabled_Idle;
int ParkAssist_Disabled;
int ParkAssist_Enabled_Engaged_Stop1;
int ParkAssist_Enabled_Override;
int ParkAssist_Enabled_Abort;
int ParkAssist_Enabled_Engaged_SwivelIn;
int ParkAssist_Enabled_Prompting;
int ParkAssist_Enabled_Engaged_SwivelOut;
int ParkAssist_Enabled_Engaged_Center;

void makeSymbolic(){


klee_make_symbolic(&ParkAssist_Enabled_Engaged_set_Brake, sizeof ParkAssist_Enabled_Engaged_set_Brake,"ParkAssist_Enabled_Engaged_set_Brake");
//klee_make_symbolic(&ParkAssist_Enabled_Prompting, sizeof ParkAssist_Enabled_Prompting,"ParkAssist_Enabled_Prompting");
//klee_make_symbolic(&ParkAssist_Enabled_Abort, sizeof ParkAssist_Enabled_Abort,"ParkAssist_Enabled_Abort");
//klee_make_symbolic(&ParkAssist_Enabled_SpaceFound, sizeof ParkAssist_Enabled_SpaceFound,"ParkAssist_Enabled_SpaceFound");
//klee_make_symbolic(&ParkAssist_Disabled, sizeof ParkAssist_Disabled,"ParkAssist_Disabled");
klee_make_symbolic(&ParkAssist_Enabled_set_SteerOut, sizeof ParkAssist_Enabled_set_SteerOut,"ParkAssist_Enabled_set_SteerOut");
klee_make_symbolic(&ParkAssist_Enabled_set_Throttle, sizeof ParkAssist_Enabled_set_Throttle,"ParkAssist_Enabled_set_Throttle");
//klee_make_symbolic(&state, sizeof state,"state");
klee_make_symbolic(&event, sizeof event,"event");
//klee_make_symbolic(&ParkAssist_Enabled_Engaged_SwivelIn, sizeof ParkAssist_Enabled_Engaged_SwivelIn,"ParkAssist_Enabled_Engaged_SwivelIn");
//klee_make_symbolic(&ParkAssist_Enabled_SteerIn, sizeof ParkAssist_Enabled_SteerIn,"ParkAssist_Enabled_SteerIn");
//klee_make_symbolic(&ParkAssist_Enabled_Engaged_Center, sizeof ParkAssist_Enabled_Engaged_Center,"ParkAssist_Enabled_Engaged_Center");
klee_make_symbolic(&ParkAssist_Enabled_AccelPedal, sizeof ParkAssist_Enabled_AccelPedal,"ParkAssist_Enabled_AccelPedal");
//klee_make_symbolic(&e, sizeof e,"e");
//klee_make_symbolic(&ParkAssist_Enabled_Engaged_SwivelOut, sizeof ParkAssist_Enabled_Engaged_SwivelOut,"ParkAssist_Enabled_Engaged_SwivelOut");
//klee_make_symbolic(&ParkAssist_minusone, sizeof ParkAssist_minusone,"ParkAssist_minusone");
//klee_make_symbolic(&Error, sizeof Error,"Error");
//klee_make_symbolic(&ParkAssist_Enabled_Engaged_Stop1, sizeof ParkAssist_Enabled_Engaged_Stop1,"ParkAssist_Enabled_Engaged_Stop1");
//klee_make_symbolic(&ParkAssist_Enabled_Accepted, sizeof ParkAssist_Enabled_Accepted,"ParkAssist_Enabled_Accepted");
//klee_make_symbolic(&ParkAssist_Enabled_Engaged_Stop2, sizeof ParkAssist_Enabled_Engaged_Stop2,"ParkAssist_Enabled_Engaged_Stop2");
//klee_make_symbolic(&No_event, sizeof No_event,"No_event");
//klee_make_symbolic(&ParkAssist_Enabled_Idle, sizeof ParkAssist_Enabled_Idle,"ParkAssist_Enabled_Idle");
klee_make_symbolic(&ParkAssist_Enabled_PRNDL_In, sizeof ParkAssist_Enabled_PRNDL_In,"ParkAssist_Enabled_PRNDL_In");
klee_make_symbolic(&ParkAssist_PA_Enabled, sizeof ParkAssist_PA_Enabled,"ParkAssist_PA_Enabled");
klee_make_symbolic(&ParkAssist_PA_HVI, sizeof ParkAssist_PA_HVI,"ParkAssist_PA_HVI");
//klee_make_symbolic(&ParkAssist_Enabled_Override, sizeof ParkAssist_Enabled_Override,"ParkAssist_Enabled_Override");
//ee_make_symbolic(&Next, sizeof Next,"Next");
klee_make_symbolic(&ParkAssist_Speed, sizeof ParkAssist_Speed,"ParkAssist_Speed");
//klee_make_symbolic(&ParkAssist_Enabled_Searching, sizeof ParkAssist_Enabled_Searching,"ParkAssist_Enabled_Searching");
//klee_make_symbolic(&ParkAssist_Fail, sizeof ParkAssist_Fail,"ParkAssist_Fail");



}
void run(int * events, int N){
e = 0;
Next = 1;
No_event = 2;
Error = 3;
ParkAssist_Fail = 0;
ParkAssist_Enabled_Engaged_Stop2 = 1;
ParkAssist_Enabled_Searching = 2;
ParkAssist_Enabled_Idle = 3;
ParkAssist_Disabled = 4;
ParkAssist_Enabled_Engaged_Stop1 = 5;
ParkAssist_Enabled_Override = 6;
ParkAssist_Enabled_Abort = 7;
ParkAssist_Enabled_Engaged_SwivelIn = 8;
ParkAssist_Enabled_Prompting = 9;
ParkAssist_Enabled_Engaged_SwivelOut = 10;
ParkAssist_Enabled_Engaged_Center = 11;
state = ParkAssist_Fail;
for (int i = 0; i < N; i++) {
event = events[i];
if ((state==ParkAssist_Enabled_Engaged_Center)) {
            stuck_spec(!
(
(((false||((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))||(ParkAssist_Enabled_PRNDL_In!=3))||((ParkAssist_Enabled_PRNDL_In==3)&&((ParkAssist_Speed>0)&&(ParkAssist_Speed<=5)))))
);
            non_det(((((false+((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))+(ParkAssist_Enabled_PRNDL_In!=3))+((ParkAssist_Enabled_PRNDL_In==3)&&((ParkAssist_Speed>0)&&(ParkAssist_Speed<=5))))>1));
if (((event==e)&&((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))) {
state = ParkAssist_Enabled_Abort;
ParkAssist_PA_HVI = 6;
}
else {
}
if (((event==e)&&(ParkAssist_Enabled_PRNDL_In!=3))) {
state = ParkAssist_Enabled_Abort;
ParkAssist_PA_HVI = 6;
}
else {
}
if (((event==Next)&&((ParkAssist_Enabled_PRNDL_In==3)&&((ParkAssist_Speed>0)&&(ParkAssist_Speed<=5))))) {
state = ParkAssist_Enabled_Engaged_Stop2;
ParkAssist_PA_HVI = 4;
ParkAssist_Enabled_Engaged_set_Brake = 30;
}
else {
}
}
else {
if ((state==ParkAssist_Enabled_Engaged_SwivelOut)) {
                stuck_spec(!
(
((false||((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))||(ParkAssist_Enabled_PRNDL_In!=1)))
);
                non_det((((false+((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))+(ParkAssist_Enabled_PRNDL_In!=1))>1));
if (((event==e)&&((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))) {
state = ParkAssist_Enabled_Abort;
ParkAssist_PA_HVI = 6;
}
else {
}
if (((event==e)&&(ParkAssist_Enabled_PRNDL_In!=1))) {
state = ParkAssist_Enabled_Abort;
ParkAssist_PA_HVI = 6;
}
else {
}
}
else {
if ((state==ParkAssist_Enabled_Prompting)) {
                    stuck_spec(!
(
((false||((ParkAssist_Enabled_SpaceFound==0)||(ParkAssist_Enabled_Accepted==0)))||((ParkAssist_Enabled_Accepted==1)&&((ParkAssist_Speed==0)&&(ParkAssist_Enabled_PRNDL_In==1)))))
);
                    non_det((((false+((ParkAssist_Enabled_SpaceFound==0)||(ParkAssist_Enabled_Accepted==0)))+((ParkAssist_Enabled_Accepted==1)&&((ParkAssist_Speed==0)&&(ParkAssist_Enabled_PRNDL_In==1))))>1));
if (((event==e)&&((ParkAssist_Enabled_SpaceFound==0)||(ParkAssist_Enabled_Accepted==0)))) {
state = ParkAssist_Enabled_Searching;
ParkAssist_PA_HVI = 2;
}
else {
}
if (((event==e)&&((ParkAssist_Enabled_Accepted==1)&&((ParkAssist_Speed==0)&&(ParkAssist_Enabled_PRNDL_In==1))))) {
state = ParkAssist_Enabled_Engaged_SwivelOut;
ParkAssist_PA_HVI = 4;
ParkAssist_Enabled_set_Throttle = 20;
ParkAssist_Enabled_set_SteerOut = 1;
}
else {
}
}
else {
if ((state==ParkAssist_Enabled_Engaged_SwivelIn)) {
                        stuck_spec(!
(
((((false||((ParkAssist_Enabled_PRNDL_In==1)&&((ParkAssist_Speed>0)&&(ParkAssist_Speed<=5))))||((ParkAssist_Enabled_PRNDL_In==1)&&((ParkAssist_Speed>0)&&(ParkAssist_Speed<=5))))||((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))||(ParkAssist_Enabled_PRNDL_In!=1)))
);
                        non_det((((((false+((ParkAssist_Enabled_PRNDL_In==1)&&((ParkAssist_Speed>0)&&(ParkAssist_Speed<=5))))+((ParkAssist_Enabled_PRNDL_In==1)&&((ParkAssist_Speed>0)&&(ParkAssist_Speed<=5))))+((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))+(ParkAssist_Enabled_PRNDL_In!=1))>1));
if (((event==Next)&&((ParkAssist_Enabled_PRNDL_In==1)&&((ParkAssist_Speed>0)&&(ParkAssist_Speed<=5))))) {
state = ParkAssist_Enabled_Engaged_SwivelOut;
ParkAssist_PA_HVI = 4;
ParkAssist_Enabled_set_Throttle = 20;
ParkAssist_Enabled_set_SteerOut = ParkAssist_minusone;
}
else {
}
if (((event==Next)&&((ParkAssist_Enabled_PRNDL_In==1)&&((ParkAssist_Speed>0)&&(ParkAssist_Speed<=5))))) {
state = ParkAssist_Enabled_Engaged_Stop1;
ParkAssist_PA_HVI = 4;
ParkAssist_Enabled_Engaged_set_Brake = 30;
}
else {
}
if (((event==e)&&((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))) {
state = ParkAssist_Enabled_Abort;
ParkAssist_PA_HVI = 6;
}
else {
}
if (((event==e)&&(ParkAssist_Enabled_PRNDL_In!=1))) {
state = ParkAssist_Enabled_Abort;
ParkAssist_PA_HVI = 6;
}
else {
}
}
else {
if ((state==ParkAssist_Enabled_Abort)) {
}
else {
if ((state==ParkAssist_Enabled_Override)) {
                                stuck_spec(!
(
(false||((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0))))
);
if (((event==e)&&((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))) {
state = ParkAssist_Enabled_Abort;
ParkAssist_PA_HVI = 6;
}
else {
}
}
else {
if ((state==ParkAssist_Enabled_Engaged_Stop1)) {
                                    stuck_spec(!
(
((false||(ParkAssist_Speed==0))||((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0))))
);
                                    non_det((((false+(ParkAssist_Speed==0))+((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))>1));
if (((event==Next)&&(ParkAssist_Speed==0))) {
state = ParkAssist_Enabled_Engaged_Center;
ParkAssist_PA_HVI = 4;
ParkAssist_Enabled_set_Throttle = 20;
}
else {
}
if (((event==e)&&((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))) {
state = ParkAssist_Enabled_Abort;
ParkAssist_PA_HVI = 6;
}
else {
}
}
else {
if ((state==ParkAssist_Disabled)) {
                                        stuck_spec(!
(
(((false||(ParkAssist_PA_Enabled==1))||(ParkAssist_PA_Enabled==0))||true))
);
                                        non_det(((((false+(ParkAssist_PA_Enabled==1))+(ParkAssist_PA_Enabled==0))+true)>1));
if (((event==e)&&(ParkAssist_PA_Enabled==1))) {
state = ParkAssist_Enabled_Idle;
ParkAssist_PA_HVI = 1;
}
else {
}
if (((event==e)&&(ParkAssist_PA_Enabled==0))) {
state = ParkAssist_Enabled_Idle;
ParkAssist_PA_HVI = 0;
}
else {
}
if (((event==Error)&&true)) {
state = ParkAssist_Enabled_Idle;
ParkAssist_PA_HVI = 8;
}
else {
}
}
else {
if ((state==ParkAssist_Enabled_Idle)) {
                                            stuck_spec(!
(
(false||((ParkAssist_Speed>0)&&((ParkAssist_Speed<=10)&&(ParkAssist_Enabled_PRNDL_In==3)))))
);
if (((event==e)&&((ParkAssist_Speed>0)&&((ParkAssist_Speed<=10)&&(ParkAssist_Enabled_PRNDL_In==3))))) {
state = ParkAssist_Enabled_Searching;
ParkAssist_PA_HVI = 2;
}
else {
}
}
else {
if ((state==ParkAssist_Enabled_Searching)) {
                                                stuck_spec(!
(
((false||((ParkAssist_Speed==0)||((ParkAssist_Speed>0)||(ParkAssist_Enabled_PRNDL_In!=3))))||(ParkAssist_Enabled_SpaceFound==1)))
);
                                                non_det((((false+((ParkAssist_Speed==0)||((ParkAssist_Speed>0)||(ParkAssist_Enabled_PRNDL_In!=3))))+(ParkAssist_Enabled_SpaceFound==1))>1));
if (((event==e)&&((ParkAssist_Speed==0)||((ParkAssist_Speed>0)||(ParkAssist_Enabled_PRNDL_In!=3))))) {
state = ParkAssist_Enabled_Idle;
ParkAssist_PA_HVI = 1;
}
else {
}
if (((event==e)&&(ParkAssist_Enabled_SpaceFound==1))) {
state = ParkAssist_Enabled_Prompting;
ParkAssist_PA_HVI = 3;
}
else {
}
}
else {
if ((state==ParkAssist_Enabled_Engaged_Stop2)) {
                                                    stuck_spec(!
(
((false||(ParkAssist_Speed==0))||((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0))))
);
                                                    non_det((((false+(ParkAssist_Speed==0))+((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))>1));
if (((event==e)&&(ParkAssist_Speed==0))) {
state = ParkAssist_Disabled;
ParkAssist_PA_HVI = 5;
}
else {
}
if (((event==e)&&((ParkAssist_Enabled_SteerIn>0)||(ParkAssist_Enabled_AccelPedal>0)))) {
state = ParkAssist_Enabled_Abort;
ParkAssist_PA_HVI = 6;
}
else {
}
}
else {
if ((state==ParkAssist_Fail)) {
}
else {
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
}
}
}
int main () {
int N = 5; int events[N];
klee_make_symbolic(events, sizeof events, "events");

run(events, N);
return 0;
}
