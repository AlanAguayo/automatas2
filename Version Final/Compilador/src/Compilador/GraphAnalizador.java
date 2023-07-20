package Compilador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GraphAnalizador extends javax.swing.JFrame {

    //EMPIEZA VERO
  /*Variables globales, para tokens, errores, simbolos, banderas, lineas de codigo  
   operadores, tipo de variables y ruta de archivos 
  */public static String lineaActual;
    public static String[] lineas = new String[100];
    public static String[] lineasAux = new String[100];
    public static String[] tokens = new String[100];
    public static String[] tokensAux = new String[100];
    public static String[][] tablaErrores = new String[100][2];
    public static String[][] tablaSimbolos = new String[100][5];
    public static int contErrores = 0;
    public static int contSimbolos = 0;
    public static int contLineas = 1;
    public static int contTokens = 1;
    public static int contId = 1;
    public static boolean banderaSimbolo = false;
    public static boolean banderaVar = false;
    public static boolean banderaOp = false;
    public static boolean banderaConst = false;
    public static boolean banderaError = false;
    public static boolean banderaFor = false;
    public static String tipoDatos[] = {"entero", "flotante", "cadena"};
    public static String operadores[] = {"+", "-", "*", "/", "%", "=", "++", "--"};
    public static DefaultTableModel modelo1, modelo2;
    public static int entero;
    public static double flotante;
    public static String cadena;
    public static String tipo = "";
    public static String archivo = "sources\\codigoBien.txt";

    public GraphAnalizador() {
        initComponents();
        modelo1 = (DefaultTableModel) jTable1.getModel();
        modelo2 = (DefaultTableModel) jTable2.getModel();
//Abrir archivo
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
                    contLineas++;
                }
            } catch (IOException e) {
            }
            contLineas = 0;

            //Leer lineas para guardar en un arreglo
            while ((lineaCodigo = bufferedReader.readLine()) != null) {
                lineaActual = lineaCodigo;
                contLineas++;

                for (int i = 0; i < lineaActual.length(); i++) {
                    lineas[i] = "" + lineaActual.charAt(i);
                }

                for (int i = 0; i < lineaActual.length(); i++) {
                    if (" ".equals(lineas[i])) {
                        contTokens++;
                    }
                }
//Inicializa el arreglo de Tokens
                for (int i = 0; i < contTokens; i++) {
                    tokens[i] = "";
                }
                contTokens = 0;
        
                for (int i = 0; i < lineaActual.length(); i++) {
                    if (!" ".equals(lineas[i])) {
                        if (";".equals(lineas[i + 1])) {
                            tokens[contTokens] = "" + tokens[contTokens] + lineas[i];
                            contTokens++;
                            tokens[contTokens] = "" + lineas[i + 1];
                            i++;
                        } else {
                            tokens[contTokens] = "" + tokens[contTokens] + lineas[i];
                        }
                    } else {
                        contTokens++;
                    }
                }
//TERMINA VERO
////////////////////////////////
//EMPIEZA ALAN
//Comparaciones de los tipos de operadores, operandos y tipo de variables
                for (int i = 0; (i < contTokens + 1 && !";".equals(tokens[i])); i++) {
                    banderaVar = false;
                    banderaOp = false;
                    banderaConst = false;

                    for (int x = 0; (x < tipoDatos.length && banderaSimbolo == false); x++) {
                        if (tokens[i].equals(tipoDatos[x])) {
                            tablaSimbolos[contSimbolos][0] = "" + contLineas;
                            tablaSimbolos[contSimbolos][1] = tokens[0];
                            tablaSimbolos[contSimbolos][2] = tokens[1];
                            if (!";".equals(tokens[2])) {
                                tablaSimbolos[contSimbolos][3] = tokens[3];
                            }
                            tablaSimbolos[contSimbolos][4] = "ID" + contId;
                            contId++;
                            contSimbolos++;
                            banderaSimbolo = true;
                        }
//Analizador Semantico
//Revisa si se tiene una variable entera
                        if (!";".equals(tokens[2])) {
                            if ("entero".equalsIgnoreCase(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3]))
                                    && banderaError == false) {

                                try {
                                    entero = Integer.parseInt(tokens[3]);
                                } catch (NumberFormatException e) {
                                    tablaErrores[contErrores][0] = "" + contLineas;
                                    tablaErrores[contErrores][1] = "Tipo de variable entero incompatible";
                                    contErrores++;
                                    banderaError = true;
                                }
//Revisa si se tiene una variable flotante
                            } else if ("flotante".equalsIgnoreCase(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3]))
                                    && banderaError == false) {
                                try {
                                    flotante = Double.parseDouble(tokens[3]);
                                } catch (NumberFormatException e) {
                                    tablaErrores[contErrores][0] = "" + contLineas;
                                    tablaErrores[contErrores][1] = "Tipo de variable flotante incompatible";
                                    contErrores++;
                                    banderaError = true;
                                }
 //Revisa si se tiene una variable cadena                               
                            } else if ("cadena".equalsIgnoreCase(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3]))
                                    && banderaError == false) {
                                if (tokens[3].charAt(0) != '"' || tokens[3].charAt(tokens[3].length() - 1) != '"') {
                                    tablaErrores[contErrores][0] = "" + contLineas;
                                    tablaErrores[contErrores][1] = "Tipo de variable cadena incompatible";
                                    contErrores++;
                                    banderaError = true;
                                }
                            }
                        }
                    }
