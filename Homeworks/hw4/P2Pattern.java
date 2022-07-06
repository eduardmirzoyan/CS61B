/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static String P1 = "^(0?[1-9]|1[012])\\/([012]?[0-9]|30|31)\\/\\d{4}";

    /** Pattern to match 61b notation for literal IntLists. */
    public static String P2 = "\\((\\d+,\\s+)+\\d+\\)";

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static String P3 = "(www\\.)?(([a-zA-Z0-9]|([a-zA-Z0-9](-|[a-z])*[a-z]))\\.)+[a-zA-Z0-9]{2,6}";

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static String P4 = "^[^0-9][a-zA-Z_$0-9]*";

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static String P5 = "([0-9]|[0-9][0-9]|[01][0-9][0-9]|2[0-4][0-9]|25[0-5])\\.([0-9]|[0-9][0-9]|[01][0-9][0-9]|2[0-4][0-9]|25[0-5])\\.([0-9]|[0-9][0-9]|[01][0-9][0-9]|2[0-4][0-9]|25[0-5])\\.([0-9]|[0-9][0-9]|[01][0-9][0-9]|2[0-4][0-9]|25[0-5])$";

}
