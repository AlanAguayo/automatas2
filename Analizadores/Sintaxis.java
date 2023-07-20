import java.util.Arrays;
import java.util.List;

public class Sintaxis {

    int posicion = 0;
    String cad = "";

    String[] primeroTIPO = { "entero", "cadena" };
    String[] primeroIN = { "entrada" };
    String[] primeroOUT = { "salida" };
    String[] primeroIF = { "si" };
    String[] primeroELSE = { "else" };
    String[] primeroFOR = { "para" };
    String[] primeroWHILE = { "mientras" };
    String[] primeroBLOQUE = { "{" };
    String[] primeroASIGNA = { "ID" };
    String[] primeroOPR = { "<=", ">=", "<", ">", "==", "!=" };
    String[] primeroOP = { "++", "--" };
    String[] primeroVAR = { "entero", "cadena" };
    String[] primeroINSTRUCCIONES = { "entrada", "salida", "si", "para", "sino", "mientras", "ID" };
    String[] primeroINSTRUCCION = primeroINSTRUCCIONES;
    String[] primeroMINSTRUCCION = primeroINSTRUCCIONES;
    String[] primeroINICIO = { "entrada", "salida", "si", "para", "sino", "mientras", "ID", "entero", "cadena", "" };
    String[] primeroEXP = { "-", "(", "ID", "NUM", "FL", "CAD" };
    String[] primeroR = { "(", "ID", "NUM", "FL", "CAD" };
    String[] primeroT = primeroEXP;
    String[] primeroF = primeroEXP;

    String[] noTerminales = { "<INICIO>", "<VAR>", "<TIPO>", "<INSTRUCCIONES>", "<MINSTRUCCION>", "<INSTRUCCION>",
            "<IF>", "<ELSE>", "<FOR>", "<WHILE>", "<IN>", "<OUT>", "<ASIGNA>", "<OPR>", "<EXP>", "<T>", "<F>",
            "<R>", "<BLOQUE>", "<OP>" };

    private Tabla tablaSimbolos;
    private List<String> pilaErrores;
    private List<String> tablaDeclaraciones;
    private List<String> tablaTipos;

    public Sintaxis(Tabla tablaSimbolos, List<String> pilaErrores, List<String> tablaDeclaraciones,
            List<String> tablaTipos) {
        this.tablaSimbolos = tablaSimbolos;
        this.pilaErrores = pilaErrores;
        this.tablaDeclaraciones = tablaDeclaraciones;
        this.tablaTipos = tablaTipos;
    }

    public void imprimirInic() {
        System.out.println(tablaDeclaraciones);
    }

    public void asignarVar() {
        int pos = this.posicion;
        while (!String.valueOf(tablaSimbolos.getElemento(pos)).equals("@")
                && !String.valueOf(tablaSimbolos.getElemento(pos)).equals(";")) {
            cad += String.valueOf(tablaSimbolos.getElementoReal(pos));
            pos++;
        }
        cad = cad.substring(1);
    }

    public String obtenerActual() {
        return tablaSimbolos.getElemento(posicion);
    }

    public String obtenerSiguiente() {
        if (!tablaSimbolos.getElemento(posicion + 1).equals("@")) {
            return tablaSimbolos.getElemento(posicion + 1);
        }
        return null;
    }

    public String obtenerSiguienteReal() {
        return tablaSimbolos.getElementoReal(posicion + 1);
    }

    public void avanzar() {
        if (posicion < tablaSimbolos.getTamano() - 1) {
            posicion++;
        }
    }

    public void INICIO() {
        String produccion = "";
        Object actual = obtenerActual();
        if (Arrays.asList(primeroINICIO).contains(actual)) {
            produccion = "<VAR> <INSTRUCCIONES>";
        }
        if (produccion.equals("")) {
            pilaErrores.add("Error De Inicio, Se Esperaba Una Variable o Alguna Instruccion");
        } else {
            String[] arregloProd = produccion.split(" ");
            for (String i : arregloProd) {
                if (Arrays.asList(noTerminales).contains(i)) {
                    detectarNoTerminal(i);
                } else if (i.equals(obtenerActual())) {
                    avanzar();
                } else {
                    pilaErrores.add("Error Sintactico");
                }
            }
        }
    }

