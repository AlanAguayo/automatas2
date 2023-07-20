import java.util.ArrayList;
import java.util.List;

public class Tabla {
    private int posicion = 1;
    private List<Tupla> tabla;

    public Tabla() {
        tabla = new ArrayList<>();
    }

    public void insertar(String cadena, String token, String lexema, String valor) {
        if (!cadena.isEmpty()) {
            cadena += String.valueOf(posicion);
            tabla.add(new Tupla(cadena, token, lexema, valor));
        } else {
            tabla.add(new Tupla(String.valueOf(posicion), token, lexema, valor));
        }
        posicion++;
    }

    public void imprimirTabla() {
        System.out.println("[ID] [TOKEN] [SIGNIFICADO] [VALOR]\n");
        for (Tupla tupla : tabla) {
            System.out.println(tupla.select() + "\n");
        }
    }

    public void actualizar(String variable, String valor) {
        List<Tupla> tabla = new ArrayList<>();
        for (Tupla tupla : tabla) {
            if (tupla.getToken().contains(variable)) {
                tabla.add(new Tupla(tupla.getId(), tupla.getToken(), tupla.getLexema(), valor));
            } else {
                tabla.add(new Tupla(tupla.getId(), tupla.getToken(), tupla.getLexema(), tupla.getValor()));
            }
        }
        this.tabla = tabla;
    }

    public int getTamano() {
        return tabla.size();
    }

    public String getElemento(int pos) {
        try {
            if (getTamano() == 0) {
                return "";
            } else {
                return tabla.get(pos).getToken();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getElementoReal(int pos) {
        try {
            if (getTamano() == 0) {
                return "";
            } else {
                return tabla.get(pos).getToken();
            }
        } catch (Exception e) {
            System.out.println("Error de Sintaxis: Instruccion abierta");
            return "";
        }
    }

    public void vaciar() {
        tabla.clear();
    }

    public List<String> filtrarVariablesNombres() {
        List<String> variables = new ArrayList<>();
        for (Tupla tupla : tabla) {
            if (tupla.getId().contains("var")) {
                if (!variables.contains(tupla.getToken())) {
                    variables.add(tupla.getToken());
                }
            }
        }
        return variables;
    }

    public List<String> filtrarVariablesValor() {
        List<String> variables = new ArrayList<>();
        for (Tupla tupla : tabla) {
            if (tupla.getId().contains("var")) {
                String variableValor = tupla.getToken() + " " + tupla.getValor();
                if (!variables.contains(variableValor)) {
                    variables.add(variableValor);
                }
            }
        }
        return variables;
    }

}
