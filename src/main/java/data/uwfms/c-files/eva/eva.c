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
int EmergencyVehicleAvoidance_Enabled_Engaged_DontStop;
int EmergencyVehicleAvoidance_Enabled_Engaged_WayClear;
int EmergencyVehicleAvoidance_EVA_HVI;
int EmergencyVehicleAvoidance_Enabled_Engaged_Set_SteerOut;
int EmergencyVehicleAvoidance_Enabled_Engaged_Set_Brake;
int EmergencyVehicleAvoidance_Enabled_Engaged_Set_Throttle;
int EmergencyVehicleAvoidance_Enabled_Siren;
int EmergencyVehicleAvoidance_minusone;
int EmergencyVehicleAvoidance_Enabled_Speed;
int EmergencyVehicleAvoidance_BrakePedal;
int EmergencyVehicleAvoidance_AccelPedal;
int EmergencyVehicleAvoidance_Enabled_PRNDL_In;
int EmergencyVehicleAvoidance_EVA_Enabled;
int state;
int event;
int e;
int Error;
int EmergencyVehicleAvoidance_Disabled;
int EmergencyVehicleAvoidance_Fail;
int EmergencyVehicleAvoidance_Override;
int EmergencyVehicleAvoidance_Enabled_Engaged_Slow;
int EmergencyVehicleAvoidance_Enabled_Engaged_Coast;
int EmergencyVehicleAvoidance_Enabled_Disengaged;
int EmergencyVehicleAvoidance_Enabled_Engaged_PullOver;

void makeSymbolic(){


//klee_make_symbolic(&e, sizeof e,"e");
//klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_Engaged_PullOver, sizeof EmergencyVehicleAvoidance_Enabled_Engaged_PullOver,"EmergencyVehicleAvoidance_Enabled_Engaged_PullOver");
klee_make_symbolic(&EmergencyVehicleAvoidance_AccelPedal, sizeof EmergencyVehicleAvoidance_AccelPedal,"EmergencyVehicleAvoidance_AccelPedal");
//klee_make_symbolic(&Error, sizeof Error,"Error");
//klee_make_symbolic(&EmergencyVehicleAvoidance_minusone, sizeof EmergencyVehicleAvoidance_minusone,"EmergencyVehicleAvoidance_minusone");
klee_make_symbolic(&EmergencyVehicleAvoidance_Override, sizeof EmergencyVehicleAvoidance_Override,"EmergencyVehicleAvoidance_Override");
klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_Engaged_Set_SteerOut, sizeof EmergencyVehicleAvoidance_Enabled_Engaged_Set_SteerOut,"EmergencyVehicleAvoidance_Enabled_Engaged_Set_SteerOut");
klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_Engaged_Set_Brake, sizeof EmergencyVehicleAvoidance_Enabled_Engaged_Set_Brake,"EmergencyVehicleAvoidance_Enabled_Engaged_Set_Brake");
klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_Siren, sizeof EmergencyVehicleAvoidance_Enabled_Siren,"EmergencyVehicleAvoidance_Enabled_Siren");
klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_Engaged_Set_Throttle, sizeof EmergencyVehicleAvoidance_Enabled_Engaged_Set_Throttle,"EmergencyVehicleAvoidance_Enabled_Engaged_Set_Throttle");
//klee_make_symbolic(&EmergencyVehicleAvoidance_Disabled, sizeof EmergencyVehicleAvoidance_Disabled,"EmergencyVehicleAvoidance_Disabled");
klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_PRNDL_In, sizeof EmergencyVehicleAvoidance_Enabled_PRNDL_In,"EmergencyVehicleAvoidance_Enabled_PRNDL_In");
//klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_Engaged_Slow, sizeof EmergencyVehicleAvoidance_Enabled_Engaged_Slow,"EmergencyVehicleAvoidance_Enabled_Engaged_Slow");
klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_Speed, sizeof EmergencyVehicleAvoidance_Enabled_Speed,"EmergencyVehicleAvoidance_Enabled_Speed");
//klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_Engaged_DontStop, sizeof EmergencyVehicleAvoidance_Enabled_Engaged_DontStop,"EmergencyVehicleAvoidance_Enabled_Engaged_DontStop");
//klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_Disengaged, sizeof EmergencyVehicleAvoidance_Enabled_Disengaged,"EmergencyVehicleAvoidance_Enabled_Disengaged");
//klee_make_symbolic(&EmergencyVehicleAvoidance_EVA_Enabled, sizeof EmergencyVehicleAvoidance_EVA_Enabled,"EmergencyVehicleAvoidance_EVA_Enabled");
//klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_Engaged_WayClear, sizeof EmergencyVehicleAvoidance_Enabled_Engaged_WayClear,"EmergencyVehicleAvoidance_Enabled_Engaged_WayClear");
klee_make_symbolic(&EmergencyVehicleAvoidance_BrakePedal, sizeof EmergencyVehicleAvoidance_BrakePedal,"EmergencyVehicleAvoidance_BrakePedal");
//klee_make_symbolic(&state, sizeof state,"state");
klee_make_symbolic(&event, sizeof event,"event");
//klee_make_symbolic(&EmergencyVehicleAvoidance_Enabled_Engaged_Coast, sizeof EmergencyVehicleAvoidance_Enabled_Engaged_Coast,"EmergencyVehicleAvoidance_Enabled_Engaged_Coast");
klee_make_symbolic(&EmergencyVehicleAvoidance_EVA_HVI, sizeof EmergencyVehicleAvoidance_EVA_HVI,"EmergencyVehicleAvoidance_EVA_HVI");
//klee_make_symbolic(&EmergencyVehicleAvoidance_Fail, sizeof EmergencyVehicleAvoidance_Fail,"EmergencyVehicleAvoidance_Fail");




}

