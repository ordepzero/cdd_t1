/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testes;

import cienciadados.FileUtil;
import java.util.List;

/**
 *
 * @author PeDeNRiQue
 */
public class TesteFiftyFifty {
    
    public static int N_GROUPS = 6;
    
    public static void main (String[] args){
       List<List<String>> matriz = FileUtil.readFile("C:\\Users\\PeDeNRiQue\\Copy\\USP\\Disciplinas\\CienciaDosDados\\trabalho\\arquivos\\train2\\partical_10000.txt",",");
 
        //System.out.println("Size: "+matriz.size()/6);
        int begin = 0;
        int end = matriz.size()/6;
        int interval = (matriz.size()/6)+1;
        
        for(int i = 0; i < N_GROUPS; i++){
            System.out.println("BEGIN: "+begin+" END: "+end);
            begin = end;
            end = end + interval;
        }
    }
    
    public static int getTotal(List<List<String>> matriz){
        int total = 0;
        for(List<String> ss : matriz){
            if(ss.get(1).equals("0")){
                total++;
            }
        }
        
        System.out.println("Total: "+matriz.size()+" 0: "+total+" 1: "+(matriz.size()-total));
        
        return 0;
    }
}


