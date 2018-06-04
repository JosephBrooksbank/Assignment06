/**
 * Really small class to enable/disable "debugging" strings, for ease of use when testing
 *
 * @author Joseph Brooksbank
 * @version 6/3/2018
 * <p>
 * To whoever grades this: Have a good summer!
 */
class CheatStrings {
    private boolean isCheating;

    CheatStrings(boolean isCheating) {
        this.isCheating = isCheating;
    }

    void out(String output) {
        if (isCheating) {
            System.out.println(output);
        }
    }
}
