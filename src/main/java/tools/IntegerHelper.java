package tools;


public class IntegerHelper {

    /**
     * 获取最小值
     * @param is
     * @return
     */
    public static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }

}
