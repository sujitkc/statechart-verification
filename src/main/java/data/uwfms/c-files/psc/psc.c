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
int ParkingSpaceCentering_PSC_Enabled;
int ParkingSpaceCentering_PSC_HVI;
int ParkingSpaceCentering_AccelPedal;
int ParkingSpaceCentering_Set_Brake;
int ParkingSpaceCentering_Enabled_PRNDL_In;
int ParkingSpaceCentering_BrakePedal;
int ParkingSpaceCentering_Enabled_Speed;
int ParkingSpaceCentering_Set_Throttle;
int ParkingSpaceCentering_LeftLine;
int ParkingSpaceCentering_RightLine;
int ParkingSpaceCentering_Set_SteerOut;
int ParkingSpaceCentering_FrontLine;
int ParkingSpaceCentering_minusone;
int state;
int event;
int e;
int Error;
int ParkingSpaceCentering_Disabled;
int ParkingSpaceCentering_Enabled_Engaged_MoveLeft;
int ParkingSpaceCentering_Enabled_Engaged_Straight;
int ParkingSpaceCentering_Enabled_Disengaged;
int ParkingSpaceCentering_Enabled_Halt;
int ParkingSpaceCentering_Fail;
int ParkingSpaceCentering_Enabled_Engaged_MoveRight;
int ParkingSpaceCentering_Override;

void makeSymbolic(){


//klee_make_symbolic(&ParkingSpaceCentering_minusone, sizeof ParkingSpaceCentering_minusone,"ParkingSpaceCentering_minusone");
//klee_make_symbolic(&ParkingSpaceCentering_Enabled_Engaged_Straight, sizeof ParkingSpaceCentering_Enabled_Engaged_Straight,"ParkingSpaceCentering_Enabled_Engaged_Straight");
klee_make_symbolic(&ParkingSpaceCentering_Enabled_Speed, sizeof ParkingSpaceCentering_Enabled_Speed,"ParkingSpaceCentering_Enabled_Speed");
klee_make_symbolic(&ParkingSpaceCentering_Set_SteerOut, sizeof ParkingSpaceCentering_Set_SteerOut,"ParkingSpaceCentering_Set_SteerOut");
//klee_make_symbolic(&ParkingSpaceCentering_Enabled_Disengaged, sizeof ParkingSpaceCentering_Enabled_Disengaged,"ParkingSpaceCentering_Enabled_Disengaged");
klee_make_symbolic(&ParkingSpaceCentering_PSC_HVI, sizeof ParkingSpaceCentering_PSC_HVI,"ParkingSpaceCentering_PSC_HVI");
klee_make_symbolic(&ParkingSpaceCentering_Set_Brake, sizeof ParkingSpaceCentering_Set_Brake,"ParkingSpaceCentering_Set_Brake");
//klee_make_symbolic(&ParkingSpaceCentering_RightLine, sizeof ParkingSpaceCentering_RightLine,"ParkingSpaceCentering_RightLine");
klee_make_symbolic(&ParkingSpaceCentering_AccelPedal, sizeof ParkingSpaceCentering_AccelPedal,"ParkingSpaceCentering_AccelPedal");
//klee_make_symbolic(&ParkingSpaceCentering_Disabled, sizeof ParkingSpaceCentering_Disabled,"ParkingSpaceCentering_Disabled");
//klee_make_symbolic(&ParkingSpaceCentering_Override, sizeof ParkingSpaceCentering_Override,"ParkingSpaceCentering_Override");
//klee_make_symbolic(&state, sizeof state,"state");
//klee_make_symbolic(&ParkingSpaceCentering_Enabled_Engaged_MoveLeft, sizeof ParkingSpaceCentering_Enabled_Engaged_MoveLeft,"ParkingSpaceCentering_Enabled_Engaged_MoveLeft");
klee_make_symbolic(&event, sizeof event,"event");
//klee_make_symbolic(&ParkingSpaceCentering_FrontLine, sizeof ParkingSpaceCentering_FrontLine,"ParkingSpaceCentering_FrontLine");
//klee_make_symbolic(&e, sizeof e,"e");
//klee_make_symbolic(&Error, sizeof Error,"Error");
klee_make_symbolic(&ParkingSpaceCentering_BrakePedal, sizeof ParkingSpaceCentering_BrakePedal,"ParkingSpaceCentering_BrakePedal");
klee_make_symbolic(&ParkingSpaceCentering_Enabled_Engaged_MoveRight, sizeof ParkingSpaceCentering_Enabled_Engaged_MoveRight,"ParkingSpaceCentering_Enabled_Engaged_MoveRight");
//klee_make_symbolic(&ParkingSpaceCentering_Enabled_Halt, sizeof ParkingSpaceCentering_Enabled_Halt,"ParkingSpaceCentering_Enabled_Halt");
//klee_make_symbolic(&ParkingSpaceCentering_Fail, sizeof ParkingSpaceCentering_Fail,"ParkingSpaceCentering_Fail");
//klee_make_symbolic(&ParkingSpaceCentering_LeftLine, sizeof ParkingSpaceCentering_LeftLine,"ParkingSpaceCentering_LeftLine");
klee_make_symbolic(&ParkingSpaceCentering_Set_Throttle, sizeof ParkingSpaceCentering_Set_Throttle,"ParkingSpaceCentering_Set_Throttle");
klee_make_symbolic(&ParkingSpaceCentering_PSC_Enabled, sizeof ParkingSpaceCentering_PSC_Enabled,"ParkingSpaceCentering_PSC_Enabled");
klee_make_symbolic(&ParkingSpaceCentering_Enabled_PRNDL_In, sizeof ParkingSpaceCentering_Enabled_PRNDL_In,"ParkingSpaceCentering_Enabled_PRNDL_In");




}