    public void VAR() {
        Object actual = obtenerActual();
        String produccion = "";
        if (Arrays.asList(primeroVAR).contains(actual)) {
            produccion = "<TIPO> ID ; <VAR>";
        }
        if (!produccion.equals("")) {
            String[] arregloProd = produccion.split(" ");
            for (String i : arregloProd) {
                if (Arrays.asList(noTerminales).contains(i)) {
                    detectarNoTerminal(i);
                } else if (i.equals(obtenerActual())) {
                    avanzar();
                } else {
                    pilaErrores.add("Error Sintactico Se Esperaba: " + i + " En Declaracion de Variable.");
                }
            }
        }
    }

    public void TIPO() {
        Object actual = obtenerActual();
        String produccion = "";
        if (Arrays.asList(primeroTIPO).contains(actual)) {
            produccion = (String) actual;
        }
        if (produccion.equals("")) {
            pilaErrores.add("Error Sintactico se Esperaba: entero o cadena");
        } else {
            if (Arrays.asList(noTerminales).contains(produccion)) {
                detectarNoTerminal(produccion);
            } else if (produccion.equals(obtenerActual())) {
                tablaDeclaraciones.add(obtenerSiguiente());
                tablaTipos.add(produccion + " " + obtenerSiguiente());
                avanzar();
            } else {
                pilaErrores.add("Error Sintactico");
            }
        }
    }

    public void INSTRUCCIONES() {
        Object actual = obtenerActual();
        String produccion = "";
        if (Arrays.asList(primeroINSTRUCCIONES).contains(actual)) {
            produccion = "<INSTRUCCION> <MINSTRUCCION>";
        }
        if (!produccion.equals("")) {
            String[] arregloProd = produccion.split(" ");
            for (String i : arregloProd) {
                if (Arrays.asList(noTerminales).contains(i)) {
                    detectarNoTerminal(i);
                } else if (i.equals(obtenerActual())) {
                    avanzar();
                } else {
                    pilaErrores.add("Error Sintactico");
                }
            }
        }
    }

    public void MINSTRUCCION() {
        Object actual = obtenerActual();
        String produccion = "";
        if (Arrays.asList(primeroMINSTRUCCION).contains(actual)) {
            produccion = "<INSTRUCCION> <MINSTRUCCION>";
        }
        if (!produccion.equals("")) {
            String[] arregloProd = produccion.split(" ");
            for (String i : arregloProd) {
                if (Arrays.asList(noTerminales).contains(i)) {
                    detectarNoTerminal(i);
                } else if (i.equals(obtenerActual())) {
                    avanzar();
                } else {
                    pilaErrores.add("Error Sintactico");
                }
            }
        }
    }

    public void INSTRUCCION() {
        Object actual = obtenerActual();
        String produccion = "";
        if (Arrays.asList(primeroINSTRUCCION).contains(actual)) {
            if (actual.equals("entrada")) {
                produccion = "<IN>";
            }
            if (actual.equals("salida")) {
                produccion = "<OUT>";
            }
            if (actual.equals("si")) {
                produccion = "<IF>";
            }
            if (actual.equals("sino")) {
                produccion = "<ELSE>";
            }
            if (actual.equals("para")) {
                produccion = "<FOR>";
            }
            if (actual.equals("mientras")) {
                produccion = "<WHILE>";
            }
            if (actual.equals("ID")) {
                produccion = "<ASIGNA>";
            }
        }
        if (produccion.equals("")) {
            pilaErrores.add("Error Sintactico Se Esperaba: entrada o salida o si o sino o para o mientras o ID");
            // No haces nada
        } else {
            if (Arrays.asList(noTerminales).contains(produccion)) {
                detectarNoTerminal(produccion);
            } else if (produccion.equals(obtenerActual())) {
                avanzar();
            } else {
                pilaErrores.add("Error Sintactico");
            }
        }
    }

    public void IF() {
        String produccion = "si ( <EXP> <OPR> <EXP> ) <BLOQUE>";
        String[] arregloProd = produccion.split(" ");
        for (String i : arregloProd) {
            if (Arrays.asList(noTerminales).contains(i)) {
                detectarNoTerminal(i);
            } else if (i.equals(obtenerActual())) {
                avanzar();
            } else {
                pilaErrores.add("Error Sintactico Se Esperaba: " + i + " En Instruccion SI.");
            }
        }
    }

