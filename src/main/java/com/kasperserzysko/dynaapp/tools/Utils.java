package com.kasperserzysko.dynaapp.tools;

import java.util.List;

public class Utils {


    public static float findMax(List<Float> list){
        float max = Float.MIN_VALUE;

        for (float i : list) {
            if (max < i) {
                max = i;
            }
        }

        return max;
    }
    public static float findMin(List<Float> list){
        float min = Float.MAX_VALUE;

        for (float i : list) {
            if (min > i) {
                min = i;
            }
        }

        return min;
    }

}
