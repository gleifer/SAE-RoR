========================================

# README file -- AVRoR (07.02.2021)

--------------------------------

## Improvements for next versions:

#1. Add recursive reasoning rule that actually makes the agent searches the whole grid, and not only the first position.

#2. Add dynamic generation of beliefs. 

#3. ...

................................

## The files:

<backup-av-agent.gwen>
<backup-RoadJunctionEnv.java>

> represent the AVRoR version for JSAN/AREA paper, but the version containing:
> some pieces of code that have been removed from the final version.

1.) code for actions "approach", "move_road_user".

2.) at Gwen code:

:Reasoning Rules:

to_check(Z,W) :- sign(2,0), ~stopped, ~given_away; 

...

// when road junction is busy
+for_road_users(S,T) : { B road_user(S,T) } <- +busy_roadjunction, wait, -to_watch(S,T);
>> removido: "-to_watch(S,T)"


========================================