void run(int * events, int N){
e = 0;
Error = 1;
EmergencyVehicleAvoidance_Disabled = 0;
EmergencyVehicleAvoidance_Fail = 1;
EmergencyVehicleAvoidance_Override = 2;
EmergencyVehicleAvoidance_Enabled_Engaged_Slow = 3;
EmergencyVehicleAvoidance_Enabled_Engaged_Coast = 4;
EmergencyVehicleAvoidance_Enabled_Disengaged = 5;
EmergencyVehicleAvoidance_Enabled_Engaged_PullOver = 6;
state = EmergencyVehicleAvoidance_Disabled;
for (int i = 0; i < N; i++) {
event = events[i];
if ((state==EmergencyVehicleAvoidance_Enabled_Engaged_PullOver)) {
            stuck_spec(!
(
((((((false||((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==0)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==0)))||(EmergencyVehicleAvoidance_EVA_Enabled!=1))||((EmergencyVehicleAvoidance_Enabled_Siren==0)||((EmergencyVehicleAvoidance_Enabled_Speed==0)||(EmergencyVehicleAvoidance_Enabled_PRNDL_In!=3))))||((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30)))||true)||(EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==1)))
);
            non_det((((((((false+((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==0)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==0)))+(EmergencyVehicleAvoidance_EVA_Enabled!=1))+((EmergencyVehicleAvoidance_Enabled_Siren==0)||((EmergencyVehicleAvoidance_Enabled_Speed==0)||(EmergencyVehicleAvoidance_Enabled_PRNDL_In!=3))))+((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30)))+true)+(EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==1))>1));
if (((event==e)&&((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==0)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==0)))) {
state = EmergencyVehicleAvoidance_Enabled_Engaged_Slow;
EmergencyVehicleAvoidance_Enabled_Engaged_Set_Brake = 30;
}
else {
}
if (((event==e)&&(EmergencyVehicleAvoidance_EVA_Enabled!=1))) {
state = EmergencyVehicleAvoidance_Disabled;
EmergencyVehicleAvoidance_EVA_HVI = 0;
}
else {
}
if (((event==e)&&((EmergencyVehicleAvoidance_Enabled_Siren==0)||((EmergencyVehicleAvoidance_Enabled_Speed==0)||(EmergencyVehicleAvoidance_Enabled_PRNDL_In!=3))))) {
state = EmergencyVehicleAvoidance_Enabled_Disengaged;
EmergencyVehicleAvoidance_EVA_HVI = 1;
}
else {
}
if (((event==e)&&((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30)))) {
state = EmergencyVehicleAvoidance_Override;
EmergencyVehicleAvoidance_EVA_HVI = 2;
}
else {
}
if (((event==Error)&&true)) {
state = EmergencyVehicleAvoidance_Fail;
EmergencyVehicleAvoidance_EVA_HVI = 4;
}
else {
}
if (((event==e)&&(EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==1))) {
state = EmergencyVehicleAvoidance_Enabled_Engaged_Coast;
EmergencyVehicleAvoidance_Enabled_Engaged_Set_Throttle = 35;
}
else {
}
}
else {
if ((state==EmergencyVehicleAvoidance_Enabled_Disengaged)) {
                stuck_spec(!
(
((((false||((EmergencyVehicleAvoidance_Enabled_Siren==1)&&((EmergencyVehicleAvoidance_Enabled_Speed>0)&&(EmergencyVehicleAvoidance_Enabled_PRNDL_In==3))))||(EmergencyVehicleAvoidance_EVA_Enabled!=1))||true)||((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30))))
);
                non_det((((((false+((EmergencyVehicleAvoidance_Enabled_Siren==1)&&((EmergencyVehicleAvoidance_Enabled_Speed>0)&&(EmergencyVehicleAvoidance_Enabled_PRNDL_In==3))))+(EmergencyVehicleAvoidance_EVA_Enabled!=1))+true)+((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30)))>1));
if (((event==e)&&((EmergencyVehicleAvoidance_Enabled_Siren==1)&&((EmergencyVehicleAvoidance_Enabled_Speed>0)&&(EmergencyVehicleAvoidance_Enabled_PRNDL_In==3))))) {
state = EmergencyVehicleAvoidance_Enabled_Engaged_Slow;
EmergencyVehicleAvoidance_EVA_HVI = 1;
}
else {
}
if (((event==e)&&(EmergencyVehicleAvoidance_EVA_Enabled!=1))) {
state = EmergencyVehicleAvoidance_Disabled;
EmergencyVehicleAvoidance_EVA_HVI = 0;
}
else {
}
if (((event==Error)&&true)) {
state = EmergencyVehicleAvoidance_Fail;
EmergencyVehicleAvoidance_EVA_HVI = 4;
}
else {
}
if (((event==e)&&((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30)))) {
state = EmergencyVehicleAvoidance_Override;
EmergencyVehicleAvoidance_EVA_HVI = 2;
}
else {
}
}
else {
if ((state==EmergencyVehicleAvoidance_Enabled_Engaged_Coast)) {
                    stuck_spec(!
(
((((((false||(EmergencyVehicleAvoidance_EVA_Enabled!=1))||((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30)))||((EmergencyVehicleAvoidance_Enabled_Siren==0)||((EmergencyVehicleAvoidance_Enabled_Speed==0)||(EmergencyVehicleAvoidance_Enabled_PRNDL_In!=3))))||true)||((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop!=1)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==1)))||((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==0)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==0))))
);
                    non_det((((((((false+(EmergencyVehicleAvoidance_EVA_Enabled!=1))+((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30)))+((EmergencyVehicleAvoidance_Enabled_Siren==0)||((EmergencyVehicleAvoidance_Enabled_Speed==0)||(EmergencyVehicleAvoidance_Enabled_PRNDL_In!=3))))+true)+((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop!=1)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==1)))+((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==0)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==0)))>1));