//Nos saltamos esto
//Intento de revisar ciclos
                    if ("para".equalsIgnoreCase(tokens[0])) {
                        banderaFor = true;
                        if (tokens[1].charAt(0) != '(') {
                            tablaErrores[contErrores][0] = "" + contLineas;
                            tablaErrores[contErrores][1] = "No se ha abierto el parentesis para el ciclo para";
                            contErrores++;
                        } else if (!";".equals(tokens[6]) || !";".equals(tokens[10])) {
                            tablaErrores[contErrores][0] = "" + contLineas;
                            tablaErrores[contErrores][1] = "Falta el ';' del separador del ciclo para";
                            contErrores++;
                            banderaError = true;
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
                        } else {
                            banderaFor = false;
                        }
                    }
//Fin de semantico
//TERMINA ALAN
//////////////////////////////////////////////

//EMPIEZA JOSUE
//Analizador Lexico y sintactico
//Revisa la tabla de simbolos, si se se encuentra el simbolo correcto
                    for (int x = 0; (x < tablaSimbolos.length && banderaSimbolo == false); x++) {
                        if (tokens[i].equals(tablaSimbolos[x][2])) {
                            banderaVar = true;
                        }
                    }
//Operadores
                    for (int x = 0; (x < operadores.length && banderaSimbolo == false); x++) {
                        if (tokens[i].equals(operadores[x])) {
                            banderaOp = true;
                        }
                    }
//Sintactico, parsea a entero
                    try {
                        entero = Integer.parseInt(tokens[i]);
                        banderaConst = true;
                    } catch (NumberFormatException e) {
                    }
//parsea a flotante
                    try {
                        flotante = Double.parseDouble(tokens[i]);
                        banderaConst = true;
                    } catch (NumberFormatException e) {
                    }
//parsea a char, para las cadenas
                    if (tokens[i].charAt(0) == '"' && tokens[i].charAt(tokens[i].length() - 1) == '"') {
                        try {
                            cadena = String.valueOf((tokens[i]));
                            banderaConst = true;
                        } catch (NumberFormatException e) {
                        }
                    }
//Nos saltamos esto
//intento de ciclo
                    if (banderaSimbolo == false && !"para".equalsIgnoreCase(tokens[0]) && !"finpara".equalsIgnoreCase(tokens[0])) {
                        if (banderaVar == false && banderaOp == false && banderaConst == false) {
                            tablaErrores[contErrores][0] = "" + contLineas;
                            tablaErrores[contErrores][1] = "Variable " + tokens[i] + " no encontrada";
                            contErrores++;
                            banderaError = true;
                        }
                    }
//Semantico, si el tipo de variable es incompatible
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
//intento de ciclo
                if (!";".equals(tokens[contTokens]) && !"para".equalsIgnoreCase(tokens[0]) && !"finpara".equalsIgnoreCase(tokens[0])) {
                    tablaErrores[contErrores][0] = "" + contLineas;
                    tablaErrores[contErrores][1] = "Hace falta ;";
                    contErrores++;
                }
//Reinicia las banderas
                banderaSimbolo = false;
                banderaVar = false;
                banderaOp = false;
                banderaConst = false;
                banderaError = false;
          
                tipo = "";
            }
            imprimirLineas();
//Intento de ciclo  
            if (banderaFor == true) {
                tablaErrores[contErrores][0] = "" + contLineas;
                tablaErrores[contErrores][1] = "El ciclo para no tiene un fin;";
                contErrores++;
            }
            fileInputStream.close();
        } catch (IOException e) {
        }

    }
    
       //Fin del lexico y sintactico  
       //TERMINA JOSUE
//////////////////////////////////

