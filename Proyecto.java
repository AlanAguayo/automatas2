import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Proyecto {
    public static void main(String[] args) {
        String[] errores;
        String[] tipos;
        String[] declaraciones;
        String[] tabla;
        try {
            FileReader fileReader = new FileReader("./Programas/codigo.txt"); // Ruta del archivo de código fuente
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(" ");

                for (String token : tokens) {
                    if (Lexico.isKeyWord(token)) {
                        System.out.println(token + " es una palabra clave");
                    } else if (Lexico.isIdentifier(token)) {
                        System.out.println(token + " es un identificador");
                    } else if (Lexico.isNumber(token)) {
                        System.out.println(token + " es un número");
                    } else if (Lexico.isFloat(token)) {
                        System.out.println(token + " es un número decimal");
                    } else if (Lexico.isOperator(token)) {
                        System.out.println(token + " es un operador");
                    } else if(Lexico.isComment(token)){
                        System.out.println(token + " es un comentario");
                    }  else if(Lexico.isComparator(token)){
                        System.out.println(token + " es un comparador");
                    } else if(Lexico.isParenth(token)){
                        System.out.println(token + " es un parentesis");
                    } else {
                        System.out.println(token + " es un token desconocido");
                    }
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}