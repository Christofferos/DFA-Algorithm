
/* Written by: Kristopher Werlinder. Date: 2020-04-01. */

import java.util.*;

public class DFA {

  public static class Path {
    private List<Integer> lst;

    public Path() {
      this.lst = new ArrayList<>();
    }

    private Path(List<Integer> l) {
      this.lst = new ArrayList<>(l);
    }

    private Path(Path p) {
      this.lst = new ArrayList<Integer>(p.lst);
    }

    public boolean isEmpty() {
      return lst.isEmpty();
    }

    public Integer first() {
      return lst.get(0);
    }

    public Path rest() {
      return new Path(lst.subList(1, lst.size()));
    }

    public Integer last() {
      return lst.get(lst.size() - 1);
    }

    public Integer len() {
      return lst.size() - 1;
    }

    public String toString() {
      return lst.toString();
    }

    public Path plus(Integer state) {
      Path retval = new Path(this);
      retval.lst.add(state);
      return retval;
    }

    public int hashCode() {
      return lst.hashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null)
        return false;
      if (getClass() != o.getClass())
        return false;
      Path other = (Path) o;
      return lst.equals(other.lst);
    }
  }

  private DFA_Data dfa;
  private int numRequestedWords;

  private final int MAX_WORD_LENGTH = 5000;
  /* paths STRUCTURE: */
  // First Integer: Path length.
  // Second Integer: Start state of the path (if it is the start state the paths will be accepted).
  // Set<Path>: Set of unique paths.
  private Map<Integer, Map<Integer, Set<Path>>> paths = new HashMap<>();
  private HashSet<String> words = new HashSet<>();

  ////////////////////////////////////////////////////////////////////////////

  public DFA(int stateCount, int startState) {
    dfa = new DFA_Data(stateCount, startState);
  }

  public void setAccepting(int state) {
    dfa.addAcceptingState(state);
  }

  public void addTransition(int from, int to, char c) {
    dfa.addTransition(from, to, c);
  }

  /* ### GetAcceptingStrings [Returns a sorted list of all generated words that are accepted by a given DFA]. ### */
  public List<String> getAcceptingStrings(int numRequestedWords) {
    this.numRequestedWords = numRequestedWords;
    int lastLengthAtWhichWeFoundNewWords = 0;
    for (int pathLen = 0; pathLen <= MAX_WORD_LENGTH; pathLen++) {
      int numWordsBefore = words.size();
      for (int acceptingState : dfa.acceptingStates()) {
        // Begins in an accepting state and backtracks until we reach the start state
        enumAllAcceptingPaths(pathLen, acceptingState);
        if (words.size() == numRequestedWords) {
          break;
        }
      }
      int numWordsAfter = words.size();
      int numNewWordsFound = numWordsAfter - numWordsBefore;
      if (numNewWordsFound > 0) {
        lastLengthAtWhichWeFoundNewWords = pathLen;
      } else {
        if (pathLen - lastLengthAtWhichWeFoundNewWords >= dfa.numStates) {
          break;
        }
      }
      if (words.size() == numRequestedWords) {
        break;
      }
    }
    ArrayList<String> listOfWords = new ArrayList<String>(words);
    Collections.sort(listOfWords);
    return listOfWords;
  }

  ////////////////////////////////////////////////////////////////////////////

  /* ### AddPath - [Generates all words for a given path using dfs]. ### */
  private void addPath(Path path) {
    int pathLen = path.len();
    int endingIn = path.last();

    Map<Integer, Set<Path>> pathsOfLen = paths.get(pathLen);
    if (pathsOfLen == null) {
      paths.put(pathLen, new HashMap<>());
      pathsOfLen = paths.get(pathLen);
    }
    Set<Path> pathsOfLenEndingIn = pathsOfLen.get(endingIn);
    if (pathsOfLenEndingIn == null) {
      pathsOfLen.put(endingIn, new HashSet<>());
      pathsOfLenEndingIn = pathsOfLen.get(endingIn);
    }
    if (pathsOfLenEndingIn.contains(path)) {
      return;
    }
    // Add path into the inner structure in "paths" 
    pathsOfLenEndingIn.add(path);
    //System.out.println("pathsOfLenEndingIn " + pathsOfLenEndingIn);
    if (dfa.isAcceptingState(endingIn)) {
      System.out.println("Found path of length " + pathLen);
      generateAllWordsForPathDfs(path);
    }
  }

  /* ### EnumAllAcceptingPaths ### */
  private void enumAllAcceptingPaths(int ofLen, int endingIn) {

    if (paths.get(ofLen) != null && paths.get(ofLen).get(endingIn) != null) {
      return;
    }

    if (ofLen == 0) {
      if (endingIn == dfa.startingState) {
        Path path = new Path().plus(endingIn);
        addPath(path);
      }
      return;
    }

    for (Integer prevState : dfa.statesLeadingTo(endingIn)) {
      enumAllAcceptingPaths(ofLen - 1, prevState);
      Map<Integer, Set<Path>> pathsOfLen = paths.get(ofLen - 1);
      if (pathsOfLen == null) {
        continue;
      }
      Set<Path> pathsToPrevState = pathsOfLen.get(prevState);
      if (pathsToPrevState == null) {
        continue;
      }
      for (Path path : pathsToPrevState) {
        addPath(path.plus(endingIn));
        if (words.size() == numRequestedWords) {
          return;
        }
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////

  private static class GenWordsQueueItem {
    Path path;
    String wordSoFar;

    public GenWordsQueueItem(Path path, String wordSoFar) {
      this.path = path;
      this.wordSoFar = wordSoFar;
    }
  }

  /* ### GenerateAllWordsForPathDfs ### */
  private void generateAllWordsForPathDfs(Path _path) {
    Deque<GenWordsQueueItem> stack = new ArrayDeque<GenWordsQueueItem>();

    stack.add(new GenWordsQueueItem(_path, ""));

    while (!stack.isEmpty() && words.size() < numRequestedWords) {
      GenWordsQueueItem queueItem = stack.removeLast();

      Integer pathStartingState = queueItem.path.first();
      Path restOfPath = queueItem.path.rest();

      if (restOfPath.isEmpty()) {
        if (!words.contains(queueItem.wordSoFar)) {
          words.add(queueItem.wordSoFar);
        }
        continue;
      }

      Integer pathNextState = restOfPath.first();

      for (DFA_Data.Transition t : dfa.transitionsFromTo(pathStartingState, pathNextState)) {
        stack.add(new GenWordsQueueItem(restOfPath, queueItem.wordSoFar + t.inputChar()));
      }
    }
  }
  ////////////////////////////////////////////////////////////////////////////
}
