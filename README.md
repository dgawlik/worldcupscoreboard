# worldcupscoreboard

### Assignment

You are working in a sports data company, and we would like you to develop a new Live Football
World Cup Scoreboard library that shows all the ongoing matches and their scores.
The scoreboard supports the following operations:
1. Start a new match, assuming initial score 0 – 0 and adding it the scoreboard.
   This should capture following parameters:
   a. Home team
   b. Away team
2. Update score. This should receive a pair of absolute scores: home team score and away
   team score.
3. Finish match currently in progress. This removes a match from the scoreboard.
4. Get a summary of matches in progress ordered by their total score. The matches with the
   same total score will be returned ordered by the most recently started match in the
   scoreboard.

### Discussion

**accomodating for the mistakes**

what will we do if the match is removed by mistake? Or score is updated with wrong results? 
scoreboard IMO should provide facility to restore it's state from back history. 

**concurrent usage**

the order in which the games are started matters and we should synchronize access to update, start
and finish methods

**easy access to match**

in many places i have seen convenient notion of generating alphanumeric ids for variuos objects,
this should be better to refer to match by such id, generating class will be provided

**presenting matches in order**

they can be just sorted at all times when stored in TreeMap / TreeSet, i think it's the easiest solution

**immutability / event sourcing**

i think i would be cool if scoreboard was immutable with respect to update, start, finish methods, they
would generate a new concurrency friendly versions. we could store older scoreboards in linkedlist and 
have something alike eventsourcing

no i think that this is overkill, with many matches run and updated concurrently there might be too
much of performance penalty, method snapshot() will likely suffice