    public void ELSE() {
        String produccion = "sino <BLOQUE>";
        String[] arregloProd = produccion.split(" ");
        for (String i : arregloProd) {
            if (Arrays.asList(noTerminales).contains(i)) {
                detectarNoTerminal(i);
            } else if (i.equals(obtenerActual())) {
                avanzar();
            } else {
                pilaErrores.add("Error Sintactico Se Esperaba: " + i + " En Instruccion SINO.");
            }
        }
    }

    public void FOR() {
        String produccion = "para ( <EXP> ; <EXP> <OPR> <EXP> ; <ASIGNA> ) <BLOQUE>";
        String[] arregloProd = produccion.split(" ");
        for (String i : arregloProd) {
            if (Arrays.asList(noTerminales).contains(i)) {
                detectarNoTerminal(i);
            } else if (i.equals(obtenerActual())) {
                avanzar();
            } else {
                pilaErrores.add("Error Sintactico Se Esperaba: " + i + " En Instruccion PARA.");
            }
        }
    }

    public void WHILE() {
        String produccion = "mientras ( <EXP> <OPR> <EXP> ) <BLOQUE>";
        String[] arregloProd = produccion.split(" ");
        for (String i : arregloProd) {
            if (Arrays.asList(noTerminales).contains(i)) {
                detectarNoTerminal(i);
            } else if (i.equals(obtenerActual())) {
                avanzar();
            } else {
                pilaErrores.add("Error Sintactico Se Esperaba: " + i + " En Instruccion MIENTRAS.");
            }
        }
    }

    public void IN() {
        String produccion = "entrada ( <EXP> ) ; <VAR>";
        String[] arregloProd = produccion.split(" ");
        for (String i : arregloProd) {
            if (Arrays.asList(noTerminales).contains(i)) {
                detectarNoTerminal(i);
            } else if (i.equals(obtenerActual())) {
                avanzar();
            } else {
                pilaErrores.add("Error Sintactico Se Esperaba: " + i + " En Instruccion ENTRADA.");
            }
        }
    }

    public void OUT() {
        String produccion = "salida ( <EXP> ) ; <VAR>";
        String[] arregloProd = produccion.split(" ");
        for (String i : arregloProd) {
            if (Arrays.asList(noTerminales).contains(i)) {
                detectarNoTerminal(i);
            } else if (i.equals(obtenerActual())) {
                avanzar();
            } else {
                pilaErrores.add("Error Sintactico Se Esperaba: " + i + " En Instruccion SALIDA.");
            }
        }
    }

    public void ASIGNA() {
        Object actual = obtenerActual();
        Object siguiente = obtenerSiguiente();
        String produccion = "";
        if (Arrays.asList(primeroASIGNA).contains(actual)) {
            if (siguiente.equals("=")) {
                produccion = "ID = <EXP> ; <VAR> <MINSTRUCCION>";
            }
            if (siguiente.equals("++")) {
                produccion = "ID <OP> ; <VAR> <MINSTRUCCION>";
            }
            if (siguiente.equals("--")) {
                produccion = "ID <OP> ; <VAR> <MINSTRUCCION>";
            }
        }
        if (produccion.equals("")) {
            pilaErrores.add("Error Sintactico Se Esperaba: = o ++ o -- En Asignacion de Variable.");
            avanzar();
        } else {
            String id = "";
            String[] arregloProd = produccion.split(" ");
            for (String i : arregloProd) {
                if (Arrays.asList(noTerminales).contains(i)) {
                    detectarNoTerminal(i);
                } else if (i.equals(obtenerActual())) {
                    if (obtenerActual().equals("ID")) {
                        id = obtenerActual();
                    }
                    if (obtenerActual().equals("=")) {
                        asignarVar();
                        tablaSimbolos.actualizar(id, cad);
                        cad = "";
                    }
                    avanzar();
                } else {
                    pilaErrores.add("Error Sintactico Se Esperaba: " + i + " En Asignacion de Variable.");
                }
            }
        }
    }

