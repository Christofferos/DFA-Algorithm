
/* Written by: Kristopher Werlinder. Date: 2020-04-01. */

import java.util.*;

public class DFA_Data {

    public static class Transition extends Pair<Integer, Character> {
        public Transition(Integer toState, Character inputChar) {
            super(toState, inputChar);
        }

        public Integer toState() {
            return first;
        }

        public Character inputChar() {
            return second;
        }
    }

    public final int numStates;
    public int numTransitions;
    public final int startingState;
    private List<Integer> _acceptingStatesList;
    private Set<Integer> _acceptingStatesHash;
    private List<List<Transition>> _transitionsFrom;
    private List<List<Transition>> _transitionsTo;
    private List<List<List<Transition>>> _transitionsFromTo;
    private List<Set<Integer>> _nextStatesFrom;
    private List<Set<Integer>> _statesLeadingTo;

    public DFA_Data(int numStates, int startingState) {
        this.numStates = numStates;
        this.startingState = startingState;
        this._acceptingStatesList = new ArrayList<Integer>();
        this._acceptingStatesHash = new HashSet<Integer>();
        this._transitionsFrom = new ArrayList<>(numStates);
        this._transitionsTo = new ArrayList<>(numStates);
        this._transitionsFromTo = new ArrayList<>(numStates);
        this._nextStatesFrom = new ArrayList<>(numStates);
        this._statesLeadingTo = new ArrayList<>(numStates);
        for (int i = 0; i < numStates; ++i) {
            _transitionsFrom.add(new ArrayList<>());
            _transitionsTo.add(new ArrayList<>());

            List<List<Transition>> t = new ArrayList<List<Transition>>(numStates);
            for (int j = 0; j < numStates; ++j)
                t.add(new ArrayList<Transition>());
            _transitionsFromTo.add(t);

            _nextStatesFrom.add(new HashSet<>());
            _statesLeadingTo.add(new HashSet<>());
        }
    }

    public void addAcceptingState(int state) {
        _acceptingStatesList.add(state);
        _acceptingStatesHash.add(state);
    }

    public boolean isAcceptingState(int state) {
        return _acceptingStatesHash.contains(state);
    }

    public Iterable<Integer> acceptingStates() {
        return _acceptingStatesList;
    }

    public void addTransition(int fromState, int toState, char c) {
        numTransitions++;
        Transition t = new Transition(toState, Character.valueOf(c));
        _transitionsFrom.get(fromState).add(t);
        _transitionsTo.get(toState).add(t);
        _transitionsFromTo.get(fromState).get(toState).add(t);
        _nextStatesFrom.get(fromState).add(toState);
        _statesLeadingTo.get(toState).add(fromState);
    }

    public Iterable<Transition> transitionsFrom(int state) {
        return _transitionsFrom.get(state);
    }

    public Iterable<Transition> transitionsTo(int state) {
        return _transitionsTo.get(state);
    }

    public Iterable<Transition> transitionsFromTo(int fromState, int toState) {
        return _transitionsFromTo.get(fromState).get(toState);
    }

    public Iterable<Integer> nextStatesFrom(int state) {
        return _nextStatesFrom.get(state);
    }

    public Iterable<Integer> statesLeadingTo(int state) {
        return _statesLeadingTo.get(state);
    }

}
