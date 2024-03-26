/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.utils;

import com.QLTV.entity.TaiKhoan;

/**
 *
 * @author tuonght
 */
public class XAuth {
    public static TaiKhoan user = null;
    
    public static void clear(){
        XAuth.user = null;
    }
    
    public static boolean isLogin(){
        return XAuth.user != null;
    } 
    
    public static boolean isManager(){
        return XAuth.isLogin() && user.getVaitro();
    }
}