void run(int * events, int N){
e = 0;
Error = 1;
ParkingSpaceCentering_Disabled = 0;
ParkingSpaceCentering_Enabled_Engaged_MoveLeft = 1;
ParkingSpaceCentering_Enabled_Engaged_Straight = 2;
ParkingSpaceCentering_Enabled_Disengaged = 3;
ParkingSpaceCentering_Enabled_Halt = 4;
ParkingSpaceCentering_Fail = 5;
ParkingSpaceCentering_Enabled_Engaged_MoveRight = 6;
ParkingSpaceCentering_Override = 7;
state = ParkingSpaceCentering_Disabled;
for (int i = 0; i < N; i++) {
event = events[i];
if ((state==ParkingSpaceCentering_Override)) {
            stuck_spec(!
(
(((false||(ParkingSpaceCentering_PSC_Enabled!=1))||true)||((ParkingSpaceCentering_BrakePedal==0)&&(ParkingSpaceCentering_AccelPedal==0))))
);
            non_det(((((false+(ParkingSpaceCentering_PSC_Enabled!=1))+true)+((ParkingSpaceCentering_BrakePedal==0)&&(ParkingSpaceCentering_AccelPedal==0)))>1));
if (((event==e)&&(ParkingSpaceCentering_PSC_Enabled!=1))) {
state = ParkingSpaceCentering_Disabled;
ParkingSpaceCentering_PSC_HVI = 0;
}
else {
}
if (((event==Error)&&true)) {
state = ParkingSpaceCentering_Fail;
ParkingSpaceCentering_PSC_HVI = 4;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_BrakePedal==0)&&(ParkingSpaceCentering_AccelPedal==0)))) {
state = ParkingSpaceCentering_Enabled_Disengaged;
ParkingSpaceCentering_PSC_HVI = 1;
}
else {
}
}
else {
if ((state==ParkingSpaceCentering_Enabled_Engaged_MoveRight)) {
                stuck_spec(!
(
((((((false||((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))||((ParkingSpaceCentering_Enabled_Speed==0)||((ParkingSpaceCentering_Enabled_Speed>5)||(ParkingSpaceCentering_Enabled_PRNDL_In!=3))))||true)||((ParkingSpaceCentering_FrontLine<5)&&(((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5)&&((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))))||((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))||(ParkingSpaceCentering_PSC_Enabled!=1)))
);
                non_det((((((((false+((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))+((ParkingSpaceCentering_Enabled_Speed==0)||((ParkingSpaceCentering_Enabled_Speed>5)||(ParkingSpaceCentering_Enabled_PRNDL_In!=3))))+true)+((ParkingSpaceCentering_FrontLine<5)&&(((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5)&&((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))))+((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))+(ParkingSpaceCentering_PSC_Enabled!=1))>1));
if (((event==e)&&((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))) {
state = ParkingSpaceCentering_Override;
ParkingSpaceCentering_PSC_HVI = 3;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_Enabled_Speed==0)||((ParkingSpaceCentering_Enabled_Speed>5)||(ParkingSpaceCentering_Enabled_PRNDL_In!=3))))) {
state = ParkingSpaceCentering_Enabled_Disengaged;
}
else {
}
if (((event==Error)&&true)) {
state = ParkingSpaceCentering_Fail;
ParkingSpaceCentering_PSC_HVI = 4;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_FrontLine<5)&&(((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5)&&((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))))) {
state = ParkingSpaceCentering_Enabled_Halt;
ParkingSpaceCentering_Set_Brake = 30;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))) {
state = ParkingSpaceCentering_Enabled_Engaged_Straight;
ParkingSpaceCentering_Set_Throttle = 20;
}
else {
}
if (((event==e)&&(ParkingSpaceCentering_PSC_Enabled!=1))) {
state = ParkingSpaceCentering_Disabled;
ParkingSpaceCentering_PSC_HVI = 0;
}
else {
}
}
else {
if ((state==ParkingSpaceCentering_Fail)) {
}
else {
if ((state==ParkingSpaceCentering_Enabled_Halt)) {
                        stuck_spec(!
(
((((false||(ParkingSpaceCentering_Enabled_Speed==0))||(ParkingSpaceCentering_PSC_Enabled!=1))||((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))||true))
);
                        non_det((((((false+(ParkingSpaceCentering_Enabled_Speed==0))+(ParkingSpaceCentering_PSC_Enabled!=1))+((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))+true)>1));
if (((event==e)&&(ParkingSpaceCentering_Enabled_Speed==0))) {
state = ParkingSpaceCentering_Enabled_Disengaged;
}
else {
}
if (((event==e)&&(ParkingSpaceCentering_PSC_Enabled!=1))) {
state = ParkingSpaceCentering_Disabled;
ParkingSpaceCentering_PSC_HVI = 0;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))) {
state = ParkingSpaceCentering_Override;
ParkingSpaceCentering_PSC_HVI = 3;
}
else {
}
if (((event==Error)&&true)) {
state = ParkingSpaceCentering_Fail;
ParkingSpaceCentering_PSC_HVI = 4;
}
else {
}
}
else {
if ((state==ParkingSpaceCentering_Enabled_Disengaged)) {
                            stuck_spec(!
(
((((false||((ParkingSpaceCentering_Enabled_Speed>0)&&((ParkingSpaceCentering_Enabled_Speed<=5)&&(ParkingSpaceCentering_Enabled_PRNDL_In==3))))||(ParkingSpaceCentering_PSC_Enabled!=1))||((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))||true))
);
                            non_det((((((false+((ParkingSpaceCentering_Enabled_Speed>0)&&((ParkingSpaceCentering_Enabled_Speed<=5)&&(ParkingSpaceCentering_Enabled_PRNDL_In==3))))+(ParkingSpaceCentering_PSC_Enabled!=1))+((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))+true)>1));
if (((event==e)&&((ParkingSpaceCentering_Enabled_Speed>0)&&((ParkingSpaceCentering_Enabled_Speed<=5)&&(ParkingSpaceCentering_Enabled_PRNDL_In==3))))) {
state = ParkingSpaceCentering_Enabled_Engaged_Straight;
}
else {
}
if (((event==e)&&(ParkingSpaceCentering_PSC_Enabled!=1))) {
state = ParkingSpaceCentering_Disabled;
ParkingSpaceCentering_PSC_HVI = 0;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))) {
state = ParkingSpaceCentering_Override;
ParkingSpaceCentering_PSC_HVI = 3;
}
else {
}
if (((event==Error)&&true)) {
state = ParkingSpaceCentering_Fail;
ParkingSpaceCentering_PSC_HVI = 4;
}
else {
}
}
else {
if ((state==ParkingSpaceCentering_Enabled_Engaged_Straight)) {
                                stuck_spec(!
(
(((((((false||(ParkingSpaceCentering_PSC_Enabled!=1))||((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)>5))||((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))||((ParkingSpaceCentering_FrontLine<5)&&(((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5)&&((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))))||((ParkingSpaceCentering_Enabled_Speed==0)||((ParkingSpaceCentering_Enabled_Speed>5)||(ParkingSpaceCentering_Enabled_PRNDL_In!=3))))||true)||((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)>5)))
);
                                non_det(((((((((false+(ParkingSpaceCentering_PSC_Enabled!=1))+((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)>5))+((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))+((ParkingSpaceCentering_FrontLine<5)&&(((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5)&&((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))))+((ParkingSpaceCentering_Enabled_Speed==0)||((ParkingSpaceCentering_Enabled_Speed>5)||(ParkingSpaceCentering_Enabled_PRNDL_In!=3))))+true)+((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)>5))>1));
if (((event==e)&&(ParkingSpaceCentering_PSC_Enabled!=1))) {
state = ParkingSpaceCentering_Disabled;
ParkingSpaceCentering_PSC_HVI = 0;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)>5))) {
state = ParkingSpaceCentering_Enabled_Engaged_MoveLeft;
ParkingSpaceCentering_Set_SteerOut = ParkingSpaceCentering_minusone;
ParkingSpaceCentering_Set_Throttle = 20;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))) {
state = ParkingSpaceCentering_Override;
ParkingSpaceCentering_PSC_HVI = 3;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_FrontLine<5)&&(((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5)&&((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))))) {
state = ParkingSpaceCentering_Enabled_Halt;
ParkingSpaceCentering_Set_Brake = 30;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_Enabled_Speed==0)||((ParkingSpaceCentering_Enabled_Speed>5)||(ParkingSpaceCentering_Enabled_PRNDL_In!=3))))) {
state = ParkingSpaceCentering_Enabled_Disengaged;
}
else {
}
if (((event==Error)&&true)) {
state = ParkingSpaceCentering_Fail;
ParkingSpaceCentering_PSC_HVI = 4;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)>5))) {
state = ParkingSpaceCentering_Enabled_Engaged_MoveRight;
ParkingSpaceCentering_Set_SteerOut = 1;
ParkingSpaceCentering_Set_Throttle = 20;
}
else {
}
}
else {
if ((state==ParkingSpaceCentering_Enabled_Engaged_MoveLeft)) {
                                    stuck_spec(!
(
((((((false||((ParkingSpaceCentering_FrontLine<5)&&(((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5)&&((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))))||((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5))||true)||((ParkingSpaceCentering_Enabled_Speed==0)||((ParkingSpaceCentering_Enabled_Speed>5)||(ParkingSpaceCentering_Enabled_PRNDL_In!=3))))||((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))||(ParkingSpaceCentering_PSC_Enabled!=1)))
);
                                    non_det((((((((false+((ParkingSpaceCentering_FrontLine<5)&&(((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5)&&((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))))+((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5))+true)+((ParkingSpaceCentering_Enabled_Speed==0)||((ParkingSpaceCentering_Enabled_Speed>5)||(ParkingSpaceCentering_Enabled_PRNDL_In!=3))))+((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))+(ParkingSpaceCentering_PSC_Enabled!=1))>1));
if (((event==e)&&((ParkingSpaceCentering_FrontLine<5)&&(((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5)&&((ParkingSpaceCentering_RightLine-ParkingSpaceCentering_LeftLine)<5))))) {
state = ParkingSpaceCentering_Enabled_Halt;
ParkingSpaceCentering_Set_Brake = 30;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_LeftLine-ParkingSpaceCentering_RightLine)<5))) {
state = ParkingSpaceCentering_Enabled_Engaged_Straight;
ParkingSpaceCentering_Set_Throttle = 20;
}
else {
}
if (((event==Error)&&true)) {
state = ParkingSpaceCentering_Fail;
ParkingSpaceCentering_PSC_HVI = 4;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_Enabled_Speed==0)||((ParkingSpaceCentering_Enabled_Speed>5)||(ParkingSpaceCentering_Enabled_PRNDL_In!=3))))) {
state = ParkingSpaceCentering_Enabled_Disengaged;
}
else {
}
if (((event==e)&&((ParkingSpaceCentering_BrakePedal>0)||(ParkingSpaceCentering_AccelPedal>0)))) {
state = ParkingSpaceCentering_Override;
ParkingSpaceCentering_PSC_HVI = 3;
}
else {
}
if (((event==e)&&(ParkingSpaceCentering_PSC_Enabled!=1))) {
state = ParkingSpaceCentering_Disabled;
ParkingSpaceCentering_PSC_HVI = 0;
}
else {
}
}
else {
if ((state==ParkingSpaceCentering_Disabled)) {
                                        stuck_spec(!
(
(false||(ParkingSpaceCentering_PSC_Enabled==1)))
);
if (((event==e)&&(ParkingSpaceCentering_PSC_Enabled==1))) {
state = ParkingSpaceCentering_Enabled_Disengaged;
ParkingSpaceCentering_PSC_HVI = 1;
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