//EMPIEZA ANTONIO
//Imprimir numero de lineas del codigo a compilar
    public void imprimirLineas(){
            String[] separacion = jTextArea1.getText().split("\n");
            String lineas = "";
            
            for(int i=1; i<separacion.length+1; i++){
                lineas += i+"\n";
            }
            this.jTextArea2.setText(lineas);
    }

    //Limpiador de variables
    public void limpiar() {
        lineas = new String[100];
        lineasAux = new String[100];
        tokens = new String[100];
        tokensAux = new String[100];
        tablaErrores = new String[100][2];
        tablaSimbolos = new String[100][5];
        contErrores = 0;
        contSimbolos = 0;
        contLineas = 1;
        contTokens = 1;
        contId = 1;
        banderaSimbolo = false;
        banderaVar = false;
        banderaOp = false;
        banderaConst = false;
        banderaError = false;
        banderaFor = false;
        tipo = "";
    }
//TERMINA ANTONIO
/////////////////////////////////////

//EMPIEZA JORGE
    //Abrir Archivos
    public void Abrir() {
        limpiar();
        try {
            jTextArea1.setText("");
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
                    contLineas++;
                }
            } catch (IOException e) {
            }
            contLineas = 0;

            while ((lineaCodigo = bufferedReader.readLine()) != null) {
                lineaActual = lineaCodigo;
                contLineas++;

                for (int i = 0; i < lineaActual.length(); i++) {
                    lineas[i] = "" + lineaActual.charAt(i);
                }

                for (int i = 0; i < lineaActual.length(); i++) {
                    if (" ".equals(lineas[i])) {
                        contTokens++;
                    }
                }
                for (int i = 0; i < contTokens; i++) {
                    tokens[i] = "";
                }
                contTokens = 0;

                for (int i = 0; i < lineaActual.length(); i++) {
                    if (!" ".equals(lineas[i])) {
                        if (";".equals(lineas[i + 1])) {
                            tokens[contTokens] = "" + tokens[contTokens] + lineas[i];
                            contTokens++;
                            tokens[contTokens] = "" + lineas[i + 1];
                            i++;
                        } else {
                            tokens[contTokens] = "" + tokens[contTokens] + lineas[i];
                        }
                    } else {
                        contTokens++;
                    }
                }

                for (int i = 0; (i < contTokens + 1 && !";".equals(tokens[i])); i++) {
                    banderaVar = false;
                    banderaOp = false;
                    banderaConst = false;

                    for (int x = 0; (x < tipoDatos.length && banderaSimbolo == false); x++) {
                        if (tokens[i].equals(tipoDatos[x])) {
                            tablaSimbolos[contSimbolos][0] = "" + contLineas;
                            tablaSimbolos[contSimbolos][1] = tokens[0];
                            tablaSimbolos[contSimbolos][2] = tokens[1];
                            if (!";".equals(tokens[2])) {
                                tablaSimbolos[contSimbolos][3] = tokens[3];
                            }
                            tablaSimbolos[contSimbolos][4] = "ID" + contId;
                            contId++;
                            contSimbolos++;
                            banderaSimbolo = true;
                        }

                        if (!";".equals(tokens[2])) {
                            if ("entero".equalsIgnoreCase(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3]))
                                    && banderaError == false) {

                                try {
                                    entero = Integer.parseInt(tokens[3]);
                                } catch (NumberFormatException e) {
                                    tablaErrores[contErrores][0] = "" + contLineas;
                                    tablaErrores[contErrores][1] = "Tipo de variable entero incompatible";
                                    contErrores++;
                                    banderaError = true;
                                }

                            } else if ("flotante".equalsIgnoreCase(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3]))
                                    && banderaError == false) {
                                try {
                                    flotante = Double.parseDouble(tokens[3]);
                                } catch (NumberFormatException e) {
                                    tablaErrores[contErrores][0] = "" + contLineas;
                                    tablaErrores[contErrores][1] = "Tipo de variable flotante incompatible";
                                    contErrores++;
                                    banderaError = true;
                                }
                            } else if ("cadena".equalsIgnoreCase(tokens[0]) && (tokens[3] != null || !"".equals(tokens[3]))
                                    && banderaError == false) {
                                if (tokens[3].charAt(0) != '"' || tokens[3].charAt(tokens[3].length() - 1) != '"') {
                                    tablaErrores[contErrores][0] = "" + contLineas;
                                    tablaErrores[contErrores][1] = "Tipo de variable cadena incompatible";
                                    contErrores++;
                                    banderaError = true;
                                }
                            }
                        }
                    }

                    if ("para".equalsIgnoreCase(tokens[0])) {
                        banderaFor = true;
                        if (tokens[1].charAt(0) != '(') {
                            tablaErrores[contErrores][0] = "" + contLineas;
                            tablaErrores[contErrores][1] = "No se ha abierto el parentesis para el ciclo para";
                            contErrores++;
                        } else if (!";".equals(tokens[6]) || !";".equals(tokens[10])) {
                            tablaErrores[contErrores][0] = "" + contLineas;
                            tablaErrores[contErrores][1] = "Falta el ';' del separador del ciclo para";
                            contErrores++;
                            banderaError = true;
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
                        } else {
                            banderaFor = false;
                        }
                    }

                    for (int x = 0; (x < tablaSimbolos.length && banderaSimbolo == false); x++) {
                        if (tokens[i].equals(tablaSimbolos[x][2])) {
                            banderaVar = true;
                        }
                    }

                    for (int x = 0; (x < operadores.length && banderaSimbolo == false); x++) {
                        if (tokens[i].equals(operadores[x])) {
                            banderaOp = true;
                        }
                    }

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

                    if (banderaSimbolo == false && !"para".equalsIgnoreCase(tokens[0]) && !"finpara".equalsIgnoreCase(tokens[0])) {
                        if (banderaVar == false && banderaOp == false && banderaConst == false) {
                            tablaErrores[contErrores][0] = "" + contLineas;
                            tablaErrores[contErrores][1] = "Variable " + tokens[i] + " no encontrada";
                            contErrores++;
                            banderaError = true;
                        }
                    }

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

                if (!";".equals(tokens[contTokens]) && !"para".equals(tokens[0]) && !"finpara".equals(tokens[0])) {
                    tablaErrores[contErrores][0] = "" + contLineas;
                    tablaErrores[contErrores][1] = "Hace falta ;";
                    contErrores++;
                }

                banderaSimbolo = false;
                banderaVar = false;
                banderaOp = false;
                banderaConst = false;
                banderaError = false;
                tipo = "";
            }
            if (banderaFor == true) {
                tablaErrores[contErrores][0] = "" + contLineas;
                tablaErrores[contErrores][1] = "El ciclo para no tiene un fin;";
                contErrores++;
            }
            fileInputStream.close();
        } catch (IOException e) {
        }
    }
