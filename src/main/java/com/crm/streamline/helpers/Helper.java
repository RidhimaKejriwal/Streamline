package com.crm.streamline.helpers;

import org.springframework.security.core.Authentication;

public class Helper {
    public static String getEmailOfLoggedInUser(Authentication authentication) {
        
        System.out.println("getting data from local database"); 
        return authentication.getName();
    }
}
