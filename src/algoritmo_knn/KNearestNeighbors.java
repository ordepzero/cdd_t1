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
public class KNearestNeighbors {
    
    /*
    CONSIDERAR_PAR_PRESENTE quando false significa que vou considerar no cálculo da distância
    mesmo quando um dos valores for ausente, e quando true só será considerado quando os dois registros tiverem 
    um valor.
    */
    private  Boolean CONSIDERAR_PAR_PRESENTE = false;    
    private  Integer CONSTANTE_K = 7;
    private Integer NUMERO_REGISTROS = 1000;
    private String ARQUIVO = "train_remocao";
    private String FILENAME = ARQUIVO+"_"+NUMERO_REGISTROS+"_K"+CONSTANTE_K+".txt";
    
    public static void main(String [] args){
//        List<List<String>> matriz = 
//                FileUtil.readFile("C:\\Users\\PeDeNRiQue\\Copy\\USP\\Disciplinas\\CienciaDosDados\\trabalho\\arquivos\\train.csv\\"+ARQUIVO+".txt",";",NUMERO_REGISTROS);
//        
//        int total = NUMERO_REGISTROS;
//        int parte = (int)((int) total * 0.9);//10%
//        List<List<String>> train = matriz.subList(1, parte);
//        List<List<String>> teste = matriz.subList(parte, total);
        
        List<List<String>> treinamento = FileUtil.readAllFile("C:\\Users\\PeDeNRiQue\\Copy\\USP\\Disciplinas\\CienciaDosDados\\trabalho\\arquivos\\train2\\partical_10000.txt",",");
        //System.out.println("Primeiro Arquivo\nSegundoArguivo");
        List<List<String>> teste = FileUtil.readAllFile("C:\\Users\\PeDeNRiQue\\Copy\\USP\\Disciplinas\\CienciaDosDados\\trabalho\\arquivos\\test.csv\\test.txt","\t");
        //System.out.println("Leu segundo arquivo");
        
        
        KNearestNeighbors knn = new KNearestNeighbors(1,false,"submissao.txt");
        FileUtil.writeFile("ID,PredictedProb",knn.FILENAME);
        knn.calculateDistances(treinamento.subList(1, treinamento.size()), teste.subList(1, teste.size()));
        
        System.out.println("FIM");
    }   
    
    public KNearestNeighbors(){}
    
    public KNearestNeighbors(Integer constanteK,boolean parPresente,String filename){
        CONSTANTE_K = constanteK;
        CONSIDERAR_PAR_PRESENTE = parPresente;
        FILENAME = filename;
    }
    
    private List<List<String>> calculateDistances(List<List<String>> train,List<List<String>> testes){
    
        for(List<String> teste : testes){
            teste.add(findKNearestNeighbors(train,teste,CONSTANTE_K,CONSIDERAR_PAR_PRESENTE));
        }
        
        return testes;
    }
    
    
    private String findKNearestNeighbors(List<List<String>> references,List<String> compared,Integer k,boolean parPresente){
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
            
            //System.out.println("REFERENCIAS: "+r.get(0)+" <_> ");
            temp = euclideanDistance(r,compared,parPresente);
           
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
//        FileUtil.writeFile(target+"\t"+compared.get(0)+"\t"+
//                compared.get(1)+"\t"+distance,this.FILENAME);
        FileUtil.writeFile(compared.get(0)+","+target,this.FILENAME);
        return target;
    }

    private  List<String> mean(List<List<String>> neighbors){
      List<String> mean = new ArrayList<String>();
      List<List<String>> classes = new ArrayList<List<String>>();
      Double summation = 0.0;
      
      
      for(List<String> s : neighbors){
          //System.out.println(s.get(0)+" >< "+s.get(1));
          
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
                    if(i == (classes.size()-1)){
                        List<String> first = new ArrayList<String>();
                        first.add(s.get(1));//target
                        first.add(s.get(0));//distance
                        classes.add(first);
                    }
                }
            }
          }
          //System.out.println("TAMANHO da CLASSE: "+classes.size());
      }
      
      String target = "";
      Double distance = Double.POSITIVE_INFINITY; 
      Double meanT;
      Double valorTotal = 0.0;
      
      for(int i = 0; i < classes.size(); i++){
          summation = 0.0;
          for(int j = 1; j < classes.get(i).size(); j++){
              summation += Double.parseDouble(classes.get(i).get(j));
          }
          valorTotal += summation;
          
          meanT = summation / classes.get(i).size();
          if(meanT < distance){
              distance = meanT;
              target = classes.get(i).get(0);
          }
      }
      //System.out.println("-> "+target+" <> "+distance);
      mean.add(target);
      mean.add(""+distance);
      
      return mean;
    }
    
    //FLAG é pra indentificar se vai considerar o cálculo com os valores nulos (quando são ausentes
    // são substituidos por zero) de um dos registros. 
    private Double euclideanDistance(List<String> reference,List<String> compared, Boolean flag){
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
    
    private void showMatriz(List<List<String>> matriz){
        for(List<String> ss : matriz){
            for(String s : ss){
                System.out.print(s+"<>");
            }
            System.out.println(" ");
        }
    }
}
