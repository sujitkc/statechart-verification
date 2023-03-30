#Automotive Dataset
A model of UWFMS - University of Waterloo Feature Model Set has been taken as reference to create these StaBL models present in this folder.
Each file represents one of the features as specified in the Non-Proprietary Automotive Feature Set: UWFMS (that references the features from https://www.trwaftermarket.com/en/). This attempt of creating the StaBL equivalent model takes in principle the same structural elements of the STATEFLOW models given and localize the scope of variables as per the actual access. We intend to use these for running the constabl simulator.

#Some pointers:
1. The stateflow has signal at a cycle unit for the Stateflow model when no other event is generated - we model it as an event "No_event"
2. For transitions where stateflow does not have any event, we model it as an event "e"
3. Stabl has a root state of type statechart and each feature model that represents concurrent subsystems (orthogonality) is modelled via a shell state.
4. For transitions where multiple events are mentioned in stateflow - it has been specified as two different transitions
	for instance, if the stateflow model is of the format - t1:e1|e2[g]/a - the corresponding constabl format is t1_1:e1[g]/a and t1_2:e2[g]/a
5. There are no priorities defined in constabl transitions
	
#subsystems

1. Cruise control - TRW’s Adaptive Cruise Control (ACC) technology improves upon standard cruise control by automatically adjusting the vehicle speed and distance to that of a target vehicle. ACC uses a long range radar sensor to detect a target vehicle up to 200 meters in front and automatically adjusts the ACC vehicle speed and gap accordingly. ACC decelerates or accelerates the vehicle according to the
desired speed and distance settings established by the driver. As per standard cruise control, the driver can override the system at any time

2. Collision Avoidance - TRW’s Collision Warning (CW) System can assist drivers by helping to prevent or mitigate accidents. Combining long and short range radars with a video camera, TRW’s Collision Warning monitors the road ahead (including part of the side-fronts). In the event that a vehicle or obstacle approaches, TRW’s collision warning system can notify the driver of a possible collision through audible or visual alerts and can also provide braking force as soon as an imminent collision is detected

4. Lane Guide - TRW’s Lane Guide Departure (LG) System supports the driver and assists in preventing unintentional lane departures. Utilizing a forward-looking video camera that continuously monitors the vehicle’s lane, the system can determine whether or not a driver is unintentionally drifting from the lane or the road. If the driver unintentionally begins to wander out of their lane, the system alerts
the driver