    public void OPR() {
        String actual = obtenerActual();
        String produccion = "";
        if (Arrays.asList(primeroOPR).contains(actual)) {
            produccion = actual;
        }
        if (produccion.equals("")) {
            pilaErrores.add("Error Sintactico Se Esperaba: Operador Relacional");
        } else {
            if (Arrays.asList(noTerminales).contains(produccion)) {
                detectarNoTerminal(produccion);
            } else if (produccion.equals(obtenerActual())) {
                avanzar();
            } else {
                pilaErrores.add("Error Sintactico");
            }
        }
    }

    public void EXP() {
        Object actual = obtenerActual();
        Object siguiente = obtenerSiguiente();
        String produccion = "";
        if (Arrays.asList(primeroEXP).contains(actual)) {
            if (siguiente.equals("*")) {
                produccion = "<T> * <EXP>";
            } else if (siguiente.equals("/")) {
                produccion = "<T> / <EXP>";
            } else {
                produccion = "<T>";
            }
        }
        if (produccion.equals("")) {
            pilaErrores.add("Error Sintactico Se Esperaba: * o / o Termino(cadenas o numeros)");
        } else {
            if (produccion.contains("<EXP>")) {
                String[] arregloProd = produccion.split(" ");
                for (String i : arregloProd) {
                    if (Arrays.asList(noTerminales).contains(i)) {
                        detectarNoTerminal(i);
                    } else if (i.equals(obtenerActual())) {
                        avanzar();
                    } else {
                        pilaErrores.add("Error Sintactico Se Esperaba: " + i);
                    }
                }
            } else {
                if (Arrays.asList(noTerminales).contains(produccion)) {
                    detectarNoTerminal(produccion);
                } else if (produccion.equals(obtenerActual())) {
                    avanzar();
                } else {
                    pilaErrores.add("Error Sintactico Se Esperaba un Termino");
                }
            }
        }
    }

    public void T() {
        Object actual = obtenerActual();
        Object siguiente = obtenerSiguiente();
        String produccion = "";
        if (Arrays.asList(primeroT).contains(actual)) {
            if (siguiente.equals("+")) {
                produccion = "<F> + <T>";
            } else if (siguiente.equals("-")) {
                produccion = "<F> - <T>";
            } else {
                produccion = "<F>";
            }
        }
        if (produccion.equals("")) {
            pilaErrores.add("Error Sintactico Se Esperaba: + o - o Termino Negativo o Positivo");
        } else {
            if (produccion.contains("<T>")) {
                String[] arregloProd = produccion.split(" ");
                for (String i : arregloProd) {
                    if (Arrays.asList(noTerminales).contains(i)) {
                        detectarNoTerminal(i);
                    } else if (i.equals(obtenerActual())) {
                        avanzar();
                    } else {
                        pilaErrores.add("Error Sintactico Se Esperaba: " + i);
                    }
                }
            } else {
                if (Arrays.asList(noTerminales).contains(produccion)) {
                    detectarNoTerminal(produccion);
                } else if (produccion.equals(obtenerActual())) {
                    avanzar();
                } else {
                    pilaErrores.add("Error Sintactico");
                }
            }
        }
    }

    public void F() {
        Object actual = obtenerActual();
        String produccion = "";
        if (Arrays.asList(primeroF).contains(actual)) {
            if (actual.equals("-")) {
                produccion = "- <F>";
            } else {
                produccion = "<R>";
            }
        }
        if (produccion.equals("")) {
            pilaErrores.add("Error Sintactico Se Esperaba: - o ( o ID o NUM o FL o CAD");
        } else {
            if (produccion.contains("<F>")) {
                String[] arregloProd = produccion.split(" ");
                for (String i : arregloProd) {
                    if (Arrays.asList(noTerminales).contains(i)) {
                        detectarNoTerminal(i);
                    } else if (i.equals(obtenerActual())) {
                        avanzar();
                    } else {
                        pilaErrores.add("Error Sintactico Se Esperaba: " + i);
                    }
                }
            } else {
                if (Arrays.asList(noTerminales).contains(produccion)) {
                    detectarNoTerminal(produccion);
                } else if (produccion.equals(obtenerActual())) {
                    avanzar();
                } else {
                    pilaErrores.add("Error Sintactico");
                }
            }
        }
    }

