
/* Written by: Kristopher Werlinder. Date: 2020-04-01. */

import java.util.*;

public class GenerateGraph {

    static String getAlphaNumericString(int n) {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Random rand = new Random();
        int stateCount = 5; // rand.nextInt(49) + 1
        System.out.print(stateCount + " ");
        int startState = rand.nextInt(stateCount);
        System.out.println(startState);

        int acceptStates = 1; // rand.nextInt(stateCount - 1) + 1
        System.out.print(acceptStates + " ");
        int setAccepting;
        for (int i = 0; i < acceptStates; ++i) {
            setAccepting = rand.nextInt(stateCount);
            while (setAccepting == startState) {
                setAccepting = rand.nextInt(stateCount);
            }
            System.out.print(setAccepting + " ");
        }
        System.out.println();
        int transitions = 50; // rand.nextInt(50)
        System.out.println(transitions);
        for (int i = 0; i < transitions; ++i) {
            int transitionWordSize = 1;
            int from = rand.nextInt(stateCount);
            int to = rand.nextInt(stateCount);
            String sym = getAlphaNumericString(transitionWordSize);
            /*
             * if (i < 5) { from = startState; }
             */
            System.out.print(from + " " + to + " " + sym);
            System.out.println();
            // }

        }
        int bound = rand.nextInt(50);
        System.out.println(bound);
    }
}