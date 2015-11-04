package com.billy.hb.task;

public class AppContext {
    
    private static String status;

    public static String getStatus() {
        return status;
    }

    static void setStatus(String status) {
        AppContext.status = status;
    }
}