    public void R() {
        Object actual = obtenerActual();
        String produccion = "";
        if (Arrays.asList(primeroR).contains(actual)) {
            if (actual.equals("(")) {
                produccion = "( <EXP> )";
            } else if (actual.equals("ID")) {
                produccion = "ID";
            } else if (actual.equals("NUM")) {
                produccion = "NUM";
            } else if (actual.equals("FL")) {
                produccion = "FL";
            } else if (actual.equals("CAD")) {
                produccion = "CAD";
            }
        }
        if (produccion.equals("")) {
            pilaErrores.add("Error Sintactico Se Esperaba: ( o ID o NUM o FL o CAD [R]");
        } else {
            if (produccion.contains("<EXP>")) {
                String[] arregloProd = produccion.split(" ");
                for (String i : arregloProd) {
                    if (Arrays.asList(noTerminales).contains(i)) {
                        detectarNoTerminal(i);
                    } else if (i.equals(obtenerActual())) {
                        avanzar();
                    } else {
                        pilaErrores.add("Error Sintactico Se Esperaba: " + i);
                    }
                }
            } else {
                if (Arrays.asList(noTerminales).contains(produccion)) {
                    detectarNoTerminal(produccion);
                } else if (produccion.equals(obtenerActual())) {
                    avanzar();
                } else {
                    pilaErrores.add("Error Sintactico");
                }
            }
        }
    }

    public void BLOQUE() {
        String produccion = "{ <VAR> <MINSTRUCCION> } <VAR> <MINSTRUCCION>";
        String[] arregloProd = produccion.split(" ");
        for (String i : arregloProd) {
            if (Arrays.asList(noTerminales).contains(i)) {
                detectarNoTerminal(i);
            } else if (i.equals(obtenerActual())) {
                avanzar();
            } else {
                pilaErrores.add("Error Sintactico Se Esperaba: " + i);
            }
        }
    }

    public void OP() {
        Object actual = obtenerActual();
        String produccion = "";
        if (Arrays.asList(primeroOP).contains(actual)) {
            produccion = (String) actual;
        }
        if (produccion.equals("")) {
            pilaErrores.add("Error Sintactico Se Esperaba Un Operador Binario");
        } else {
            if (Arrays.asList(noTerminales).contains(produccion)) {
                detectarNoTerminal(produccion);
            } else if (produccion.equals(obtenerActual())) {
                avanzar();
            } else {
                pilaErrores.add("Error Sintactico");
            }
        }
    }

    public void detectarNoTerminal(String noTerminal) {
        if (noTerminal.equals("<INICIO>")) {
            INICIO();
        } else if (noTerminal.equals("<VAR>")) {
            VAR();
        } else if (noTerminal.equals("<TIPO>")) {
            TIPO();
        } else if (noTerminal.equals("<INSTRUCCIONES>")) {
            INSTRUCCIONES();
        } else if (noTerminal.equals("<MINSTRUCCION>")) {
            MINSTRUCCION();
        } else if (noTerminal.equals("<INSTRUCCION>")) {
            INSTRUCCION();
        } else if (noTerminal.equals("<IF>")) {
            IF();
        } else if (noTerminal.equals("<ELSE>")) {
            ELSE();
        } else if (noTerminal.equals("<FOR>")) {
            FOR();
        } else if (noTerminal.equals("<WHILE>")) {
            WHILE();
        } else if (noTerminal.equals("<IN>")) {
            IN();
        } else if (noTerminal.equals("<OUT>")) {
            OUT();
        } else if (noTerminal.equals("<ASIGNA>")) {
            ASIGNA();
        } else if (noTerminal.equals("<OPR>")) {
            OPR();
        } else if (noTerminal.equals("<EXP>")) {
            EXP();
        } else if (noTerminal.equals("<T>")) {
            T();
        } else if (noTerminal.equals("<F>")) {
            F();
        } else if (noTerminal.equals("<R>")) {
            R();
        } else if (noTerminal.equals("<BLOQUE>")) {
            BLOQUE();
        } else if (noTerminal.equals("<OP>")) {
            OP();
        }
    }

    public void analizarSintaxis() {
        INICIO();
    }

}
