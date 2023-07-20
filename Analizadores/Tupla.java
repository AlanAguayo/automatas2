public class Tupla {
    private String id;
    private String token;
    private String lexema;
    private String valor;

    public Tupla(String id, String token, String lexema, String valor) {
        this.id = id;
        this.token = token;
        this.lexema = lexema;
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getLexema() {
        return lexema;
    }

    public String getValor() {
        return valor;
    }

    public String select() {
        return "[" + id + "] [" + token + "] [" + lexema + "] [" + valor + "]";
    }
}
