import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Proyecto {
    public static void main(String[] args) throws IOException {
        Tabla tablaSimbolos = new Tabla();
        List<String> tablaDeclaraciones = new ArrayList<String>();
        List<String> tablaTipos = new ArrayList<String>();
        List<String> pilaErrores = new ArrayList<String>();
        FileReader fileReader = new FileReader("./Programas/codigo.lin");
        
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] programa = line.split(" ");

                int lineas = 0;

                for (String linea : programa) {
                    lineas++;
                    String[] lexemas = linea.split(" ");
                    List<String> lexemasValidos = new ArrayList<String>();
                    List<String> lexemasRefinados = new ArrayList<String>();

                    for (String lexema : lexemas) {
                        String aux = "";
                        boolean flag = false;
                        String cad = "";

                        if (!lexema.equals("")) {
                            if (lexema.startsWith("\"") && lexema.endsWith("\"")) {
                                Lexico.validar(lexema, lineas, tablaSimbolos, pilaErrores);
                            } else {
                                for (char token : lexema.toCharArray()) {
                                    if (Lexico.tokens.containsKey(String.valueOf(token)) && token != '#') {
                                        if (!cad.equals("")) {
                                            if (cad.equals("!")) {
                                                flag = true;
                                                cad += token;
                                                lexemasValidos.add(cad);
                                                cad = "";
                                            } else {
                                                lexemasValidos.add(cad);
                                                cad = "";
                                            }
                                        }
                                        if (!flag) {
                                            if (cad.equals("")) {
                                                lexemasValidos.add(String.valueOf(token));
                                            }
                                        }
                                    } else {
                                        cad += token;
                                    }
                                }
                                if (!cad.equals("")) {
                                    lexemasValidos.add(cad);
                                }
                                if (lexemasValidos.size() > 0) {
                                    for (String lexemaA : lexemasValidos) {
                                        if (lexemaA.equals("+") || lexemaA.equals("=") || lexemaA.equals("<")
                                                || lexemaA.equals(">")) {
                                            aux += lexemaA;
                                            lexemasRefinados.add(lexemaA);
                                            if (aux.equals("=<")) {
                                                aux = "<";
                                            }
                                            if (aux.equals("=>")) {
                                                aux = ">";
                                            }
                                            if (aux.equals("++") || aux.equals("==") || aux.equals("<=")
                                                    || aux.equals(">=")) {
                                                lexemasRefinados.remove(lexemasRefinados.size() - 1);
                                                lexemasRefinados.remove(lexemasRefinados.size() - 1);
                                                lexemasRefinados.add(aux);
                                                aux = "";
                                            }
                                        } else {
                                            lexemasRefinados.add(lexemaA);
                                        }
                                    }
                                    for (String lexemaB : lexemasRefinados) {
                                        Lexico.validar(lexemaB, lineas, tablaSimbolos, pilaErrores);
                                    }
                                    lexemasValidos.clear();
                                    lexemasRefinados.clear();
                                } else {
                                    Lexico.validar(lexema, lineas, tablaSimbolos, pilaErrores);
                                }
                            }
                        }
                    }
                }
            }
        }

        tablaSimbolos.insertar("", "@", "@", "@");

        // Sintaxis analisisSintactico = new Sintaxis(tablaSimbolos, pilaErrores,
        // tablaDeclaraciones, tablaTipos);
        // analisisSintactico.analizarSintaxis();
        tablaSimbolos.imprimirTabla();
        Semantica analisisSemantico = new Semantica(tablaSimbolos, pilaErrores, tablaDeclaraciones, tablaTipos);
        analisisSemantico.analizar();

        for (String error : pilaErrores) {
            System.out.println(error);
        }
    }
}