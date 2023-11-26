package Code;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author tungd
 */
public class Test {
    public static void main(String[] args) throws IOException {
        int[] x = {1, 2, 3};
        int[] y = {1, 2, 3};
        ArrayList lst = new ArrayList();
        lst.add(x);
        System.out.println(lst.contains(y));
    }
}
