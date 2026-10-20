package org.example.screen;

import org.example.Connection;
import org.example.screen.components.Buttons;
import org.example.screen.components.PanelResult;
import org.example.screen.components.TextField;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

public class Window extends JFrame {

    // Pares de ejemplo {A, B}
    private static final String[][] EJEMPLOS = {
            {"5", "3"}, {"12", "20"}, {"2.5", "1.25"}, {"-4", "9"}
    };

    private final TextField txtA = new TextField("Ej: 5");
    private final TextField txtB = new TextField("Ej: 3");
    private final PanelResult panelResultado = new PanelResult();
    private final JLabel lblEstado = new JLabel(" ");

    public Window(Connection motor) {
        super("Calculadora en Prolog");

        // Enter en cualquiera de los campos = sumar
        txtA.addActionListener(e -> ejecutar(motor, "sumar"));
        txtB.addActionListener(e -> ejecutar(motor, "sumar"));

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        agregar(contenido, crearCampos(), 8);
        agregar(contenido, crearEjemplos(), 18);
        agregar(contenido, crearBotones(motor), 22);
        agregar(contenido, panelResultado, 10);
        agregar(contenido, crearPie(), 0);

        JPanel raiz = new JPanel(new BorderLayout(0, 20));
        raiz.setBackground(Theme.BG);
        raiz.setBorder(new EmptyBorder(24, 28, 24, 28));
        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        raiz.add(contenido, BorderLayout.CENTER);

        setContentPane(raiz);
        setMinimumSize(new Dimension(660, 500));
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------------
    // Construcción de secciones
    // ------------------------------------------------------------------
    private JPanel crearEncabezado() {
        JLabel titulo = new JLabel("Calculadora");
        titulo.setFont(Theme.sans(Font.BOLD, 24));
        titulo.setForeground(Theme.TEXT);

        JLabel subtitulo = new JLabel("Sumas y restas resueltas con reglas en Prolog");
        subtitulo.setFont(Theme.sans(Font.PLAIN, 13));
        subtitulo.setForeground(Theme.MUTED);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.add(titulo);
        p.add(Box.createVerticalStrut(4));
        p.add(subtitulo);
        return p;
    }

    // Dos columnas: "Número A" y "Número B"
    private JPanel crearCampos() {
        JPanel p = new JPanel(new GridLayout(1, 2, 14, 0));
        p.setOpaque(false);
        p.add(columna("Número A", txtA));
        p.add(columna("Número B", txtB));
        return p;
    }

    private JPanel columna(String texto, TextField campo) {
        JPanel col = new JPanel(new BorderLayout(0, 6));
        col.setOpaque(false);
        col.add(etiqueta(texto), BorderLayout.NORTH);
        col.add(campo, BorderLayout.CENTER);
        return col;
    }

    private JPanel crearEjemplos() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setOpaque(false);

        JLabel lbl = new JLabel("Ejemplos:");
        lbl.setForeground(Theme.MUTED);
        lbl.setFont(Theme.sans(Font.PLAIN, 12));
        p.add(lbl);

        for (String[] ej : EJEMPLOS) {
            Buttons chip = new Buttons(ej[0] + " , " + ej[1], Theme.CARD, Theme.TEXT, 12, 6, 11);
            chip.addActionListener(e -> {
                txtA.setText(ej[0]);
                txtB.setText(ej[1]);
                txtA.requestFocusInWindow();
            });
            p.add(chip);
        }
        return p;
    }

    private JPanel crearBotones(Connection motor) {
        Buttons btnSumar = new Buttons("Sumar", Theme.GREEN, Theme.BG, 26, 11, 14);
        Buttons btnRestar = new Buttons("Restar", Theme.RED, Theme.BG, 26, 11, 14);
        btnSumar.addActionListener(e -> ejecutar(motor, "sumar"));
        btnRestar.addActionListener(e -> ejecutar(motor, "restar"));

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        p.add(btnSumar);
        p.add(btnRestar);
        return p;
    }

    private JPanel crearPie() {
        lblEstado.setFont(Theme.sans(Font.PLAIN, 12));
        lblEstado.setForeground(Theme.MUTED);

        Buttons btnCopiar = new Buttons("Copiar", Theme.CARD, Theme.TEXT, 16, 7, 12);
        btnCopiar.addActionListener(e -> copiarResultado());

        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(lblEstado, BorderLayout.WEST);
        p.add(btnCopiar, BorderLayout.EAST);
        // Que el pie no se estire a lo alto
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, p.getPreferredSize().height));
        return p;
    }

    // ------------------------------------------------------------------
    // Lógica
    // ------------------------------------------------------------------
    private void ejecutar(Connection motor, String pred) {
        String a = txtA.getText().trim();
        String b = txtB.getText().trim();
        boolean esSuma = pred.equals("sumar");
        panelResultado.setTitulo(esSuma ? "Suma de A + B" : "Resta de A - B");

        if (a.isEmpty() || b.isEmpty()) {
            mostrar("", "Escribí los dos números primero.", Theme.AMBER);
            return;
        }
        try {
            String resultado = motor.consultar(pred, a, b);
            if (resultado.startsWith("Sin solución")) {
                mostrar("", resultado, Theme.AMBER);
            } else {
                String signo = esSuma ? " + " : " - ";
                mostrar(a + signo + b + " = " + resultado, "Listo", Theme.GREEN);
            }
        } catch (Exception ex) {
            mostrar("", "Entrada inválida: " + ex.getMessage(), Theme.RED);
        }
    }

    private void mostrar(String resultado, String estado, Color colorEstado) {
        panelResultado.setTexto(resultado);
        lblEstado.setText(estado);
        lblEstado.setForeground(colorEstado);
    }

    private void copiarResultado() {
        String t = panelResultado.getTexto();
        if (t.isEmpty()) {
            return;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(t), null);
        lblEstado.setText("Copiado al portapapeles");
        lblEstado.setForeground(Theme.BLUE);
    }

    private static void agregar(JPanel panel, JComponent c, int espacioDespues) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(c);
        if (espacioDespues > 0) {
            panel.add(Box.createVerticalStrut(espacioDespues));
        }
    }

    private static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Theme.sans(Font.BOLD, 12));
        l.setForeground(Theme.MUTED);
        return l;
    }
}
