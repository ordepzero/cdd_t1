/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algoritmo_knn;

import cienciadados.Estatisticas;
import cienciadados.FileUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PeDeNRiQue
 */
public class UmKNN {
    public static void main(String [] args){
        List<List<String>> matriz = FileUtil.readFile("C:\\Users\\PeDeNRiQue\\Copy\\USP\\Disciplinas\\CienciaDosDados\\trabalho\\arquivos\\train2\\partical_1000.txt",",");
        
        int total = matriz.size();
        int parte = (int)((int) total * 0.9);//10%
        List<List<String>> train = matriz.subList(1, parte);
        List<List<String>> teste = matriz.subList(parte, total);
        
        //showMatriz(teste);
        
        //Double result = euclideanDistance(train.get(1),train.get(2),true);
        
        calculateDistances(train, teste);
        
        System.out.println("FIM");
    }
    
    public static List<List<String>> calculateDistances(List<List<String>> train,List<List<String>> testes){
    
        for(List<String> teste : testes){
            teste.add(findOneNearest(train,teste));
        }
        
        return testes;
    }
    
    
    public static String findOneNearest(List<List<String>> references,List<String> compared){
        Double distance = Double.POSITIVE_INFINITY;
        Double temp;
        String target = null;
        String indice = null;        
        
        
        for(List<String> r : references){
            temp = euclideanDistance(r,compared,false);
            if(temp < distance){
                distance = temp;
                indice = r.get(0);
                target = r.get(1);
            }
        }
        FileUtil.writeFile(indice+"\t"+target+"\t"+compared.get(0)+"\t"+
                compared.get(1)+"\t"+distance);
        return target;
    }
    
    
    //FLAG é pra indentificar se vai considerar o cálculo com os valores nulos (quando são ausentes
    // são substituidos por zero) de um dos registros. 
    public static Double euclideanDistance(List<String> reference,List<String> compared, Boolean flag){
        Double distance = 0.;
        Double diference;
        
        int size = Math.max(reference.size(), compared.size());
        //System.out.println("TTTTTTTTT "+size+" "+compared.size()+" "+reference.size());

        if(reference.size() < size){
            while(reference.size() < size){
                reference.add("0.0");
            }
        }else if(compared.size() < size){
            while(compared.size() < size){
                compared.add("0.0");
            }
        }
        
        for(int i = 2; i < size; i++){
            
            if(Estatisticas.isNumeric(compared.get(i)) && Estatisticas.isNumeric(reference.get(i))){
                diference = Double.parseDouble(compared.get(i)) - Double.parseDouble(reference.get(i));
                distance += diference * diference;
                //System.out.println(i+" "+Double.parseDouble(compared.get(i))+" "+Double.parseDouble(reference.get(i))+" "+diference*diference);
            }else{
                
                
                if(flag && (Estatisticas.isNumeric(compared.get(i)) || Estatisticas.isNumeric(reference.get(i)))){
//                    System.out.println("\t"+i+"->> "+compared.get(i).length() +" <> "+ reference.get(i).length()
//                    +"\t->>"+compared.get(i)+" <> "+ reference.get(i));
                    if(compared.get(i).length() == 0){
                        compared.set(i,"0.0");
                    }
                    if(reference.get(i).length() == 0){
                        reference.set(i,"0.0");
                    }
                    diference = Double.parseDouble(compared.get(i)) - Double.parseDouble(reference.get(i));
                    distance += diference * diference;
                    //System.out.println(i+" "+Double.parseDouble(compared.get(i))+" "+Double.parseDouble(reference.get(i))+" "+diference*diference);
                }
            }
        }
        
        return Math.sqrt(distance);
    }
    
    public static void showMatriz(List<List<String>> matriz){
        for(List<String> ss : matriz){
            for(String s : ss){
                System.out.print(s+"<>");
            }
            System.out.println(" ");
        }
    }
    
    public static void teste1(){
               
        List<String> l1 = new ArrayList<String>();
        List<String> l2 = new ArrayList<String>();
                
        l1.add("1");
        l1.add("2");
        l1.add("3");
        l1.add("4");
        
        l2.add("1");
        l2.add("2");
        l2.add("3");
        l2.add("4");
        
        System.out.println("-> "+ euclideanDistance(l1,l2,true));
    }
    
}