if (((event==e)&&(EmergencyVehicleAvoidance_EVA_Enabled!=1))) {
state = EmergencyVehicleAvoidance_Disabled;
EmergencyVehicleAvoidance_EVA_HVI = 0;
}
else {
}
if (((event==e)&&((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30)))) {
state = EmergencyVehicleAvoidance_Override;
EmergencyVehicleAvoidance_EVA_HVI = 2;
}
else {
}
if (((event==e)&&((EmergencyVehicleAvoidance_Enabled_Siren==0)||((EmergencyVehicleAvoidance_Enabled_Speed==0)||(EmergencyVehicleAvoidance_Enabled_PRNDL_In!=3))))) {
state = EmergencyVehicleAvoidance_Enabled_Disengaged;
EmergencyVehicleAvoidance_EVA_HVI = 1;
}
else {
}
if (((event==Error)&&true)) {
state = EmergencyVehicleAvoidance_Fail;
EmergencyVehicleAvoidance_EVA_HVI = 4;
}
else {
}
if (((event==e)&&((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop!=1)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==1)))) {
state = EmergencyVehicleAvoidance_Enabled_Engaged_PullOver;
EmergencyVehicleAvoidance_Enabled_Engaged_Set_Brake = 60;
EmergencyVehicleAvoidance_Enabled_Engaged_Set_SteerOut = EmergencyVehicleAvoidance_minusone;
}
else {
}
if (((event==e)&&((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==0)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==0)))) {
state = EmergencyVehicleAvoidance_Enabled_Engaged_Slow;
EmergencyVehicleAvoidance_Enabled_Engaged_Set_Brake = 30;
}
else {
}
}
else {
if ((state==EmergencyVehicleAvoidance_Enabled_Engaged_Slow)) {
                        stuck_spec(!
(
((((((false||((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop!=1)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==1)))||(EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==1))||(EmergencyVehicleAvoidance_EVA_Enabled!=1))||true)||((EmergencyVehicleAvoidance_Enabled_Siren==0)||((EmergencyVehicleAvoidance_Enabled_Speed==0)||(EmergencyVehicleAvoidance_Enabled_PRNDL_In!=3))))||((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30))))
);
                        non_det((((((((false+((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop!=1)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==1)))+(EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==1))+(EmergencyVehicleAvoidance_EVA_Enabled!=1))+true)+((EmergencyVehicleAvoidance_Enabled_Siren==0)||((EmergencyVehicleAvoidance_Enabled_Speed==0)||(EmergencyVehicleAvoidance_Enabled_PRNDL_In!=3))))+((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30)))>1));
