package analizador;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.table.DefaultTableModel;

public class GraphAnalizador extends javax.swing.JFrame {

    String lineaActual;
    String[] lineas = new String[100];
    String[] lineasAux = new String[100];
    String[] tokens = new String[100];
    String[] tokensAux = new String[100];
    String[][] tablaErrores = new String[100][2];
    String[][] tablaSimbolos = new String[100][5];
    int contErrores = 0;
    int contSimbolos = 0;
    int contLineas = 1;
    int contTokens = 1;
    int contId = 1;
    boolean banderaSimbolo = false;
    boolean banderaVar = false;
    boolean banderaOp = false;
    boolean banderaConst = false;
    boolean banderaError = false;
    boolean banderaFor = false;
    String tipoDatos[] = { "entero", "flotante", "cadena" };
    String operadores[] = { "+", "-", "*", "/", "%", "=", "++", "--" };
    DefaultTableModel modelo1, modelo2;
    int entero;
    double flotante;
    String cadena;
    String tipo = "";
    String archivo = "sources\\codigoBien.txt";

    /*
     * Parte de tripletas
     * String[][] tablaopsides = new String[5][2];
     * String[][] tablaopsrelides = new String[5][2];
     * String[][] tablatripletas = new String[100][4];
     * String[] lineasTripletas = new String[100];
     * int contTokensTripletas = 1;
     * int contLineaTripletas = 0;
     * int contTripletas = 0;
     * int contT = 1;
     * int contC = 1;
     * int contTR = 1;
     * int apuntadorFor = 0;
     * int apuntadorForCiclo = 0;
     * DefaultTableModel modelo3;
     */

    public GraphAnalizador() {
        initComponents();
        modelo1 = (DefaultTableModel) jTable1.getModel();
        modelo2 = (DefaultTableModel) jTable2.getModel();
        /* Parte de tripletas
        modelo3 = (DefaultTableModel) jTable3.getModel();
        */

        try {
            FileInputStream fileInputStream = new FileInputStream(archivo);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineaCodigo;
            BufferedReader lectura;
            try {
                lectura = new BufferedReader(new FileReader(archivo));
                String codigo;
                while (lectura.ready()) {
                    codigo = lectura.readLine();
                    jTextArea1.setText(jTextArea1.getText() + codigo + "\n");
                    //jTextArea1.setText(jTextArea1.getText() + contLineas + ":      " + codigo + "\n");
                    contLineas++;
                }
            } catch (IOException e) {
            }
            contLineas = 0;

            while ((lineaCodigo = bufferedReader.readLine()) != null) {
                lineaActual = lineaCodigo;
                contLineas++;

                for (int i = 0; i < lineaActual.length(); i++)
                    lineas[i] = "" + lineaActual.charAt(i);

                for (int i = 0; i < lineaActual.length(); i++) {
                    if (" ".equals(lineas[i]))
                        contTokens++;
                }
                for (int i = 0; i < contTokens; i++)
                    tokens[i] = "";
                contTokens = 0;

                // --Agrega los tokens al arreglo--//
                for (int i = 0; i < lineaActual.length(); i++) {
                    if (!" ".equals(lineas[i])) {
                        if (";".equals(lineas[i + 1])) {
                            tokens[contTokens] = "" + tokens[contTokens] + lineas[i];
                            contTokens++;
                            tokens[contTokens] = "" + lineas[i + 1];
                            i++;
                        } else
                            tokens[contTokens] = "" + tokens[contTokens] + lineas[i];
                    } else
                        contTokens++;
                }

                // --Recorre cada token--//
                for (int i = 0; (i < contTokens + 1 && !";".equals(tokens[i])); i++) {
                    banderaVar = false;
                    banderaOp = false;
                    banderaConst = false;

                    // --Revisa si se define una variable con un tipo de dato (int, boolean,
                    // double...)--//
                    for (int x = 0; (x < tipoDatos.length && banderaSimbolo == false); x++) {
                        if (tokens[i].equals(tipoDatos[x])) {
                            tablaSimbolos[contSimbolos][0] = "" + contLineas;
                            tablaSimbolos[contSimbolos][1] = tokens[0];
                            tablaSimbolos[contSimbolos][2] = tokens[1];
                            if (!";".equals(tokens[2]))
                                tablaSimbolos[contSimbolos][3] = tokens[3];
                            tablaSimbolos[contSimbolos][4] = "IDE0" + contId;
                            contId++;
                            contSimbolos++;
                            banderaSimbolo = true;
                        }

                        // --Detecta que los tipos de datos sean compatibles--//
                        // --Deteccion de enteros--//
                        if (!";".equals(tokens[2])) {
                            if ("entero".equals(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3]))
                                    && banderaError == false) {

                                try {
                                    entero = Integer.parseInt(tokens[3]);
                                } catch (NumberFormatException e) {
                                    tablaErrores[contErrores][0] = "" + contLineas;
                                    tablaErrores[contErrores][1] = "Tipo de variable (int) incompatible";
                                    contErrores++;
                                    banderaError = true;
                                }

                            } else

                            // --Deteccion de booleanos--//
                            if ("boolean".equals(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3]))
                                    && banderaError == false) {
                                if (!"true".equals(tokens[3]) && !"false".equals(tokens[3])) {
                                    tablaErrores[contErrores][0] = "" + contLineas;
                                    tablaErrores[contErrores][1] = "Tipo de variable (boolean) incompatible";
                                    contErrores++;
                                    banderaError = true;
                                }
                            } else

                            // --Deteccion de decimales--//
                            if ("flotante".equals(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3]))
                                    && banderaError == false) {
                                try {
                                    flotante = Double.parseDouble(tokens[3]);
                                } catch (NumberFormatException e) {
                                    tablaErrores[contErrores][0] = "" + contLineas;
                                    tablaErrores[contErrores][1] = "Tipo de variable (double) incompatible";
                                    contErrores++;
                                    banderaError = true;
                                }
                            } else

