import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class AutomatonImpl implements Automaton {

    class StateLabelPair {
        int state;
        char label;

        public StateLabelPair(int state_, char label_) {
            state = state_;
            label = label_;
        }

        @Override
        public int hashCode() {
            return Objects.hash((Integer) state, (Character) label);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof StateLabelPair)) {
                return false;
            }
            StateLabelPair o1 = (StateLabelPair) o;
            return (state == o1.state) && (label == o1.label);
        }
    }

    HashSet<Integer> start_states;
    HashSet<Integer> accept_states;
    HashSet<Integer> current_states;
    HashMap<StateLabelPair, HashSet<Integer>> transitions;

    public AutomatonImpl() {
        start_states = new HashSet<Integer>();
        accept_states = new HashSet<Integer>();
        current_states = new HashSet<Integer>();
        transitions = new HashMap<StateLabelPair, HashSet<Integer>>();
    }

    @Override
    public void addState(int s, boolean is_start, boolean is_accept) {
        // The existence of a state is implicit in these sets.
        if (is_start) {
            start_states.add(s);
        }
        if (is_accept) {
            accept_states.add(s);
        }
    }

    @Override
    public void addTransition(int s_initial, char label, int s_final) {
        StateLabelPair key = new StateLabelPair(s_initial, label);
        HashSet<Integer> dests = transitions.get(key);
        if (dests == null) {
            dests = new HashSet<Integer>();
            transitions.put(key, dests);
        }
        dests.add(s_final);
    }

    @Override
    public void reset() {
        current_states.clear();
        current_states.addAll(start_states);
    }

    @Override
    public void apply(char input) {
        HashSet<Integer> next_states = new HashSet<Integer>();
        for (Integer s : current_states) {
            StateLabelPair key = new StateLabelPair(s, input);
            HashSet<Integer> dests = transitions.get(key);
            if (dests != null) {
                next_states.addAll(dests);
            }
        }
        current_states = next_states;
    }

    @Override
    public boolean accepts() {
        for (Integer s : current_states) {
            if (accept_states.contains(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTransitions(char label) {
        for (Integer s : current_states) {
            StateLabelPair key = new StateLabelPair(s, label);
            if (transitions.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

}
