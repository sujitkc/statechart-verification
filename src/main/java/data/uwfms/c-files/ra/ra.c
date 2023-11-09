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
int ReversingAssistance_RA_Enabled;
int ReversingAssistance_AccelPedal;
int ReversingAssistance_Enabled_Engaged_Set_Brake;
int ReversingAssistance_RA_HVI;
int ReversingAssistance_Enabled_Engaged_ObstacleZone;
int ReversingAssistance_RA_Warning;
int ReversingAssistance_Enabled_PRNDL_In;
int ReversingAssistance_Enabled_Speed;
int ReversingAssistance_Enabled_BrakePedal;
int state;
int event;
int e;
int Error;
int ReversingAssistance_Disabled;
int ReversingAssistance_Enabled_Hold;
int ReversingAssistance_Enabled_Engaged_Assist;
int ReversingAssistance_Override;
int ReversingAssistance_Fail;
int ReversingAssistance_Enabled_Disengaged;
int ReversingAssistance_Enabled_Engaged_Idle;
int ReversingAssistance_Enabled_Engaged_Warn;

void makeSymbolic(){


klee_make_symbolic(&event, sizeof event,"event");
//klee_make_symbolic(&e, sizeof e,"e");
//klee_make_symbolic(&ReversingAssistance_Fail, sizeof ReversingAssistance_Fail,"ReversingAssistance_Fail");
//klee_make_symbolic(&Error, sizeof Error,"Error");
//klee_make_symbolic(&ReversingAssistance_RA_Warning, sizeof ReversingAssistance_RA_Warning,"ReversingAssistance_RA_Warning");
//klee_make_symbolic(&ReversingAssistance_Enabled_Engaged_Idle, sizeof ReversingAssistance_Enabled_Engaged_Idle,"ReversingAssistance_Enabled_Engaged_Idle");
klee_make_symbolic(&ReversingAssistance_Enabled_PRNDL_In, sizeof ReversingAssistance_Enabled_PRNDL_In,"ReversingAssistance_Enabled_PRNDL_In");
klee_make_symbolic(&ReversingAssistance_AccelPedal, sizeof ReversingAssistance_AccelPedal,"ReversingAssistance_AccelPedal");
//klee_make_symbolic(&ReversingAssistance_Enabled_Engaged_Warn, sizeof ReversingAssistance_Enabled_Engaged_Warn,"ReversingAssistance_Enabled_Engaged_Warn");
//klee_make_symbolic(&ReversingAssistance_Enabled_Engaged_Assist, sizeof ReversingAssistance_Enabled_Engaged_Assist,"ReversingAssistance_Enabled_Engaged_Assist");
//klee_make_symbolic(&ReversingAssistance_Enabled_Disengaged, sizeof ReversingAssistance_Enabled_Disengaged,"ReversingAssistance_Enabled_Disengaged");
//klee_make_symbolic(&ReversingAssistance_Enabled_Engaged_ObstacleZone, sizeof ReversingAssistance_Enabled_Engaged_ObstacleZone,"ReversingAssistance_Enabled_Engaged_ObstacleZone");
//klee_make_symbolic(&ReversingAssistance_Disabled, sizeof ReversingAssistance_Disabled,"ReversingAssistance_Disabled");
klee_make_symbolic(&ReversingAssistance_RA_Enabled, sizeof ReversingAssistance_RA_Enabled,"ReversingAssistance_RA_Enabled");
klee_make_symbolic(&ReversingAssistance_Enabled_Speed, sizeof ReversingAssistance_Enabled_Speed,"ReversingAssistance_Enabled_Speed");
//klee_make_symbolic(&ReversingAssistance_Override, sizeof ReversingAssistance_Override,"ReversingAssistance_Override");
//klee_make_symbolic(&state, sizeof state,"state");
//klee_make_symbolic(&ReversingAssistance_Enabled_Engaged_Set_Brake, sizeof ReversingAssistance_Enabled_Engaged_Set_Brake,"ReversingAssistance_Enabled_Engaged_Set_Brake");
//klee_make_symbolic(&event, sizeof event,"event");
//klee_make_symbolic(&ReversingAssistance_Enabled_Hold, sizeof ReversingAssistance_Enabled_Hold,"ReversingAssistance_Enabled_Hold");
klee_make_symbolic(&ReversingAssistance_RA_HVI, sizeof ReversingAssistance_RA_HVI,"ReversingAssistance_RA_HVI");
klee_make_symbolic(&ReversingAssistance_Enabled_BrakePedal, sizeof ReversingAssistance_Enabled_BrakePedal,"ReversingAssistance_Enabled_BrakePedal");



}
void run(int * events, int N){
e = 0;
Error = 1;
ReversingAssistance_Disabled = 0;
ReversingAssistance_Enabled_Hold = 1;
ReversingAssistance_Enabled_Engaged_Assist = 2;
ReversingAssistance_Override = 3;
ReversingAssistance_Fail = 4;
ReversingAssistance_Enabled_Disengaged = 5;
ReversingAssistance_Enabled_Engaged_Idle = 6;
ReversingAssistance_Enabled_Engaged_Warn = 7;
state = ReversingAssistance_Disabled;
for (int i = 0; i < N; i++) {
event = events[i];
if ((state==ReversingAssistance_Enabled_Engaged_Warn)) {
            stuck_spec(!
(
(((((((false||(ReversingAssistance_Enabled_Speed==0))||(ReversingAssistance_AccelPedal>=35))||(ReversingAssistance_Enabled_Engaged_ObstacleZone==2))||(ReversingAssistance_Enabled_Engaged_ObstacleZone==0))||(((ReversingAssistance_Enabled_Speed>0)&&(ReversingAssistance_Enabled_Speed<10))||((ReversingAssistance_Enabled_Speed>25)||(ReversingAssistance_Enabled_PRNDL_In!=1))))||true)||(ReversingAssistance_RA_Enabled!=1)))
);
            non_det(((((((((false+(ReversingAssistance_Enabled_Speed==0))+(ReversingAssistance_AccelPedal>=35))+(ReversingAssistance_Enabled_Engaged_ObstacleZone==2))+(ReversingAssistance_Enabled_Engaged_ObstacleZone==0))+(((ReversingAssistance_Enabled_Speed>0)&&(ReversingAssistance_Enabled_Speed<10))||((ReversingAssistance_Enabled_Speed>25)||(ReversingAssistance_Enabled_PRNDL_In!=1))))+true)+(ReversingAssistance_RA_Enabled!=1))>1));
if (((event==e)&&(ReversingAssistance_Enabled_Speed==0))) {
state = ReversingAssistance_Enabled_Hold;
ReversingAssistance_RA_Warning = 3;
}
else {
}
if (((event==e)&&(ReversingAssistance_AccelPedal>=35))) {
state = ReversingAssistance_Override;
ReversingAssistance_RA_HVI = 4;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_Enabled_Engaged_ObstacleZone==2))) {
state = ReversingAssistance_Enabled_Engaged_Assist;
ReversingAssistance_RA_Warning = 2;
ReversingAssistance_Enabled_Engaged_Set_Brake = 60;
}
else {
}
if (((event==e)&&(ReversingAssistance_Enabled_Engaged_ObstacleZone==0))) {
state = ReversingAssistance_Enabled_Engaged_Idle;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(((ReversingAssistance_Enabled_Speed>0)&&(ReversingAssistance_Enabled_Speed<10))||((ReversingAssistance_Enabled_Speed>25)||(ReversingAssistance_Enabled_PRNDL_In!=1))))) {
state = ReversingAssistance_Enabled_Disengaged;
ReversingAssistance_RA_HVI = 1;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==Error)&&true)) {
state = ReversingAssistance_Fail;
ReversingAssistance_RA_HVI = 3;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_RA_Enabled!=1))) {
state = ReversingAssistance_Disabled;
ReversingAssistance_RA_HVI = 0;
ReversingAssistance_RA_Warning = 0;
}
else {
}
}
else {
if ((state==ReversingAssistance_Enabled_Engaged_Idle)) {
                stuck_spec(!
(
(((((((false||(ReversingAssistance_AccelPedal>=35))||(ReversingAssistance_Enabled_Engaged_ObstacleZone==1))||(ReversingAssistance_Enabled_Speed==0))||(((ReversingAssistance_Enabled_Speed>0)&&(ReversingAssistance_Enabled_Speed<10))||((ReversingAssistance_Enabled_Speed>25)||(ReversingAssistance_Enabled_PRNDL_In!=1))))||true)||(ReversingAssistance_RA_Enabled!=1))||(ReversingAssistance_Enabled_Engaged_ObstacleZone==2)))
);
                non_det(((((((((false+(ReversingAssistance_AccelPedal>=35))+(ReversingAssistance_Enabled_Engaged_ObstacleZone==1))+(ReversingAssistance_Enabled_Speed==0))+(((ReversingAssistance_Enabled_Speed>0)&&(ReversingAssistance_Enabled_Speed<10))||((ReversingAssistance_Enabled_Speed>25)||(ReversingAssistance_Enabled_PRNDL_In!=1))))+true)+(ReversingAssistance_RA_Enabled!=1))+(ReversingAssistance_Enabled_Engaged_ObstacleZone==2))>1));
if (((event==e)&&(ReversingAssistance_AccelPedal>=35))) {
state = ReversingAssistance_Override;
ReversingAssistance_RA_HVI = 4;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_Enabled_Engaged_ObstacleZone==1))) {
state = ReversingAssistance_Enabled_Engaged_Warn;
ReversingAssistance_RA_Warning = 1;
}
else {
}
if (((event==e)&&(ReversingAssistance_Enabled_Speed==0))) {
state = ReversingAssistance_Enabled_Hold;
ReversingAssistance_RA_Warning = 3;
}
else {
}
if (((event==e)&&(((ReversingAssistance_Enabled_Speed>0)&&(ReversingAssistance_Enabled_Speed<10))||((ReversingAssistance_Enabled_Speed>25)||(ReversingAssistance_Enabled_PRNDL_In!=1))))) {
state = ReversingAssistance_Enabled_Disengaged;
ReversingAssistance_RA_HVI = 1;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==Error)&&true)) {
state = ReversingAssistance_Fail;
ReversingAssistance_RA_HVI = 3;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_RA_Enabled!=1))) {
state = ReversingAssistance_Disabled;
ReversingAssistance_RA_HVI = 0;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_Enabled_Engaged_ObstacleZone==2))) {
state = ReversingAssistance_Enabled_Engaged_Assist;
ReversingAssistance_RA_Warning = 2;
ReversingAssistance_Enabled_Engaged_Set_Brake = 60;
}
else {
}
}
else {
if ((state==ReversingAssistance_Enabled_Disengaged)) {
                    stuck_spec(!
(
((((false||(ReversingAssistance_AccelPedal>=35))||true)||(ReversingAssistance_RA_Enabled!=1))||((ReversingAssistance_Enabled_Speed>=10)&&((ReversingAssistance_Enabled_Speed<=25)&&(ReversingAssistance_Enabled_PRNDL_In==1)))))
);
                    non_det((((((false+(ReversingAssistance_AccelPedal>=35))+true)+(ReversingAssistance_RA_Enabled!=1))+((ReversingAssistance_Enabled_Speed>=10)&&((ReversingAssistance_Enabled_Speed<=25)&&(ReversingAssistance_Enabled_PRNDL_In==1))))>1));
if (((event==e)&&(ReversingAssistance_AccelPedal>=35))) {
state = ReversingAssistance_Override;
ReversingAssistance_RA_HVI = 4;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==Error)&&true)) {
state = ReversingAssistance_Fail;
ReversingAssistance_RA_HVI = 3;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_RA_Enabled!=1))) {
state = ReversingAssistance_Disabled;
ReversingAssistance_RA_HVI = 0;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&((ReversingAssistance_Enabled_Speed>=10)&&((ReversingAssistance_Enabled_Speed<=25)&&(ReversingAssistance_Enabled_PRNDL_In==1))))) {
state = ReversingAssistance_Enabled_Engaged_Idle;
ReversingAssistance_RA_HVI = 2;
}
else {
}
}
else {
if ((state==ReversingAssistance_Fail)) {
}
else {
if ((state==ReversingAssistance_Override)) {
                            stuck_spec(!
(
(((false||true)||(ReversingAssistance_AccelPedal<35))||(ReversingAssistance_RA_Enabled!=1)))
);
                            non_det(((((false+true)+(ReversingAssistance_AccelPedal<35))+(ReversingAssistance_RA_Enabled!=1))>1));
if (((event==Error)&&true)) {
state = ReversingAssistance_Fail;
ReversingAssistance_RA_HVI = 3;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_AccelPedal<35))) {
state = ReversingAssistance_Enabled_Disengaged;
ReversingAssistance_RA_HVI = 1;
}
else {
}
if (((event==e)&&(ReversingAssistance_RA_Enabled!=1))) {
state = ReversingAssistance_Disabled;
ReversingAssistance_RA_HVI = 0;
ReversingAssistance_RA_Warning = 0;
}
else {
}
}
else {
if ((state==ReversingAssistance_Enabled_Engaged_Assist)) {
                                stuck_spec(!
(
(((((((false||(ReversingAssistance_Enabled_Engaged_ObstacleZone==1))||(ReversingAssistance_Enabled_Speed==0))||(((ReversingAssistance_Enabled_Speed>0)&&(ReversingAssistance_Enabled_Speed<10))||((ReversingAssistance_Enabled_Speed>25)||(ReversingAssistance_Enabled_PRNDL_In!=1))))||true)||(ReversingAssistance_Enabled_Engaged_ObstacleZone==0))||(ReversingAssistance_RA_Enabled!=1))||(ReversingAssistance_AccelPedal>=35)))
);
                                non_det(((((((((false+(ReversingAssistance_Enabled_Engaged_ObstacleZone==1))+(ReversingAssistance_Enabled_Speed==0))+(((ReversingAssistance_Enabled_Speed>0)&&(ReversingAssistance_Enabled_Speed<10))||((ReversingAssistance_Enabled_Speed>25)||(ReversingAssistance_Enabled_PRNDL_In!=1))))+true)+(ReversingAssistance_Enabled_Engaged_ObstacleZone==0))+(ReversingAssistance_RA_Enabled!=1))+(ReversingAssistance_AccelPedal>=35))>1));
if (((event==e)&&(ReversingAssistance_Enabled_Engaged_ObstacleZone==1))) {
state = ReversingAssistance_Enabled_Engaged_Warn;
ReversingAssistance_RA_Warning = 1;
}
else {
}
if (((event==e)&&(ReversingAssistance_Enabled_Speed==0))) {
state = ReversingAssistance_Enabled_Hold;
ReversingAssistance_RA_Warning = 3;
}
else {
}
if (((event==e)&&(((ReversingAssistance_Enabled_Speed>0)&&(ReversingAssistance_Enabled_Speed<10))||((ReversingAssistance_Enabled_Speed>25)||(ReversingAssistance_Enabled_PRNDL_In!=1))))) {
state = ReversingAssistance_Enabled_Disengaged;
ReversingAssistance_RA_HVI = 1;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==Error)&&true)) {
state = ReversingAssistance_Fail;
ReversingAssistance_RA_HVI = 3;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_Enabled_Engaged_ObstacleZone==0))) {
state = ReversingAssistance_Enabled_Engaged_Idle;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_RA_Enabled!=1))) {
state = ReversingAssistance_Disabled;
ReversingAssistance_RA_HVI = 0;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_AccelPedal>=35))) {
state = ReversingAssistance_Override;
ReversingAssistance_RA_HVI = 4;
ReversingAssistance_RA_Warning = 0;
}
else {
}
}
else {
if ((state==ReversingAssistance_Enabled_Hold)) {
                                    stuck_spec(!
(
((((false||(ReversingAssistance_Enabled_BrakePedal>20))||(ReversingAssistance_RA_Enabled!=1))||(ReversingAssistance_AccelPedal>=35))||true))
);
                                    non_det((((((false+(ReversingAssistance_Enabled_BrakePedal>20))+(ReversingAssistance_RA_Enabled!=1))+(ReversingAssistance_AccelPedal>=35))+true)>1));
if (((event==e)&&(ReversingAssistance_Enabled_BrakePedal>20))) {
state = ReversingAssistance_Enabled_Disengaged;
ReversingAssistance_RA_HVI = 1;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_RA_Enabled!=1))) {
state = ReversingAssistance_Disabled;
ReversingAssistance_RA_HVI = 0;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==e)&&(ReversingAssistance_AccelPedal>=35))) {
state = ReversingAssistance_Override;
ReversingAssistance_RA_HVI = 4;
ReversingAssistance_RA_Warning = 0;
}
else {
}
if (((event==Error)&&true)) {
state = ReversingAssistance_Fail;
ReversingAssistance_RA_HVI = 3;
ReversingAssistance_RA_Warning = 0;
}
else {
}
}
else {
if ((state==ReversingAssistance_Disabled)) {
                                        stuck_spec(!
(
(false||(ReversingAssistance_RA_Enabled==1)))
);
if (((event==e)&&(ReversingAssistance_RA_Enabled==1))) {
state = ReversingAssistance_Enabled_Disengaged;
ReversingAssistance_RA_HVI = 1;
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
int main () {
int N = 5; int events[N];
klee_make_symbolic(events, sizeof events, "events");
run(events, N);
return 0;
}
