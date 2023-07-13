import java.util.ArrayList;
import java.util.List;

public class Semantica {
    private Tabla tablaSimbolos;
    private List<String> pilaErrores;
    private List<String> tablaDeclaraciones;
    private List<String> tablaTipos;

    public Semantica(Tabla tablaSimbolos, List<String> pilaErrores, List<String> tablaDeclaraciones,
            List<String> tablaTipos) {
        this.tablaSimbolos = tablaSimbolos;
        this.pilaErrores = pilaErrores;
        this.tablaDeclaraciones = tablaDeclaraciones;
        this.tablaTipos = tablaTipos;
    }

    public void analizar() {
        List<String> tabla = tablaSimbolos.filtrarVariablesNombres();
        for (String i : tabla) {
            if (!tablaDeclaraciones.contains(i)) {
                pilaErrores.add("Error semantico: Variable " + i + " sin declarar");
            }
        }
        //
        List<String> nuevaTabla = new ArrayList<>();
        List<String> aux = new ArrayList<>();
        for (String i : tabla) {
            String[] reparto = i.split(" ");
            if (tablaDeclaraciones.contains(reparto[0])) {
                nuevaTabla.add(i);
            }
        }

        for (String i : nuevaTabla) {
            String[] reparto = i.split(" ");
            for (String j : tablaTipos) {
                String[] reparto2 = j.split(" ");
                if(reparto.length!= 0){
                    
                if (reparto[0].equals(reparto2[1]) && !reparto[1].equals("nulo")) {
                    aux.add(reparto2[0] + " " + i);
                }
                }
            }
        }

        for (String i : aux) {
            String[] reparto = i.split(" ");
            if (reparto[0].equals("entero")) {
                try {
                    Integer.parseInt(reparto[2]);
                } catch (NumberFormatException e) {
                    pilaErrores.add("Error Semantico Se Declaro: " + reparto[1]
                            + " Como entero y Se Trata De Usar Como cadena");
                }
            }
            if (reparto[0].equals("cadena")) {
                if (reparto[2].contains("\"")) {
                    // Se trata de una cadena válida
                } else {
                    pilaErrores.add(
                            "Error Semantico Se Declaro " + reparto[1] + " Como cadena y Se Trata De Usar Como entero");
                }
            }
        }

    }
}
