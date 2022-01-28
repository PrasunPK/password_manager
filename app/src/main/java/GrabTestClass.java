import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class GrabTestClass {
    public static int solution(int[] A) {
        int[] sortedArray = Arrays.stream(A).sorted().toArray();

        if (sortedArray != null && sortedArray.length > 0) {
            return sortedArray[sortedArray.length - 1] + 1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        int[] ints = {1, 3, 6, 4, 1, 2};
        int result = solution(ints);
        System.out.println(result);
    }


}