//TERMINA JORGE
///////////////////////////////////////////////

//EMPIEZA SAUL
    //Interfaz
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelGeneral = new javax.swing.JPanel();
        PanelBoton = new javax.swing.JPanel();
        BtnCompilar = new javax.swing.JButton();
        BtnAbrir = new javax.swing.JButton();
        BtnGuardar = new javax.swing.JButton();
        PanelSimbolos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelCodigo = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        PanelErrores = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Analizador");
        setBackground(new java.awt.Color(18, 150, 200));
        setForeground(new java.awt.Color(18, 150, 200));
        setResizable(false);

        PanelGeneral.setBackground(new java.awt.Color(153, 153, 153));

        PanelBoton.setBackground(new java.awt.Color(153, 153, 153));
        PanelBoton.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Menu", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        BtnCompilar.setBackground(new java.awt.Color(0, 0, 0));
        BtnCompilar.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 14)); // NOI18N
        BtnCompilar.setForeground(new java.awt.Color(240, 240, 240));
        BtnCompilar.setText("Compilar");
        BtnCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCompilarActionPerformed(evt);
            }
        });

        BtnAbrir.setBackground(new java.awt.Color(0, 0, 0));
        BtnAbrir.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 14)); // NOI18N
        BtnAbrir.setForeground(new java.awt.Color(240, 240, 240));
        BtnAbrir.setText("Abrir");
        BtnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAbrirActionPerformed(evt);
            }
        });

        BtnGuardar.setBackground(new java.awt.Color(0, 0, 0));
        BtnGuardar.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 14)); // NOI18N
        BtnGuardar.setForeground(new java.awt.Color(240, 240, 240));
        BtnGuardar.setText("Guardar");
        BtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelBotonLayout = new javax.swing.GroupLayout(PanelBoton);
        PanelBoton.setLayout(PanelBotonLayout);
        PanelBotonLayout.setHorizontalGroup(
            PanelBotonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBotonLayout.createSequentialGroup()
                .addContainerGap(152, Short.MAX_VALUE)
                .addComponent(BtnAbrir)
                .addGap(101, 101, 101)
                .addComponent(BtnCompilar)
                .addGap(271, 271, 271))
            .addGroup(PanelBotonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBotonLayout.createSequentialGroup()
                    .addContainerGap(500, Short.MAX_VALUE)
                    .addComponent(BtnGuardar)
                    .addGap(97, 97, 97)))
        );
        PanelBotonLayout.setVerticalGroup(
            PanelBotonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBotonLayout.createSequentialGroup()
                .addGroup(PanelBotonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBotonLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(BtnAbrir, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                    .addGroup(PanelBotonLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(BtnCompilar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(PanelBotonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PanelBotonLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(BtnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        PanelSimbolos.setBackground(new java.awt.Color(153, 153, 153));
        PanelSimbolos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Simbolos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        jTable1.setBackground(new java.awt.Color(204, 204, 204));
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelCodigo.setBackground(new java.awt.Color(153, 153, 153));
        PanelCodigo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Código", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea1);

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setPreferredSize(new java.awt.Dimension(50, 50));
        jScrollPane4.setViewportView(jTextArea2);

        javax.swing.GroupLayout PanelCodigoLayout = new javax.swing.GroupLayout(PanelCodigo);
        PanelCodigo.setLayout(PanelCodigoLayout);
        PanelCodigoLayout.setHorizontalGroup(
            PanelCodigoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCodigoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelCodigoLayout.setVerticalGroup(
            PanelCodigoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCodigoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCodigoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );

        PanelErrores.setBackground(new java.awt.Color(153, 153, 153));
        PanelErrores.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Errores", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        jTable2.setBackground(new java.awt.Color(204, 204, 204));
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

        javax.swing.GroupLayout PanelGeneralLayout = new javax.swing.GroupLayout(PanelGeneral);
        PanelGeneral.setLayout(PanelGeneralLayout);
        PanelGeneralLayout.setHorizontalGroup(
            PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGeneralLayout.createSequentialGroup()
                .addGroup(PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelGeneralLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(PanelSimbolos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PanelErrores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(PanelBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        PanelGeneralLayout.setVerticalGroup(
            PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGeneralLayout.createSequentialGroup()
                .addGroup(PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelGeneralLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(PanelCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PanelGeneralLayout.createSequentialGroup()
                        .addComponent(PanelBoton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addGroup(PanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PanelSimbolos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelErrores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        PanelBoton.getAccessibleContext().setAccessibleDescription("");

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
                .addContainerGap()
                .addComponent(PanelGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAbrirActionPerformed
        JFileChooser jf = new JFileChooser();
        jf.showOpenDialog(this);
        File archivo = jf.getSelectedFile();
        if (archivo != null) {
            GraphAnalizador.archivo = archivo.getAbsolutePath();
            limpiar();
            Abrir();
        }
    }//GEN-LAST:event_BtnAbrirActionPerformed

    private void BtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGuardarActionPerformed
   //Guardar los archivos e indicador si fue exitoso
        try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
                writer.write(jTextArea1.getText());
                writer.close();
                JOptionPane.showMessageDialog(null, "Archivo guardado exitosamente.");
                Abrir();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + ex.getMessage());
            }
    }//GEN-LAST:event_BtnGuardarActionPerformed

    private void BtnCompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCompilarActionPerformed
       /*Compilador, guarda el archivo y lo abre, por ultimo vacias en las tablas el 
        el contenido de los arreglos de las lineas de codigo
       */
        limpiar();
        try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
                writer.write(jTextArea1.getText());
                writer.close();
            } catch (IOException ex) {
            }
        Abrir();
        int filas = modelo1.getRowCount();
        for (int i = 1; i <= filas; i++) {
            modelo1.removeRow(0);
        }
        for (int i = 0; i < contSimbolos; i++) {
            modelo1.addRow(new Object[]{tablaSimbolos[i][0], tablaSimbolos[i][1], tablaSimbolos[i][2],
                tablaSimbolos[i][3], tablaSimbolos[i][4]});
        }
        int filas2 = modelo2.getRowCount();
        for (int i = 1; i <= filas2; i++) {
            modelo2.removeRow(0);
        }
        for (int i = 0; i < contErrores; i++) {
            modelo2.addRow(new Object[]{tablaErrores[i][0], tablaErrores[i][1]});
        }
    }//GEN-LAST:event_BtnCompilarActionPerformed

    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyPressed
        imprimirLineas();
    }//GEN-LAST:event_jTextArea1KeyPressed

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
    private javax.swing.JButton BtnAbrir;
    private javax.swing.JButton BtnCompilar;
    private javax.swing.JButton BtnGuardar;
    private javax.swing.JPanel PanelBoton;
    private javax.swing.JPanel PanelCodigo;
    private javax.swing.JPanel PanelErrores;
    private javax.swing.JPanel PanelGeneral;
    private javax.swing.JPanel PanelSimbolos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
//TERMINA SAUL
}
