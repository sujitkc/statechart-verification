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
int LaneGuide_LG_Enabled;
int LaneGuide_minusone;
int LaneGuide_Enabled_Engaged_LaneDrift;
int LaneGuide_Enabled_BrakePedal;
int LaneGuide_Enabled_Engaged_LGMode;
int LaneGuide_LG_Warning;
int LaneGuide_Enabled_TurnSignal;
int LaneGuide_minusten;
int LaneGuide_Enabled_SteerIn;
int LaneGuide_Enabled_Engaged_set_SteerOut;
int LaneGuide_Enabled_Speed;
int LaneGuide_Enabled_PRNDL_In;
int state;
int event;
int e;
int No_event;
int Error;
int LaneGuide_Fail;
int LaneGuide_Enabled_Engaged_WarnLeft;
int LaneGuide_Enabled_Override;
int LaneGuide_Enabled_Disengaged;
int LaneGuide_Enabled_Engaged_AssistLeft;
int LaneGuide_Disabled;
int LaneGuide_Enabled_Engaged_WarnRight;
int LaneGuide_Enabled_Engaged_Idle;
int LaneGuide_Enabled_Engaged_AssistRight;

void makeSymbolic(){


klee_make_symbolic(&LaneGuide_Enabled_PRNDL_In, sizeof LaneGuide_Enabled_PRNDL_In,"LaneGuide_Enabled_PRNDL_In");
//klee_make_symbolic(&LaneGuide_Enabled_Engaged_AssistRight, sizeof LaneGuide_Enabled_Engaged_AssistRight,"LaneGuide_Enabled_Engaged_AssistRight");
klee_make_symbolic(&LaneGuide_Enabled_Speed, sizeof LaneGuide_Enabled_Speed,"LaneGuide_Enabled_Speed");
//klee_make_symbolic(&LaneGuide_Enabled_Engaged_WarnLeft, sizeof LaneGuide_Enabled_Engaged_WarnLeft,"LaneGuide_Enabled_Engaged_WarnLeft");
//klee_make_symbolic(&LaneGuide_Enabled_Override, sizeof LaneGuide_Enabled_Override,"LaneGuide_Enabled_Override");
//klee_make_symbolic(&LaneGuide_Disabled, sizeof LaneGuide_Disabled,"LaneGuide_Disabled");
//klee_make_symbolic(&LaneGuide_minusone, sizeof LaneGuide_minusone,"LaneGuide_minusone");
klee_make_symbolic(&LaneGuide_Enabled_TurnSignal, sizeof LaneGuide_Enabled_TurnSignal,"LaneGuide_Enabled_TurnSignal");
klee_make_symbolic(&LaneGuide_Enabled_Engaged_LGMode, sizeof LaneGuide_Enabled_Engaged_LGMode,"LaneGuide_Enabled_Engaged_LGMode");
//klee_make_symbolic(&LaneGuide_Enabled_Disengaged, sizeof LaneGuide_Enabled_Disengaged,"LaneGuide_Enabled_Disengaged");
//klee_make_symbolic(&LaneGuide_Enabled_Engaged_Idle, sizeof LaneGuide_Enabled_Engaged_Idle,"LaneGuide_Enabled_Engaged_Idle");
//klee_make_symbolic(&LaneGuide_Enabled_Engaged_LaneDrift, sizeof LaneGuide_Enabled_Engaged_LaneDrift,"LaneGuide_Enabled_Engaged_LaneDrift");
//klee_make_symbolic(&LaneGuide_Enabled_Engaged_WarnRight, sizeof LaneGuide_Enabled_Engaged_WarnRight,"LaneGuide_Enabled_Engaged_WarnRight");
//klee_make_symbolic(&state, sizeof state,"state");
klee_make_symbolic(&event, sizeof event,"event");
//klee_make_symbolic(&e, sizeof e,"e");
klee_make_symbolic(&LaneGuide_Enabled_BrakePedal, sizeof LaneGuide_Enabled_BrakePedal,"LaneGuide_Enabled_BrakePedal");
//klee_make_symbolic(&LaneGuide_Enabled_Engaged_AssistLeft, sizeof LaneGuide_Enabled_Engaged_AssistLeft,"LaneGuide_Enabled_Engaged_AssistLeft");
klee_make_symbolic(&LaneGuide_LG_Enabled, sizeof LaneGuide_LG_Enabled,"LaneGuide_LG_Enabled");
//klee_make_symbolic(&Error, sizeof Error,"Error");
//klee_make_symbolic(&No_event, sizeof No_event,"No_event");
//klee_make_symbolic(&LaneGuide_minusten, sizeof LaneGuide_minusten,"LaneGuide_minusten");
klee_make_symbolic(&LaneGuide_Enabled_Engaged_set_SteerOut, sizeof LaneGuide_Enabled_Engaged_set_SteerOut,"LaneGuide_Enabled_Engaged_set_SteerOut");
klee_make_symbolic(&LaneGuide_LG_Warning, sizeof LaneGuide_LG_Warning,"LaneGuide_LG_Warning");
klee_make_symbolic(&LaneGuide_Enabled_SteerIn, sizeof LaneGuide_Enabled_SteerIn,"LaneGuide_Enabled_SteerIn");
//klee_make_symbolic(&LaneGuide_Fail, sizeof LaneGuide_Fail,"LaneGuide_Fail");



}

