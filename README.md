# NfaToDfa
Objective: learner will be able to implement partitioning; set union operation in
turn minimizes the number of states in the given DFA.
<br>
Prerequisite: learner should be able to apply Hopcroft's algorithm
<br>
Pre-lab exercise: implement the set union operation on a set of sets.
<br>
Procedure – Hopcrofts Algorithm :
<br>
1. Initially start with two partitions of states: the set of all final, and the non-final
states.
<br>
2. Repeat
<br>
{ For each partition updated by the previous iteration,
<br>

Examine the transitions for each state on each input symbol
<br>

If any two states in a partition leading to different partitions on same symbol
<br>

Further divide and update the partitions
<br>

} until (no new partition is created)