if (((event==e)&&((EmergencyVehicleAvoidance_Enabled_Engaged_DontStop!=1)&&(EmergencyVehicleAvoidance_Enabled_Engaged_WayClear==1)))) {
state = EmergencyVehicleAvoidance_Enabled_Engaged_PullOver;
EmergencyVehicleAvoidance_Enabled_Engaged_Set_Brake = 60;
EmergencyVehicleAvoidance_Enabled_Engaged_Set_SteerOut = EmergencyVehicleAvoidance_minusone;
}
else {
}
if (((event==e)&&(EmergencyVehicleAvoidance_Enabled_Engaged_DontStop==1))) {
state = EmergencyVehicleAvoidance_Enabled_Engaged_Coast;
EmergencyVehicleAvoidance_Enabled_Engaged_Set_Throttle = 35;
}
else {
}
if (((event==e)&&(EmergencyVehicleAvoidance_EVA_Enabled!=1))) {
state = EmergencyVehicleAvoidance_Disabled;
EmergencyVehicleAvoidance_EVA_HVI = 0;
}
else {
}
if (((event==Error)&&true)) {
state = EmergencyVehicleAvoidance_Fail;
EmergencyVehicleAvoidance_EVA_HVI = 4;
}
else {
}
if (((event==e)&&((EmergencyVehicleAvoidance_Enabled_Siren==0)||((EmergencyVehicleAvoidance_Enabled_Speed==0)||(EmergencyVehicleAvoidance_Enabled_PRNDL_In!=3))))) {
state = EmergencyVehicleAvoidance_Enabled_Disengaged;
EmergencyVehicleAvoidance_EVA_HVI = 1;
}
else {
}
if (((event==e)&&((EmergencyVehicleAvoidance_BrakePedal!=0)||(EmergencyVehicleAvoidance_AccelPedal>=30)))) {
state = EmergencyVehicleAvoidance_Override;
EmergencyVehicleAvoidance_EVA_HVI = 2;
}
else {
}
}
else {
if ((state==EmergencyVehicleAvoidance_Override)) {
                            stuck_spec(!
(
(((false||true)||((EmergencyVehicleAvoidance_BrakePedal==0)&&(EmergencyVehicleAvoidance_AccelPedal<30)))||(EmergencyVehicleAvoidance_EVA_Enabled!=1)))
);
                            non_det(((((false+true)+((EmergencyVehicleAvoidance_BrakePedal==0)&&(EmergencyVehicleAvoidance_AccelPedal<30)))+(EmergencyVehicleAvoidance_EVA_Enabled!=1))>1));
if (((event==Error)&&true)) {
state = EmergencyVehicleAvoidance_Fail;
EmergencyVehicleAvoidance_EVA_HVI = 4;
}
else {
}
if (((event==e)&&((EmergencyVehicleAvoidance_BrakePedal==0)&&(EmergencyVehicleAvoidance_AccelPedal<30)))) {
state = EmergencyVehicleAvoidance_Enabled_Disengaged;
EmergencyVehicleAvoidance_EVA_HVI = 2;
}
else {
}
if (((event==e)&&(EmergencyVehicleAvoidance_EVA_Enabled!=1))) {
state = EmergencyVehicleAvoidance_Disabled;
EmergencyVehicleAvoidance_EVA_HVI = 0;
}
else {
}
}
else {
if ((state==EmergencyVehicleAvoidance_Fail)) {
}
else {
if ((state==EmergencyVehicleAvoidance_Disabled)) {
                                    stuck_spec(!
(
(false||(EmergencyVehicleAvoidance_EVA_Enabled==1)))
);
if (((event==e)&&(EmergencyVehicleAvoidance_EVA_Enabled==1))) {
state = EmergencyVehicleAvoidance_Enabled_Disengaged;
EmergencyVehicleAvoidance_EVA_HVI = 1;
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
int main () {
int N = 5; int events[N];
klee_make_symbolic(events, sizeof events, "events");
run(events, N);
return 0;
}
