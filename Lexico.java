import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexico {
    static Map<String, String> tokens = new HashMap<String, String>() {
        {
            put("si", "si");
            put("sino", "sino");
            put("para", "para");
            put("mientras", "mientras");
            put("entero", "entero");
            put("cadena", "cadena");
            put("entrada", "entrada");
            put("salida", "salida");
            put("flotante", "flotante");
            put("+", "+");
            put("-", "-");
            put("*", "*");
            put("/", "/");
            put("**", "**");
            put("%", "%");
            put(">", ">");
            put("<", "<");
            put(">=", ">=");
            put("<=", "<=");
            put("==", "==");
            put("!=", "!=");
            put("=", "=");
            put("(", "(");
            put(")", ")");
            put(";", ";");
            put("{", "{");
            put("}", "}");
            put("/-/", "comentario");
            put("++", "++");
        }
    };

    public static boolean esNumero(String lexema) {
        Pattern pattern = Pattern.compile("^([0-9]+)$");
        Matcher matcher = pattern.matcher(lexema);
        return matcher.matches();
    }

    public static boolean esFlotante(String lexema) {
        Pattern pattern = Pattern.compile("^[+-]?([0-9]*[.])?[0-9]+$");
        Matcher matcher = pattern.matcher(lexema);
        return matcher.matches();
    }

    public static boolean esIdentificador(String lexema) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_]+)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(lexema);
        return matcher.matches();
    }

    public static boolean esCadena(String lexema) {
        Pattern pattern = Pattern.compile("^(\"[a-zA-Z0-9_@$\\-%+\\/#!?\"<¡>\\(\\)¿=|°{},;.\\[\\]]*\")$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(lexema);
        return matcher.matches();
    }

    public static void validar(String lexema, int linea, Tabla tablaSimbolos, List<String> pilaErrores) {
        if (tokens.containsKey(lexema)) {
            tablaSimbolos.insertar("", lexema, tokens.get(lexema), "");
        } else if (esNumero(lexema)) {
            tablaSimbolos.insertar("", lexema, "numero", "0");
        } else if (esIdentificador(lexema)) {
            tablaSimbolos.insertar("var", lexema, "identificador", "nulo");
        } else if (esFlotante(lexema)) {
            tablaSimbolos.insertar("", lexema, "flotante", "0.0");
        } else if (esCadena(lexema)) {
            tablaSimbolos.insertar("", lexema, "cadena", "");
        } else {
            if (!lexema.startsWith("/-/")) {
                pilaErrores.add("Error lexico linea: " + linea + ". " + lexema + " no pertenece al lenguaje");
            }
        }
    }
}
