package utility;


public class SearchUtil {


    /** Converts null to empty text, trims edges, lowercases, and collapses whitespace for title matching. */
    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    // Returns category, edit distance and normalization length, in that order.
    /**
     * Classifies title relevance: 0 exact, 1 substring, 2 close typo, or 3 no match.
     * Fuzzy matching checks contiguous title-word windows and allows 1-3 edits based on query length;
     * queries shorter than three characters skip fuzzy matching.
     * @return category, best edit distance, and normalization length as a three-value array
     */
    private static int[] relevance(String query, String title) {
        String q = normalize(query), t = normalize(title);
        if (q.isEmpty() || t.isEmpty()) return new int[] {3, 0, 1};
        if (q.equals(t)) return new int[] {0, 0, 1};
        if (t.contains(q)) return new int[] {1, 0, 1};
        if (q.length() < 3) return new int[] {3, 0, 1};
        int cutoff = Math.min(3, Math.max(1, q.length() / 4));
        String[] words = t.split(" ");
        int wordCount = q.split(" ").length;
        int bestDistance = Integer.MAX_VALUE, bestLength = 1;
        for (int start = 0; start < words.length; start++) {
            String window = "";
            for (int end = start; end < words.length && end < start + wordCount; end++) {
                window += (end == start ? "" : " ") + words[end];
                if (end - start + 1 != wordCount) continue;
                int distance = levenshteinDistance(q, window);
                int length = Math.max(q.length(), window.length());
                if (distance <= cutoff && (bestDistance == Integer.MAX_VALUE
                        || (long) distance * bestLength < (long) bestDistance * length)) {
                    bestDistance = distance;
                    bestLength = length;
                }
            }
        }
        return bestDistance == Integer.MAX_VALUE ? new int[] {3, 0, 1}
                : new int[] {2, bestDistance, bestLength};
    }

    /**
     * Reports whether a normalized title is an exact, substring, or automatically tolerated typo match.
     * Blank queries and missing titles return false; no user-entered edit distance is needed.
     */
    public static boolean matchesJobTitle(String query, String title) {
        return relevance(query, title)[0] < 3;
    }

    /**
     * Ranks two titles for a query: exact before substring before typo before no match.
     * Within the typo category, lower normalized edit distance ranks first.
     * Cross-multiplication compares distance/length ratios without floating-point division.
     * @return negative when first is more relevant, zero for a relevance tie, positive otherwise
     */
    public static int compareJobTitles(String query, String first, String second) {
        int[] a = relevance(query, first), b = relevance(query, second);
        if (a[0] != b[0]) return a[0] < b[0] ? -1 : 1;
        long left = (long) a[1] * b[2], right = (long) b[1] * a[2];
        return left < right ? -1 : left > right ? 1 : 0;
    }

    /**
     * Calculates the minimum insertions, deletions, or substitutions between two case-sensitive strings.
     * The dynamic-programming matrix uses O(input.length * target.length) time and storage.
     * @param input first string, including empty text
     * @param target second string, including empty text
     * @return zero for identical strings, otherwise the minimum edit count
     * @throws IllegalArgumentException if either string is null
     */
    public static int levenshteinDistance(String input, String target) {
        if (input == null || target == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int[][] dp = new int[input.length() + 1][target.length() + 1];

        for (int i = 0; i <= input.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= target.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= input.length(); i++) {
            for (int j = 1; j <= target.length(); j++) {
                int cost = (input.charAt(i - 1) == target.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost);
            }
        }

        return dp[input.length()][target.length()];
    }

    /**
     * Checks whether the case-sensitive edit distance is within an explicit internal threshold.
     * Unlike automatic job-title matching, this helper does not trim or normalize its arguments.
     * @param input first string
     * @param target second string
     * @param threshold maximum accepted number of edits
     * @return false for null input or a distance above the threshold
     */
    public static boolean fuzzySearch(String input, String target, int threshold) {
        if (input == null || target == null) {
            return false;
        }
        int distance = levenshteinDistance(input, target);
        return distance <= threshold;
    }
}