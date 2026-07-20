package ph.edu.dlsu.lbycpob.gradetracker.util;

// ============================================================
// IDVerifier.java
// NOTEs:
//   The original class had two layers:
//     (a) Static pure methods  -- isValidID(), getIDRole(),
//         validateID(), calculateDotProduct()   (NO CHANGES)
//     (b) Instance method verifyID()            (REMOVED)
//         That method ran an interactive console loop; the web
//         equivalent is GET /verify + POST /verify in
//         GradeController, rendered by verify-id.html.
// ============================================================
public final class IDVerifier {

    private IDVerifier() { }  // prevent instantiation

    /**
     * Returns true iff the ID string passes all three checks:
     *   (1) exactly ID_LENGTH (8) characters
     *   (2) all characters are digits
     *   (3) dot-product is divisible by ID_DIVISOR (11)
     */
    public static boolean isValidID(String idNumber) {
        if (idNumber == null || idNumber.length() != GradeConstants.ID_LENGTH)
            return false;
        if (!idNumber.chars().allMatch(Character::isDigit))
            return false;
        return calculateDotProduct(idNumber) % GradeConstants.ID_DIVISOR == 0;
    }

    /**
     * Returns "faculty" or "student" based on the dot-product quotient.
     * Call ONLY after isValidID() returns true.
     */
    public static String getIDRole(String idNumber) {
        int quotient = calculateDotProduct(idNumber) / GradeConstants.ID_DIVISOR;
        return (quotient >= GradeConstants.FACULTY_THRESHOLD) ? "faculty" : "student";
    }

    /**
     * Returns a human-readable validation result message.
     *   Invalid -> message starts with "Invalid"
     *   Valid   -> "Valid faculty ID number." or "Valid student ID number."
     */
    public static String validateID(String idNumber) {
        if (idNumber == null || idNumber.length() != GradeConstants.ID_LENGTH)
            return "Invalid ID number. Please enter "
                    + GradeConstants.ID_LENGTH + " digits.";

        if (!idNumber.chars().allMatch(Character::isDigit))
            return "Invalid ID number. All characters must be digits.";

        if (!isValidID(idNumber))
            return "Invalid ID number. Dot product must be divisible by "
                    + GradeConstants.ID_DIVISOR + ".";

        return "Valid " + getIDRole(idNumber) + " ID number.";
    }

    /** Dot product of the ID digits against descending weights [8..1]. */
    private static int calculateDotProduct(String idNumber) {
        final int[] WEIGHTS = {8, 7, 6, 5, 4, 3, 2, 1};
        int sum = 0;
        for (int i = 0; i < idNumber.length(); i++) {
            sum += Character.getNumericValue(idNumber.charAt(i)) * WEIGHTS[i];
        }
        return sum;
    }
}