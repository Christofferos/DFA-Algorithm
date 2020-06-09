/* Written by: Kristopher Werlinder. Date: 2020-04-01. */

import java.util.List;

public class Main {
	public static void main(String[] args) {
		Kattio IO = new Kattio(System.in, System.out);
		DFA automaton = new DFA(IO.getInt(), IO.getInt());
		int accept = IO.getInt();
		for (int i = 0; i < accept; ++i)
			automaton.setAccepting(IO.getInt());
		int transitions = IO.getInt();
		for (int i = 0; i < transitions; ++i) {
			int from = IO.getInt();
			int to = IO.getInt();
			String sym = IO.getWord();
			char lo = sym.charAt(0);
			char hi = sym.charAt(sym.length() - 1);
			for (char x = lo; x <= hi; ++x)
				automaton.addTransition(from, to, x);
		}
		int bound = IO.getInt();
		List<String> strings = automaton.getAcceptingStrings(bound);
		IO.println(strings.size());
		for (String s : strings) {
			IO.println(s);
		}
		IO.close();
	}
}
