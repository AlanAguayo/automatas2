public class Tradutor {
    String[] variables;
    public static void ejecutar(String[] codigo){
        for (String linea : codigo) {
            if(linea.contains("entero")){
                System.out.println("entero");
            }
            if(linea.contains("si")){
                si(linea);
            }
        }
    }

    public static void variable(String linea){
        System.out.println("variable");
    }

    public static void si(String linea){

    }
}
