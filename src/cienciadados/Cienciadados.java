/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cienciadados;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PeDeNRiQue
 */
public class Cienciadados {

    public static void main(String[] args) {
        List<List<String>> matriz = FileUtil.readFile("C:\\Users\\PeDeNRiQue\\Copy\\USP\\Disciplinas\\CienciaDosDados\\trabalho\\arquivos\\train.csv\\train.txt","\t");
        int total = matriz.size();
        int parte = (int)((int) total * 0.1);
        List<List<String>> train = matriz.subList(0, parte);//new ArrayList<List<String>>();
        List<List<String>> teste = matriz.subList(parte, total);
            
        String cabecalhos = "ID\tTARGET_ID\tID_REF\tTARGET_REF\tMENOR";
        FileUtil.writeFile(cabecalhos);
        for(int i = 0; i < teste.size(); i++){
            comparar(train,teste.get(i));
        }
        
        System.out.println("FIM");
    }
    
    
    public static void comparar(List<List<String>> matriz, List<String> registro){
        
        Double diferenca;
        List<Double> diferencas = new ArrayList<Double>();
        Double menor = null;
        Double temp;
        Integer indiceMenor = null;
        
        for(int i = 1; i < matriz.size(); i++){//PRIMEIRA LINHA CONTEM OS CABECALHOS
            //diferencas.add(comparaRegistros(matriz.get(i),registro));
            if(menor == null){
                if(comparaRegistros(matriz.get(i),registro) != 0){
                    menor = comparaRegistros(matriz.get(i),registro);
                    indiceMenor = i;
                }
            }
            temp = comparaRegistros(matriz.get(i),registro);
            if(temp != 0 && temp < menor){
                menor = temp;
                indiceMenor = i;
            }
            //System.out.println(temp);
            diferencas.add(temp);
        }
        
        if(menor != null && menor != 0 && indiceMenor != null){
//            System.out.println("Menor: "+menor+",Posicao: "+indiceMenor+",ID: "+matriz.get(indiceMenor).get(0)
//                +",ID_REG: "+registro.get(0));
        
            String resultado = registro.get(0)+"\t"+registro.get(1)+"\t"+matriz.get(indiceMenor).get(0)+"\t"
            +matriz.get(indiceMenor).get(1)+"\t"+menor;

            FileUtil.writeFile(resultado);
        }
                
    }
    
    public static Double comparaRegistros(List<String> train,List<String> registro){
        Double diferenca = 0.;
        
        for(int i = 1; i < registro.size(); i++){//PRIMEIRA POSICAO É O ID DO REGSITRO
            
            if(i >= train.size() || i >= registro.size()){
                break;
            }
            //System.out.println(train.get(i)+" - "+registro.get(i));
            if(Estatisticas.isNumeric(train.get(i)) && Estatisticas.isNumeric(registro.get(i))){
                
                //System.out.println(train.get(i)+" - "+registro.get(i));
                diferenca += Double.parseDouble(train.get(i)) - Double.parseDouble(registro.get(i));
                //System.out.println("("+diferenca+")");
            }
        }
        
        return Math.abs(diferenca);
    }
    
}