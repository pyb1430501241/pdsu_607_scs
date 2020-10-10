package com.pdsu.scs.utils;

import com.pdsu.scs.bean.BlobInformation;

import java.util.Comparator;

/**
 * @author 半梦
 * @create 2020-09-21 20:33
 */
public class SortUtils {


    public static Comparator<BlobInformation> getBlobComparator() {
        return (o1, o2) -> {
            Integer hint1 = o1.getCollection()*50000 + o1.getThumbs()*30000 + o1.getVisit()*10000
                    - (int) SimpleUtils.getSimpleDateDifference(o1.getWeb().getSubTime(), SimpleUtils.getSimpleDateSecond());
            Integer hint2 = o2.getCollection()*50000 + o2.getThumbs()*30000 + o2.getVisit()*10000
                    - (int)SimpleUtils.getSimpleDateDifference(o2.getWeb().getSubTime(), SimpleUtils.getSimpleDateSecond());
            if(hint1 > hint2) {
                return -1;
            } else if(hint1.equals(hint2))
                return 0;
            return 1;
        };
    }
}
