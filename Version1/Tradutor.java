import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tradutor {

    public static void ejecutar(String[] codigo) throws IOException {
        String[] variable = null;
        String[] valorVariable = null;
        String[] tipoVariable = null;

        FileReader fileReader = new FileReader("./Programas/codigo.lin");

        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                codigo = line.split(";");
                for (String linea : codigo) {
                    if (linea.contains("entero") || linea.contains("cadena")) {
                        variables(linea, variable, tipoVariable);
                    }
                    if (linea.contains("salida")) {
                        salida(linea, variable, valorVariable);
                    }
                    if (linea.contains("si")) {
                        si(linea);
                    }
                }
            }
        }
    }

    public static void variables(String linea, String[] variable, String[] tipoVariable) {
        String[] parte = linea.split(" ");
        String[] variableAux;
        String[] tipoVariableAux;
        if (variable != null) {
            variableAux = new String[variable.length];
        } else {
            variableAux = new String[1];

        }
        variableAux[variableAux.length - 1] = parte[0];

        if (tipoVariable != null) {
            tipoVariableAux = new String[tipoVariable.length];
        } else {
            tipoVariableAux = new String[1];
        }
        tipoVariableAux[variableAux.length - 1] = parte[1];

        variable = variableAux;
        tipoVariable = tipoVariableAux;
    }

    public static void si(String linea) {

    }

    public static void salida(String linea, String[] variable, String[] valorVariable) {
        String[] partes = linea.split("salida");
        String cadena = partes[1].substring(1, partes[1].length() - 1);
        for (int i = 0; i < variable.length; i++) {
            if (variable[i].equals(cadena)) {
                cadena = variable[i];
                //cadena = valorVariable[i];
            }
        }

        System.out.println(cadena);
    }
}
