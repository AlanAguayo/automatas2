import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lexico {
    
    public static boolean isComment(String token) {
        String[] keywords = {"/-/","/*/","si","sino","para","mientras","entrada","salida"};
        for (String keyword : keywords) {
            if (token.equals(keyword)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isParenth(String token) {
        String[] keywords = {"(", ")"}; 

        for (String keyword : keywords) {
            if (token.equals(keyword)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isKeyWord(String token) {
        String[] keywords = {"si","sino", "para", "mientras" }; // Ejemplo de palabras clave

        for (String keyword : keywords) {
            if (token.equals(keyword)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isComparator(String token) {
        String[] keywords = {">","<", ">=", "<=", "==","!="}; // Ejemplo de palabras clave

        for (String keyword : keywords) {
            if (token.equals(keyword)) {
                return true;
            }
        }

        return false;
    }
    

    public static boolean isIdentifier(String token) {
        // Implementar lógica para verificar si es un identificador válido
        return token.matches("[a-zA-Z][a-zA-Z0-9]*");
    }

    public static boolean isNumber(String token) {
        // Implementar lógica para verificar si es un número válido
        return token.matches("[0-9]+");
    }

    public static boolean isFloat(String number) {
        try {
            Float.parseFloat(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isOperator(String token) {
        String[] operators = {"+", "-", "*", "/"}; // Ejemplo de operadores

        for (String operator : operators) {
            if (token.equals(operator)) {
                return true;
            }
        }

        return false;
    }
}



