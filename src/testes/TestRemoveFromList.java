/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PeDeNRiQue
 */
public class TestRemoveFromList {
    public static void main(String[]args){
        List<String> list = new ArrayList<String>();
        
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        
        
        list.remove(2);
        System.out.println(list.get(2));
//        for(String s : list){
//            System.out.println(s);
//        }
    }
}
