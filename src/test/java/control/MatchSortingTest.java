package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.Match;

public class MatchSortingTest {
    /** Checks score ordering, ties, empty results, and undefined scores. */
    public static void main(String[] args) {
        ListInterface<Match> matches = new ArrayList<>();
        Match low = new Match(null, null, 0.2);
        Match high = new Match(null, null, 0.9);
        Match tied = new Match(null, null, 0.9);
        matches.add(low); matches.add(high); matches.add(tied);
        MatchingEngine engine = new MatchingEngine();
        String output = engine.listAllMatches(matches);
        if (matches.get(0) != high || matches.get(1) != tied || matches.get(2) != low) {
            throw new AssertionError("Matches must be highest-score first, preserving tied order");
        }
        if (output.indexOf("0.9") > output.indexOf("0.2")) {
            throw new AssertionError("Display order must match selection order");
        }
        if (!engine.listAllMatches(new ArrayList<>()).equals("No matching jobs found.")) {
            throw new AssertionError("Empty-match display changed");
        }
        Match undefined = new Match(null, null, Double.NaN);
        matches.add(undefined);
        matches.add(new Match(null, null, 0.5));
        engine.listAllMatches(matches);
        if (matches.get(4) != undefined || matches.get(2).getScore() != 0.5) {
            throw new AssertionError("Undefined scores must not outrank valid scores");
        }
        System.out.println("Match sorting: 4 checks passed");
    }
}
