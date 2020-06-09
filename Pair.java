/* Written by: Kristopher Werlinder. Date: 2020-04-01. */

public class Pair<A, B> {
	public final A first;
	public final B second;

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	@Override
	final public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Pair<A, B> other = (Pair<A, B>) o;
		return first.equals(other.first) && second.equals(other.second);
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}
}
