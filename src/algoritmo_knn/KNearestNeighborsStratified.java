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
public class KNearestNeighborsStratified {
    /*
    CONSIDERAR_PAR_PRESENTE quando false significa que vou considerar no cálculo da distância
    mesmo quando um dos valores for ausente, e quando true só será considerado quando os dois registros tiverem 
    um valor.
    */
    public final static Boolean CONSIDERAR_PAR_PRESENTE = false;    
    public final static Integer CONSTANTE_K = 5;
    public final static Integer FOLD = 6;
    public final static Integer NUMERO_REGISTROS = 1000;
    public final static String ARQUIVO = "sem_id";
    public final static String FILENAME = ARQUIVO+"_"+NUMERO_REGISTROS+"_K"+CONSTANTE_K+"_6FOLD.txt";
    
    public static void main(String [] args){
        List<List<String>> matriz = FileUtil.readFile("C:\\Users\\PeDeNRiQue\\Copy\\USP\\Disciplinas\\CienciaDosDados\\trabalho\\arquivos\\train2\\"+ARQUIVO+".txt",";",FOLD*NUMERO_REGISTROS);
        
        int total = NUMERO_REGISTROS;
        int parte = (int)((int) total * 0.9);//10%
        //List<List<String>> train = matriz.subList(1, parte);
        //List<List<String>> teste = matriz.subList(parte, total);
        
        List<List<List<String>>> folds = new ArrayList<List<List<String>>>();
        
        for(int i = 0; i < FOLD; i++){
            folds.add(matriz.subList(i*NUMERO_REGISTROS, (i+1)*NUMERO_REGISTROS));
        }
        
        calculateDistances(folds);
        
        System.out.println("FIM");
    }
    

    
    public static void calculateDistances(List<List<List<String>>> folds){
        
        List<List<String>> train;
        List<List<String>> testes;
        
        for(int i = 0; i < folds.size(); i++){
            train = new ArrayList<List<String>>();
            testes = new ArrayList<List<String>>();
            
            testes = folds.get(i);
            for(int j = 0; j < folds.size(); j++){
                if(j != i){
                    for(int x = 0; x < folds.get(j).size(); x++){
                        train.add(folds.get(j).get(x));
                    }
                }
            }
            for(List<String> teste : testes){
                teste.add(findKNearestNeighbors(train,teste,CONSTANTE_K));
            }
        }  
        
        return;
    }
    
    
    public static String findKNearestNeighbors(List<List<String>> references,List<String> compared,Integer k){
        Double distance = Double.POSITIVE_INFINITY;
        Double temp;
        String target = null;      
        int j;
        List<List<String>> neighbors = new ArrayList<List<String>>();
        List<String> neighbor;
        
        for(int i = 0; i < k; i++){
            neighbor = new ArrayList<String>();
            neighbor.add(distance+"");
            neighbors.add(neighbor);
        }
        
        for(List<String> r : references){
            temp = euclideanDistance(r,compared,CONSIDERAR_PAR_PRESENTE);
           
            j = 0;
            while(temp < Double.parseDouble(neighbors.get(j).get(0))){
                j++;
                if(j == k){
                    break;
                }
            }
            if(j > 0){
                neighbor = new ArrayList<String>();
                neighbor.add(temp+"");//distance
                neighbor.add(r.get(1));//target
                neighbors.add(j,neighbor);
                neighbors.remove(0);
            }
        }
        
        List<String> result = mean(neighbors);
        
        target = result.get(0);
        distance = Double.parseDouble(result.get(1));
        
        //compared.get(0) -> id
        //compared.get(1) -> target
        FileUtil.writeFile(target+"\t"+compared.get(0)+"\t"+
                compared.get(1)+"\t"+distance,FILENAME);
        return target;
    }
    
    public static List<String> mean(List<List<String>> neighbors){
      List<String> mean = new ArrayList<String>();
      List<List<String>> classes = new ArrayList<List<String>>();
      Double summation = 0.0;
      
      
      for(List<String> s : neighbors){
          
          if( classes.size() == 0){
              List<String> first = new ArrayList<String>();
              first.add(s.get(1));//target
              first.add(s.get(0));//distance
              classes.add(first);
          }else{
            for(int i = 0; i < classes.size(); i++){
                if(classes.get(i).get(0).equals(s.get(1))){
                    classes.get(i).add(s.get(0));
                }else{
                    List<String> first = new ArrayList<String>();
                    first.add(s.get(1));//target
                    first.add(s.get(0));//distance
                    classes.add(first);
                }
            }
          }
      }
      
      String target = "";
      Double distance = Double.POSITIVE_INFINITY; 
      Double meanT;
      
      for(int i = 0; i < classes.size(); i++){
          summation = 0.0;
          for(int j = 1; j < classes.get(i).size(); j++){
              summation += Double.parseDouble(classes.get(i).get(j));
          }
          
          meanT = summation / classes.get(i).size();
          if(meanT < distance){
              distance = meanT;
              target = classes.get(i).get(0);
          }
      }
      
      mean.add(target);
      mean.add(""+distance);
      
      return mean;
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
}
