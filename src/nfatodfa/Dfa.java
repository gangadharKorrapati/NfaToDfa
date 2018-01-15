/* 
 * The MIT License
 *
 * Copyright 2018 "gangadhar";.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nfatodfa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author "gangadhar"
 */
public class Dfa {

    private final ArrayList<Integer> endStates;
    private final Set<Character> transVals;
    private final Integer[][] transitiontable;

    public Dfa(Integer[][] trans, ArrayList<Integer> es, Set<Character> tv) {
        endStates = es;
        transVals = tv;
        transitiontable = trans;
    }

    public Dfa(Nfa nfa) {
        transVals = new HashSet<>(nfa.gettransVal());
        HashMap< Integer, Set< Integer>> subsets = new HashMap<>();
        Set< Integer> newSubset;
        Set< Integer> currentSubset;
        int completed = 0;
        subsets.put(subsets.size(), epsilonclosure(0,nfa));
        while (subsets.size() - completed > 0) {
            currentSubset = subsets.get(completed);
            for (char a : transVals) {
                newSubset = multipleTransitions(currentSubset,a,nfa);
                if (!(newSubset.isEmpty() || subsets.containsValue(newSubset))) {
                    subsets.put(subsets.size(), newSubset);
                }
            }
            completed++;
        }
        transitiontable = new Integer[subsets.size()][transVals.size()];
        int j;
        for (int i = 0; i < subsets.size(); i++) {
            j = 0;
            currentSubset = subsets.get(i);
            for (char a : transVals) {
                newSubset = multipleTransitions(currentSubset,a,nfa);
                if (subsets.containsValue(newSubset)) {
                    transitiontable[i][j++] = getkey(subsets, newSubset);
                } else {
                    transitiontable[i][j++] = -1;
                }
            }
        }
        endStates = new ArrayList<>();
        for (int i = 0; i < subsets.size(); i++) {
            for (Integer k : nfa.getEndStates()) {
                if (subsets.get(i).contains(k)) {
                    endStates.add(i);
                }
            }
        }
    }

    private static Integer getkey(HashMap< Integer, Set< Integer>> states, Set< Integer> newStateSet) {
        int i;

        for (i = 0; i < states.size(); i++) {
            if (newStateSet.equals(states.get(i))) {
                return i;
            }
        }

        return i;
    }

    void display() {
        System.out.println("\n************************Dfa************************");
        System.out.println("\nStart state:\n0");
        System.out.println("Final States are :");
        for(Integer i:endStates){
                    System.out.println(i+"\t");
        }
        for (char c: transVals)
            System.out.print("\t"+c);
        System.out.println("\n");
        for (int row = 0; row < transitiontable.length; ++row) {
            System.out.print(row);
            for (int col = 0; col < transitiontable[row].length; ++col) {
                if (transitiontable[row][col] == -1)
                    System.out.print("\tT");
                else
                    System.out.print("\t" + transitiontable[row][col]);
            }
            System.out.println("\n");
        }
    }
    private static Set < Integer > epsilonclosure(int state,Nfa nfa) {
        Set < Integer > setofstates = new TreeSet < > ();
        ArrayList < Integer > stateArray = new ArrayList < > ();
        stateArray.add(state);
        int i = 0;
        int completed = 0;
        while (stateArray.size() - completed > 0) {
            state = stateArray.get(completed);
            for (Edge e:nfa.getEdges()) {
                if (e.getTransVal() == 'e' && e.getFromState()==state) {
                    if (!stateArray.contains(e.getToState())) {
                        stateArray.add(e.getToState());
                    }
                }
            }
            completed++;
        }
        setofstates.addAll(stateArray);
        return setofstates;
    }
    
    private static Set < Integer > transition(int newstate, char symbol,Nfa nfa) {
        Set < Integer > s = new TreeSet < > ();
        for (Edge e:nfa.getEdges()) {
                if (e.getTransVal() == symbol && e.getFromState()==newstate) {
                s.add(e.getToState());
                }
            }
        return s;
    }

    private static Set < Integer > epsilonclosureofset(Set < Integer > stateSet,Nfa nfa) {
        Set < Integer > resultStateSet = new TreeSet < > ();
        resultStateSet.clear();
        for (int i: stateSet) {
            resultStateSet.addAll(epsilonclosure(i,nfa));
        }
        return resultStateSet;

    }

    private static Set < Integer > multipleTransitions(Set < Integer > states, char a,Nfa nfa) {
        Set < Integer > newset = new TreeSet < > ();
        for (int i: states) {
            newset.addAll(transition(i, a,nfa));
        }
        return epsilonclosureofset(newset,nfa);
    }
    
}