                            // --Deteccion de Strings--//
                            if ("cadena".equals(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3]))
                                    && banderaError == false) {
                                if (tokens[3].charAt(0) != '"' || tokens[3].charAt(tokens[3].length() - 1) != '"') {
                                    tablaErrores[contErrores][0] = "" + contLineas;
                                    tablaErrores[contErrores][1] = "Tipo de variable (String) incompatible o mal definida";
                                    contErrores++;
                                    banderaError = true;
                                }
                            }
                        }
                    }

                    // --Revision y correccion del ciclo FOR--//
                    if ("para".equalsIgnoreCase(tokens[0])) {
                        banderaFor = true;
                        if (tokens[1].charAt(0) != '(') {
                            tablaErrores[contErrores][0] = "" + contLineas;
                            tablaErrores[contErrores][1] = "No se ha abierto el parentesis para el ciclo para";
                            contErrores++;
                        } else {
                            if (!";".equals(tokens[6]) || !";".equals(tokens[10])) {
                                tablaErrores[contErrores][0] = "" + contLineas;
                                tablaErrores[contErrores][1] = "Falta el ';' del separador del ciclo para";
                                contErrores++;
                                banderaError = true;
                            }
                        }
                        if (tokens[contTokens].charAt(tokens[contTokens].length() - 1) != ')') {
                            tablaErrores[contErrores][0] = "" + contLineas;
                            tablaErrores[contErrores][1] = "No se ha cerrado el parentesis para el ciclo para";
                            contErrores++;
                        }
                        i = contTokens;
                    }
                    if ("finPara".equalsIgnoreCase(tokens[0])) {
                        if (banderaFor == false) {
                            tablaErrores[contErrores][0] = "" + contLineas;
                            tablaErrores[contErrores][1] = "No se ha abierto el ciclo para";
                            contErrores++;
                        } else
                            banderaFor = false;
                    }

                    // --Revisa si existe alguna variable--//
                    for (int x = 0; (x < tablaSimbolos.length && banderaSimbolo == false); x++) {
                        if (tokens[i].equals(tablaSimbolos[x][2]))
                            banderaVar = true;
                    }

                    // --Ignora operadores--//
                    for (int x = 0; (x < operadores.length && banderaSimbolo == false); x++) {
                        if (tokens[i].equals(operadores[x]))
                            banderaOp = true;
                    }

                    // --Ignora constantes--//
                    try {
                        entero = Integer.parseInt(tokens[i]);
                        banderaConst = true;
                    } catch (NumberFormatException e) {
                    }
                    try {
                        flotante = Double.parseDouble(tokens[i]);
                        banderaConst = true;
                    } catch (NumberFormatException e) {
                    }
                    if (tokens[i].charAt(0) == '"' && tokens[i].charAt(tokens[i].length() - 1) == '"') {
                        try {
                            cadena = String.valueOf((tokens[i]));
                            banderaConst = true;
                        } catch (NumberFormatException e) {
                        }
                    }

                    // --Checa los tokens excluidos--//
                    if (banderaSimbolo == false && !"FOR".equals(tokens[0]) && !"ENDFOR".equals(tokens[0])) {
                        if (banderaVar == false && banderaOp == false && banderaConst == false) {
                            tablaErrores[contErrores][0] = "" + contLineas;
                            tablaErrores[contErrores][1] = "Variable " + tokens[i] + " no encontrada";
                            contErrores++;
                            banderaError = true;
                        }
                    }

                    // --Deteccion de tipos incompatibles--//
                    for (int x = 0; (x < tablaSimbolos.length && banderaSimbolo == false); x++) {
                        if (tokens[i].equals(tablaSimbolos[x][2])) {
                            if (!"".equals(tipo) && !tablaSimbolos[x][1].equals(tipo)) {
                                tablaErrores[contErrores][0] = "" + contLineas;
                                tablaErrores[contErrores][1] = "Tipo de variables incompatible";
                                contErrores++;
                                banderaError = true;
                            }
                            tipo = tablaSimbolos[x][1];
                        }
                    }
                }

                // --Revisa error si termina con ;--//
                if (!";".equals(tokens[contTokens]) && !"FOR".equals(tokens[0]) && !"ENDFOR".equals(tokens[0])) {
                    tablaErrores[contErrores][0] = "" + contLineas;
                    tablaErrores[contErrores][1] = "Hace falta ;";
                    contErrores++;
                }

                /*
                 * Parte de las tripletas
                 * if (banderaerror == false) {
                 * lineasTripletas[contLineaTripletas] = lineaActual;
                 * contLineaTripletas++;
                 * }
                 */

                banderaSimbolo = false;
                banderaVar = false;
                banderaOp = false;
                banderaConst = false;
                banderaError = false;
                tipo = "";
            }
            if (banderaFor == true) {
                tablaErrores[contErrores][0] = "" + contLineas;
                tablaErrores[contErrores][1] = "El ciclo FOR no tiene un ENDFOR;";
                contErrores++;
            }
            fileInputStream.close();
        } catch (IOException e) {
        }

        /*
         * Parte de tripletas
         * tablaopsides[0][0] = "+";
         * tablaopsides[0][1] = "OPA01";
         * tablaopsides[1][0] = "-";
         * tablaopsides[1][1] = "OPA02";
         * tablaopsides[2][0] = "*";
         * tablaopsides[2][1] = "OPA03";
         * tablaopsides[3][0] = "/";
         * tablaopsides[3][1] = "OPA04";
         * tablaopsides[4][0] = "%";
         * tablaopsides[4][1] = "OPA05";
         * 
         * tablaopsrelides[0][0] = "=";
         * tablaopsrelides[0][1] = "OPR01";
         * tablaopsrelides[1][0] = "<";
         * tablaopsrelides[1][1] = "OPR02";
         * tablaopsrelides[2][0] = ">";
         * tablaopsrelides[2][1] = "OPR03";
         * tablaopsrelides[3][0] = "<=";
         * tablaopsrelides[3][1] = "OPR04";
         * tablaopsrelides[4][0] = ">=";
         * tablaopsrelides[4][1] = "OPR05";
         * 
         * for (int x = 0; x < contLineaTripletas; x++) {
         * System.out.println(lineasTripletas[x]);
         * //--Separa todos los caracteres de la linea--//
         * for (int i = 0; i < lineasTripletas[x].length(); i++) lineasAux[i] = "" +
         * lineasTripletas[x].charAt(i);
         * //--Cuenta los espacios para saber el numero de tokens--//
         * for (int i = 0; i < lineasTripletas[x].length(); i++) {
         * if (" ".equals(lineasAux[i])) contTokensTripletas++;
         * }
         * //--Asigna y libera un espacio a los tokens--//
         * for (int i = 0; i < contTokensTripletas; i++) tokensAux[i] = "";
         * contTokensTripletas = 0;
         * //--Agrega los tokens al arreglo--//
         * for (int i = 0; i < lineasTripletas[x].length(); i++) {
         * if (!" ".equals(lineasAux[i])) {
         * if (";".equals(lineasAux[i+1])) {
         * tokensAux[contTokensTripletas] = "" + tokensAux[contTokensTripletas] +
         * lineasAux[i];
         * contTokensTripletas++;
         * tokensAux[contTokensTripletas] = "" + lineasAux[i+1];
         * i++;
         * } else tokensAux[contTokensTripletas] = "" + tokensAux[contTokensTripletas] +
         * lineasAux[i];
         * } else contTokensTripletas++;
         * }
         * //--Recorre cada token de las tripletas--//
         * for (int i = 0; (i < contTokensTripletas+1 && !";".equals(tokensAux[i]));
         * i++) {
         * for (String tipodato : tipodatos) {
         * if (tokensAux[0].equals(tipodato)) {
         * tablatripletas[contTripletas][0] = "" + (contTripletas);
         * tablatripletas[contTripletas][1] = "T" + contT;
         * tablatripletas[contTripletas][2] = "CE" + contC;
         * tablatripletas[contTripletas][3] = "=";
         * tablatripletas[contTripletas+1][0] = "" + (contTripletas + 1);
         * for (String[] tablasimbolo : tablaSimbolos) {
         * if (tokensAux[1].equals(tablasimbolo[2])) {
         * tablatripletas[contTripletas+1][1] = tablasimbolo[4];
         * }
         * }
         * tablatripletas[contTripletas+1][2] = "T" + contT;
         * tablatripletas[contTripletas+1][3] = "OPR01";
         * for (String[] tablaopsrelide : tablaopsrelides) {
         * if (tokensAux[2].equals(tablaopsrelide[0])) {
         * tablatripletas[contTripletas+1][3] = tablaopsrelide[1];
         * }
         * }
         * contT++;
         * contC++;
         * contTripletas++;contTripletas++;
         * i = contTokensTripletas+1;
         * }
         * }
         * 
         * if (";".equals(tokensAux[3])) {
         * tablatripletas[contTripletas][0] = "" + (contTripletas);
         * tablatripletas[contTripletas][1] = "T" + contT;
         * for (String[] tablasimbolo : tablaSimbolos) {
         * if (tokensAux[2].equals(tablasimbolo[2])) {
         * tablatripletas[contTripletas][2] = tablasimbolo[4];
         * }
         * }
         * if ("".equals(tablatripletas[contTripletas][2]) ||
         * tablatripletas[contTripletas][2] ==
         * null) {
         * tablatripletas[contTripletas][2] = "CE" + contC;
         * contC++;
         * }
         * tablatripletas[contTripletas][3] = "=";
         * tablatripletas[contTripletas+1][0] = "" + (contTripletas + 1);
         * for (String[] tablasimbolo : tablaSimbolos) {
         * if (tokensAux[0].equals(tablasimbolo[2])) {
         * tablatripletas[contTripletas+1][1] = tablasimbolo[4];
         * }
         * }
         * tablatripletas[contTripletas+1][2] = tablatripletas[contTripletas][1];
         * tablatripletas[contTripletas+1][3] = tablaopsrelides[0][1];
         * contT++;
         * contTripletas++;contTripletas++;
         * i = contTokensTripletas+1;
         * }
         * 
         * if (";".equals(tokensAux[5])) {
         * tablatripletas[contTripletas][0] = "" + (contTripletas);
         * tablatripletas[contTripletas][1] = "T" + contT;
         * for (String[] tablasimbolo : tablaSimbolos) {
         * if (tokensAux[2].equals(tablasimbolo[2])) {
         * tablatripletas[contTripletas][2] = tablasimbolo[4];
         * }
         * }
         * if ("".equals(tablatripletas[contTripletas][2]) ||
         * tablatripletas[contTripletas][2] ==
         * null) {
         * tablatripletas[contTripletas][2] = "CE" + contC;
         * contC++;
         * }
         * tablatripletas[contTripletas][3] = "=";
         * if ((("+".equals(tokensAux[3]) || "-".equals(tokensAux[3])) &&
         * "0".equals(tokensAux[4])) ||
         * (("*".equals(tokensAux[3]) || "/".equals(tokensAux[3])) &&
         * "1".equals(tokensAux[4]))) {
         * } else {
         * tablatripletas[contTripletas+1][0] = "" + (contTripletas + 1);
         * tablatripletas[contTripletas+1][1] = tablatripletas[contTripletas][1];
         * for (String[] tablasimbolo : tablaSimbolos) {
         * if (tokensAux[4].equals(tablasimbolo[2])) {
         * tablatripletas[contTripletas+1][2] = tablasimbolo[4];
         * }
         * }
         * if ("".equals(tablatripletas[contTripletas+1][2]) ||
         * tablatripletas[contTripletas+1][2]
         * == null) {
         * tablatripletas[contTripletas+1][2] = "CE" + contC;
         * contC++;
         * }
         * for (String[] tablaopside : tablaopsides) {
         * if (tokensAux[3].equals(tablaopside[0])) {
         * tablatripletas[contTripletas+1][3] = tablaopside[1];
         * }
         * }
         * tablatripletas[contTripletas+2][0] = "" + (contTripletas + 2);
         * for (String[] tablasimbolo : tablaSimbolos) {
         * if (tokensAux[0].equals(tablasimbolo[2])) {
         * tablatripletas[contTripletas+2][1] = tablasimbolo[4];
         * }
         * }
         * tablatripletas[contTripletas+2][2] = tablatripletas[contTripletas][1];
         * tablatripletas[contTripletas+2][3] = tablaopsrelides[0][1];
         * contT++;
         * contTripletas++;contTripletas++;contTripletas++;
         * i = contTokensTripletas+1;
         * }
         * }
         * if ("FOR".equals(tokensAux[0])) {
         * apuntadorForCiclo = contTripletas;
         * for (String tipodato : tipodatos) {
         * if (tokensAux[2].equals(tipodato)) {
         * tablatripletas[contTripletas][0] = "" + (contTripletas);
         * tablatripletas[contTripletas][1] = "T" + contT;
         * tablatripletas[contTripletas][2] = "CE" + contC;
         * tablatripletas[contTripletas][3] = "=";
         * tablatripletas[contTripletas+1][0] = "" + (contTripletas + 1);
         * tablatripletas[contTripletas+1][1] = "IDETEMP";
         * tablatripletas[contTripletas+1][2] = "T" + contT;
         * tablatripletas[contTripletas+1][3] = "OPR01";
         * for (String[] tablaopsrelide : tablaopsrelides) {
         * if (tokensAux[4].equals(tablaopsrelide[0])) {
         * tablatripletas[contTripletas+1][3] = tablaopsrelide[1];
         * }
         * }
         * contT++;
         * contC++;
         * contTripletas++;contTripletas++;
         * i = contTokensTripletas+1;
         * }
         * }
         * 
         * tablatripletas[contTripletas][0] = "" + (contTripletas);
         * tablatripletas[contTripletas][1] = "T" + contT;
         * tablatripletas[contTripletas][2] = "CE" + contC;
         * tablatripletas[contTripletas][3] = "=";
         * contC++;
         * contT++;
         * contTripletas++;
         * tablatripletas[contTripletas][0] = "" + (contTripletas);
         * tablatripletas[contTripletas][1] = "T" + (contT);
         * tablatripletas[contTripletas][2] = "T" + (contT-1);
         * for (String[] tablaopsrelide : tablaopsrelides) {
         * if (tokensAux[8].equals(tablaopsrelide[0])) {
         * tablatripletas[contTripletas][3] = tablaopsrelide[1];
         * }
         * }
         * contT++;
         * contC++;
         * contTripletas++;
         * 
         * tablatripletas[contTripletas][0] = "" + (contTripletas);
         * tablatripletas[contTripletas][1] = "TR" + (contTR);
         * tablatripletas[contTripletas][2] = "TRUE";
         * tablatripletas[contTripletas][3] = "" + (contTripletas + 2);
         * contTripletas++;
         * tablatripletas[contTripletas][0] = "" + (contTripletas);
         * tablatripletas[contTripletas][1] = "TR" + (contTR);
         * tablatripletas[contTripletas][2] = "FALSE";
         * apuntadorfor = contTripletas;
         * contTripletas++;
         * contTR++;
         * 
         * }
         * if ("ENDFOR".equals(tokensAux[0])) {
         * tablatripletas[contTripletas][0] = "" + (contTripletas);
         * tablatripletas[contTripletas][1] = tablatripletas[apuntadorFor - 5][1];
         * tablatripletas[contTripletas][2] = tablatripletas[apuntadorFor - 5][2];
         * tablatripletas[contTripletas][3] = "OPA01";
         * contTripletas++;
         * tablatripletas[contTripletas][0] = "" + (contTripletas);
         * tablatripletas[contTripletas][1] = "JP";
         * tablatripletas[contTripletas][3] = "" + (apuntadorForCiclo + 1);
         * tablatripletas[apuntadorfor][3] = "" + (contTripletas);
         * contTripletas++;
         * }
         * }
         * }
         */

    }

        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelGeneral = new javax.swing.JPanel();
        PanelBoton = new javax.swing.JPanel();
        BtnDesplegar = new javax.swing.JButton();
        PanelSimbolos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelCodigo = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        PanelErrores = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        PanelTripletas = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Analizador");
        setBackground(new java.awt.Color(18, 150, 200));
        setForeground(new java.awt.Color(18, 150, 200));
        setResizable(false);

        PanelGeneral.setBackground(new java.awt.Color(18, 150, 200));

        PanelBoton.setBackground(new java.awt.Color(18, 150, 200));
        PanelBoton.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Seleccion de despliegue", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        BtnDesplegar.setBackground(new java.awt.Color(0, 0, 0));
        BtnDesplegar.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 14)); // NOI18N
        BtnDesplegar.setForeground(new java.awt.Color(240, 240, 240));
        BtnDesplegar.setText("Desplegar Datos");
        BtnDesplegar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnDesplegarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout PanelBotonLayout = new javax.swing.GroupLayout(PanelBoton);
        PanelBoton.setLayout(PanelBotonLayout);
        PanelBotonLayout.setHorizontalGroup(
            PanelBotonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBotonLayout.createSequentialGroup()
                .addContainerGap(294, Short.MAX_VALUE)
                .addComponent(BtnDesplegar)
                .addGap(303, 303, 303))
        );
        PanelBotonLayout.setVerticalGroup(
            PanelBotonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBotonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnDesplegar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelSimbolos.setBackground(new java.awt.Color(18, 150, 200));
        PanelSimbolos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Simbolos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        jTable1.setBackground(new java.awt.Color(0, 255, 102));
        jTable1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Linea", "Tipo de dato", "Variable", "Valor", "IDE"
            }
        ));
        jTable1.setEnabled(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(70);
        }

        javax.swing.GroupLayout PanelSimbolosLayout = new javax.swing.GroupLayout(PanelSimbolos);
        PanelSimbolos.setLayout(PanelSimbolosLayout);
        PanelSimbolosLayout.setHorizontalGroup(
            PanelSimbolosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSimbolosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelSimbolosLayout.setVerticalGroup(
            PanelSimbolosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSimbolosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelCodigo.setBackground(new java.awt.Color(18, 150, 200));
        PanelCodigo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Código", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout PanelCodigoLayout = new javax.swing.GroupLayout(PanelCodigo);
        PanelCodigo.setLayout(PanelCodigoLayout);
        PanelCodigoLayout.setHorizontalGroup(
            PanelCodigoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCodigoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelCodigoLayout.setVerticalGroup(
            PanelCodigoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCodigoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelErrores.setBackground(new java.awt.Color(18, 150, 200));
        PanelErrores.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Errores", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        jTable2.setBackground(new java.awt.Color(255, 51, 51));
        jTable2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Linea", "Error"
            }
        ));
        jTable2.setEnabled(false);
        jScrollPane3.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(50);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout PanelErroresLayout = new javax.swing.GroupLayout(PanelErrores);
        PanelErrores.setLayout(PanelErroresLayout);
        PanelErroresLayout.setHorizontalGroup(
            PanelErroresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelErroresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelErroresLayout.setVerticalGroup(
            PanelErroresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelErroresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelTripletas.setBackground(new java.awt.Color(18, 150, 200));
        PanelTripletas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tripletas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        jTable3.setBackground(new java.awt.Color(102, 204, 255));
        jTable3.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Dato Objeto", "Dato Fuente", "Operador"
            }
        ));
        jTable3.setEnabled(false);
        jScrollPane4.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setPreferredWidth(10);
        }

        javax.swing.GroupLayout PanelTripletasLayout = new javax.swing.GroupLayout(PanelTripletas);
        PanelTripletas.setLayout(PanelTripletasLayout);
        PanelTripletasLayout.setHorizontalGroup(
            PanelTripletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTripletasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelTripletasLayout.setVerticalGroup(
            PanelTripletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTripletasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );

        javax.swing.GroupLayout PanelGeneralLayout = new javax.swing.GroupLayout(PanelGeneral);
        PanelGeneral.setLayout(PanelGeneralLayout);
        PanelGeneralLayout.setHorizontalGroup(
            PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelGeneralLayout.createSequentialGroup()
                        .addComponent(PanelSimbolos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PanelErrores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(PanelBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelTripletas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelGeneralLayout.setVerticalGroup(
            PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PanelGeneralLayout.createSequentialGroup()
                        .addComponent(PanelBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PanelSimbolos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelErrores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(PanelTripletas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(PanelGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(PanelGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnDesplegarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnDesplegarMouseClicked
        int filas = modelo1.getRowCount();
        for (int i = 1; i <= filas; i++) {
            modelo1.removeRow(0);
        }
        for (int i = 0; i <contSimbolos; i++) {
            modelo1.addRow(new Object[]{tablaSimbolos[i][0], tablaSimbolos[i][1], tablaSimbolos[i][2],
                                        tablaSimbolos[i][3], tablaSimbolos[i][4]});
        }
        int filas2 = modelo2.getRowCount();
        for (int i = 1; i <= filas2; i++) {
            modelo2.removeRow(0);
        }
        for (int i = 0; i <contErrores; i++) {
            modelo2.addRow(new Object[]{tablaErrores[i][0], tablaErrores[i][1]});
        }
        /* 
        int filas3 = modelo3.getRowCount();
        for (int i = 1; i <= filas3; i++) {
            modelo3.removeRow(0);
        }
        for (int i = 0; i <conttrip; i++) {
            modelo3.addRow(new Object[]{tablatripletas[i][0], tablatripletas[i][1], tablatripletas[i][2], tablatripletas[i][3]});
        }
        */
    }//GEN-LAST:event_BtnDesplegarMouseClicked

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GraphAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GraphAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GraphAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GraphAnalizador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GraphAnalizador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnDesplegar;
    private javax.swing.JPanel PanelBoton;
    private javax.swing.JPanel PanelCodigo;
    private javax.swing.JPanel PanelErrores;
    private javax.swing.JPanel PanelGeneral;
    private javax.swing.JPanel PanelSimbolos;
    private javax.swing.JPanel PanelTripletas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}