void run(int * events, int N){
e = 0;
No_event = 1;
Error = 2;
LaneGuide_Fail = 0;
LaneGuide_Enabled_Engaged_WarnLeft = 1;
LaneGuide_Enabled_Override = 2;
LaneGuide_Enabled_Disengaged = 3;
LaneGuide_Enabled_Engaged_AssistLeft = 4;
LaneGuide_Disabled = 5;
LaneGuide_Enabled_Engaged_WarnRight = 6;
LaneGuide_Enabled_Engaged_Idle = 7;
LaneGuide_Enabled_Engaged_AssistRight = 8;
state = LaneGuide_Fail;
for (int i = 0; i < N; i++) {
event = events[i];
if ((state==LaneGuide_Enabled_Engaged_AssistRight)) {
            stuck_spec(!
(
((((false||(LaneGuide_Enabled_Engaged_LaneDrift<10))||(LaneGuide_Enabled_Engaged_LGMode==0))||(LaneGuide_LG_Enabled==0))||((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0))))))
);
            non_det((((((false+(LaneGuide_Enabled_Engaged_LaneDrift<10))+(LaneGuide_Enabled_Engaged_LGMode==0))+(LaneGuide_LG_Enabled==0))+((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))>1));
if (((event==e)&&(LaneGuide_Enabled_Engaged_LaneDrift<10))) {
state = LaneGuide_Enabled_Engaged_Idle;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&(LaneGuide_Enabled_Engaged_LGMode==0))) {
state = LaneGuide_Enabled_Engaged_WarnRight;
LaneGuide_LG_Warning = 1;
}
else {
}
if (((event==e)&&(LaneGuide_LG_Enabled==0))) {
state = LaneGuide_Disabled;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))) {
state = LaneGuide_Enabled_Override;
LaneGuide_LG_Warning = 0;
}
else {
}
}
else {
if ((state==LaneGuide_Enabled_Engaged_Idle)) {
                stuck_spec(!
(
((((((false||((LaneGuide_Enabled_Engaged_LaneDrift<LaneGuide_minusten)&&(LaneGuide_Enabled_Engaged_LGMode==0)))||((LaneGuide_Enabled_Engaged_LaneDrift>10)&&(LaneGuide_Enabled_Engaged_LGMode==1)))||(LaneGuide_LG_Enabled==0))||((LaneGuide_Enabled_Engaged_LaneDrift>10)&&(LaneGuide_Enabled_Engaged_LGMode==0)))||((LaneGuide_Enabled_Engaged_LaneDrift<LaneGuide_minusten)&&(LaneGuide_Enabled_Engaged_LGMode==1)))||((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0))))))
);
                non_det((((((((false+((LaneGuide_Enabled_Engaged_LaneDrift<LaneGuide_minusten)&&(LaneGuide_Enabled_Engaged_LGMode==0)))+((LaneGuide_Enabled_Engaged_LaneDrift>10)&&(LaneGuide_Enabled_Engaged_LGMode==1)))+(LaneGuide_LG_Enabled==0))+((LaneGuide_Enabled_Engaged_LaneDrift>10)&&(LaneGuide_Enabled_Engaged_LGMode==0)))+((LaneGuide_Enabled_Engaged_LaneDrift<LaneGuide_minusten)&&(LaneGuide_Enabled_Engaged_LGMode==1)))+((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))>1));
if (((event==e)&&((LaneGuide_Enabled_Engaged_LaneDrift<LaneGuide_minusten)&&(LaneGuide_Enabled_Engaged_LGMode==0)))) {
state = LaneGuide_Enabled_Engaged_WarnLeft;
LaneGuide_LG_Warning = 1;
}
else {
}
if (((event==e)&&((LaneGuide_Enabled_Engaged_LaneDrift>10)&&(LaneGuide_Enabled_Engaged_LGMode==1)))) {
state = LaneGuide_Enabled_Engaged_AssistRight;
LaneGuide_LG_Warning = 0;
LaneGuide_Enabled_Engaged_set_SteerOut = 1;
}
else {
}
if (((event==e)&&(LaneGuide_LG_Enabled==0))) {
state = LaneGuide_Disabled;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&((LaneGuide_Enabled_Engaged_LaneDrift>10)&&(LaneGuide_Enabled_Engaged_LGMode==0)))) {
state = LaneGuide_Enabled_Engaged_WarnRight;
LaneGuide_LG_Warning = 1;
}
else {
}
if (((event==e)&&((LaneGuide_Enabled_Engaged_LaneDrift<LaneGuide_minusten)&&(LaneGuide_Enabled_Engaged_LGMode==1)))) {
state = LaneGuide_Enabled_Engaged_AssistLeft;
LaneGuide_LG_Warning = 0;
LaneGuide_Enabled_Engaged_set_SteerOut = LaneGuide_minusone;
}
else {
}
if (((event==e)&&((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))) {
state = LaneGuide_Enabled_Override;
LaneGuide_LG_Warning = 0;
}
else {
}
}
else {
if ((state==LaneGuide_Enabled_Engaged_WarnRight)) {
                    stuck_spec(!
(
((((false||(LaneGuide_LG_Enabled==0))||(LaneGuide_Enabled_Engaged_LaneDrift<10))||((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))||(LaneGuide_Enabled_Engaged_LGMode==1)))
);
                    non_det((((((false+(LaneGuide_LG_Enabled==0))+(LaneGuide_Enabled_Engaged_LaneDrift<10))+((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))+(LaneGuide_Enabled_Engaged_LGMode==1))>1));
if (((event==e)&&(LaneGuide_LG_Enabled==0))) {
state = LaneGuide_Disabled;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&(LaneGuide_Enabled_Engaged_LaneDrift<10))) {
state = LaneGuide_Enabled_Engaged_Idle;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))) {
state = LaneGuide_Enabled_Override;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&(LaneGuide_Enabled_Engaged_LGMode==1))) {
state = LaneGuide_Enabled_Engaged_AssistRight;
LaneGuide_LG_Warning = 1;
LaneGuide_Enabled_Engaged_set_SteerOut = 1;
}
else {
}
}
else {
if ((state==LaneGuide_Disabled)) {
                        stuck_spec(!
(
(false||(LaneGuide_LG_Enabled==1)))
);
if (((event==e)&&(LaneGuide_LG_Enabled==1))) {
state = LaneGuide_Enabled_Disengaged;
}
else {
}
}
else {
if ((state==LaneGuide_Enabled_Engaged_AssistLeft)) {
                            stuck_spec(!
(
((((false||(LaneGuide_Enabled_Engaged_LaneDrift>LaneGuide_minusten))||(LaneGuide_LG_Enabled==0))||((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))||(LaneGuide_Enabled_Engaged_LGMode==0)))
);
                            non_det((((((false+(LaneGuide_Enabled_Engaged_LaneDrift>LaneGuide_minusten))+(LaneGuide_LG_Enabled==0))+((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))+(LaneGuide_Enabled_Engaged_LGMode==0))>1));
if (((event==e)&&(LaneGuide_Enabled_Engaged_LaneDrift>LaneGuide_minusten))) {
state = LaneGuide_Enabled_Engaged_Idle;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&(LaneGuide_LG_Enabled==0))) {
state = LaneGuide_Disabled;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))) {
state = LaneGuide_Enabled_Override;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&(LaneGuide_Enabled_Engaged_LGMode==0))) {
state = LaneGuide_Enabled_Engaged_WarnLeft;
LaneGuide_LG_Warning = 1;
}
else {
}
}
else {
if ((state==LaneGuide_Enabled_Disengaged)) {
                                stuck_spec(!
(
(((false||((LaneGuide_Enabled_Speed<40)||(LaneGuide_Enabled_PRNDL_In!=3)))||((LaneGuide_Enabled_Speed>=40)&&(LaneGuide_Enabled_PRNDL_In==3)))||(LaneGuide_LG_Enabled==0)))
);
                                non_det(((((false+((LaneGuide_Enabled_Speed<40)||(LaneGuide_Enabled_PRNDL_In!=3)))+((LaneGuide_Enabled_Speed>=40)&&(LaneGuide_Enabled_PRNDL_In==3)))+(LaneGuide_LG_Enabled==0))>1));
if (((event==e)&&((LaneGuide_Enabled_Speed<40)||(LaneGuide_Enabled_PRNDL_In!=3)))) {
state = LaneGuide_Enabled_Engaged_WarnLeft;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&((LaneGuide_Enabled_Speed>=40)&&(LaneGuide_Enabled_PRNDL_In==3)))) {
state = LaneGuide_Enabled_Engaged_WarnLeft;
}
else {
}
if (((event==e)&&(LaneGuide_LG_Enabled==0))) {
state = LaneGuide_Disabled;
LaneGuide_LG_Warning = 0;
}
else {
}
}
else {
if ((state==LaneGuide_Enabled_Override)) {
                                    stuck_spec(!
(
((false||(LaneGuide_LG_Enabled==0))||((LaneGuide_Enabled_SteerIn==0)&&((LaneGuide_Enabled_TurnSignal==0)&&(LaneGuide_Enabled_BrakePedal==0)))))
);
                                    non_det((((false+(LaneGuide_LG_Enabled==0))+((LaneGuide_Enabled_SteerIn==0)&&((LaneGuide_Enabled_TurnSignal==0)&&(LaneGuide_Enabled_BrakePedal==0))))>1));
if (((event==e)&&(LaneGuide_LG_Enabled==0))) {
state = LaneGuide_Disabled;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&((LaneGuide_Enabled_SteerIn==0)&&((LaneGuide_Enabled_TurnSignal==0)&&(LaneGuide_Enabled_BrakePedal==0))))) {
state = LaneGuide_Enabled_Disengaged;
}
else {
}
}
else {
if ((state==LaneGuide_Enabled_Engaged_WarnLeft)) {
                                        stuck_spec(!
(
((((false||(LaneGuide_Enabled_Engaged_LGMode==1))||(LaneGuide_Enabled_Engaged_LaneDrift>LaneGuide_minusten))||((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))||(LaneGuide_LG_Enabled==0)))
);
                                        non_det((((((false+(LaneGuide_Enabled_Engaged_LGMode==1))+(LaneGuide_Enabled_Engaged_LaneDrift>LaneGuide_minusten))+((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))+(LaneGuide_LG_Enabled==0))>1));
if (((event==e)&&(LaneGuide_Enabled_Engaged_LGMode==1))) {
state = LaneGuide_Enabled_Engaged_AssistLeft;
LaneGuide_LG_Warning = 0;
LaneGuide_Enabled_Engaged_set_SteerOut = LaneGuide_minusone;
}
else {
}
if (((event==e)&&(LaneGuide_Enabled_Engaged_LaneDrift>LaneGuide_minusten))) {
state = LaneGuide_Enabled_Engaged_Idle;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&((LaneGuide_Enabled_SteerIn>10)||((LaneGuide_Enabled_SteerIn<LaneGuide_minusten)||((LaneGuide_Enabled_TurnSignal==1)||(LaneGuide_Enabled_BrakePedal>0)))))) {
state = LaneGuide_Enabled_Override;
LaneGuide_LG_Warning = 0;
}
else {
}
if (((event==e)&&(LaneGuide_LG_Enabled==0))) {
state = LaneGuide_Disabled;
LaneGuide_LG_Warning = 0;
}
else {
}
}
else {
if ((state==LaneGuide_Fail)) {
                                            stuck_spec(!
(
(false||true))
);
if (((event==Error)&&true)) {
state = LaneGuide_Enabled_Disengaged;
LaneGuide_LG_Warning = 0;
}
else {
}
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
int main () {
int N = 5; int events[N];
klee_make_symbolic(events, sizeof events, "events");

run(events, N);
return 0;
}
