# Automotive Dataset
A model of UWFMS - University of Waterloo Feature Model Set has been taken as reference to create these StaBL models present in this folder.
Each file represents one of the features as specified in the Non-Proprietary Automotive Feature Set: UWFMS (that references the features from https://www.trwaftermarket.com/en/). This attempt of creating the StaBL equivalent model takes in principle the same structural elements of the STATEFLOW models given and localize the scope of variables as per the actual access. We intend to use these for running the constabl simulator.

# Some pointers:
1. The stateflow has signal at a cycle unit for the Stateflow model when no other event is generated - we model it as an event "No_event"
2. For transitions where stateflow does not have any event, we model it as an event "e"
3. Stabl has a root state of type statechart and each feature model that represents concurrent subsystems (orthogonality) is modelled via a shell state.
4. For transitions where multiple events are mentioned in stateflow - it has been specified as two different transitions
	for instance, if the stateflow model is of the format - t1:e1|e2[g]/a - the corresponding constabl format is t1_1:e1[g]/a and t1_2:e2[g]/a
5. There are no priorities defined in constabl transitions
6. Run **make simulator2-testcasestudy**

# Files to use

1. CruiseControl.stb [has a shell state]
2. CollisionAvoidance.stb  
3. ParkAssist.stb    
4. LaneGuide.stb   
5. EmergencyVehicleAvoidance.stb  
6. ParkingSpaceCentering.stb
7. ReversingAssistance.stb

	
# Subsystems

1. **Cruise control (CC)** - TRW’s Adaptive Cruise Control (ACC) technology improves upon standard cruise control by automatically adjusting the vehicle speed and distance to that of a target vehicle. ACC uses a long range radar sensor to detect a target vehicle up to 200 meters in front and automatically adjusts the ACC vehicle speed and gap accordingly. ACC decelerates or accelerates the vehicle according to the
desired speed and distance settings established by the driver. As per standard cruise control, the driver can override the system at any time

2. **Collision Avoidance (CA)** - TRW’s Collision Warning (CW) System can assist drivers by helping to prevent or mitigate accidents. Combining long and short range radars with a video camera, TRW’s Collision Warning monitors the road ahead (including part of the side-fronts). In the event that a vehicle or obstacle approaches, TRW’s collision warning system can notify the driver of a possible collision through audible or visual alerts and can also provide braking force as soon as an imminent collision is detected

3. The TRW **Park Assist (PA)** system combines electrically powered steering with environmental sensing to aid drivers during parallel parking maneuvers. The system uses short range radar sensors to evaluate the length of the parking slot. From this information, the steering trajectory is calculated and the proper steering angle is automatically chosen. The driver monitors the steering.

4. TRW’s **Lane Guide (LG)** Departure System supports the driver and assists in preventing unintentional lane departures. Utilizing a forward-looking video camera that continuously monitors the vehicle’s lane, the system can determine whether or not a driver is unintentionally drifting from the lane or the road. If the driver unintentionally begins to wander out of their lane, the system alerts
the driver

5. The **Emergency Vehicle Avoidance (EVA)** feature assists drivers by pulling over the vehicle in situations when an emergency vehicle, which makes use of a siren, needs the road to be cleared. Combining long and short range radars with a sound detector, and the use of a GPS device, this feature determines when and where the vehicle needs to pull over. When a siren is detected the vehicle should slow and pull to the right-side of the road, and stop if the vehicle is in a safe location. If at an unsafe location (e.g., in the middle of an
intersection), the vehicle will coast until it is safe to continue the stop procedure.

6. The **Parking Space Centering (PSC)** feature assists drivers during perpendicular parking maneuvers. I assumed that the vehicle is already at a valid parking space; an error signal is sent if it is detected that this is not the case. The feature uses short range radar sensors to determine the location of the vehicle within the box.

7. The **Reversing Assistant (RA)** feature can assist drivers by helping to prevent or mitigate collisions while reversing. Combining long and short range radars, RA monitors the path of the vehicle while reversing. In the event that a vehicle or obstacle approaches, RA notifies the driver of a possible collision and also brakes as soon as the threat of a collision becomes imminent
