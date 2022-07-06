import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {
    // FIXME: Add your own tests for your ECHashStringSetTest

    @Test
    public void myTest() {
        BSTStringSet bst = new BSTStringSet();

        bst.put("c");
        assertEquals(bst.contains("c"),true);

        bst.put("d");
        assertEquals(bst.contains("d"),true);

        ArrayList<String> tmp = new ArrayList();
        tmp.add("c");
        tmp.add("d");
        assertEquals(tmp,bst.asList());

        bst.put("a");
        ArrayList<String> tmp0 = new ArrayList();
        tmp0.add("a");
        tmp0.add("c");
        tmp0.add("d");
        assertEquals(tmp0,bst.asList());
    }


}
