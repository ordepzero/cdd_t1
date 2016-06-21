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
public class TesteTrocarPontoVirgula {
    public final static Boolean CONSIDERAR_PAR_PRESENTE = false;    
    public final static Integer CONSTANTE_K = 1;
    public final static Integer NUMERO_REGISTROS = 114322;
    public final static String ARQUIVO = "train_remocao";
    public final static String FILENAME = "remocao.txt";
    
    
    public static void main(String [] args){
        List<List<String>> matriz = 
                FileUtil.readFile("C:\\Users\\PeDeNRiQue\\Copy\\USP\\Disciplinas\\CienciaDosDados\\trabalho\\arquivos\\train.csv\\"+ARQUIVO+".txt"," ",NUMERO_REGISTROS);
        String sss;
        for(List<String> s : matriz){
            for(String ss : s){
                sss = ss.replace(";",",");
                FileUtil.writeFile(sss, FILENAME);
            }
        }
    }
}
