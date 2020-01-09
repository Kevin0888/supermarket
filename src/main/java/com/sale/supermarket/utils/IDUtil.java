package com.sale.supermarket.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class IDUtil {

    public static Integer getId() {
        String sdf = new SimpleDateFormat("MMddHHMMSS").format(new Date());
        return Integer.parseInt(sdf);
    }

}
