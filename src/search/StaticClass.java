/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

/**
 *
 * @author parham
 */
public class StaticClass {

    public static int VALUEOF(char v) {
        if (v > 64 && v < 90) {
            return v - 65;
        } else {
            return v - 97;
        }
    
    }
}
