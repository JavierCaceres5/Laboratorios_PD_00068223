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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

public class Window extends JFrame {

    private static final String[] EJEMPLOS = {
            "x", "log(x)", "3*x^2", "2*x^3"
    };

    private final TextField txtEntrada = new TextField("Ej: 5*x^2");
    private final TextField txtPunto = new TextField("Ej: 2", 4);
    private final TextField txtLimA = new TextField("0", 4);
    private final TextField txtLimB = new TextField("1", 4);
    private final PanelResult panelResultado = new PanelResult();
    private final JLabel lblEstado = new JLabel(" ");

    public Window(Connection motor) {
        super("Cálculo simbólico en Prolog");

        txtEntrada.addActionListener(e -> ejecutar(motor, "d"));

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        agregar(contenido, etiqueta("Función f(x)"), 6);
        agregar(contenido, txtEntrada, 8);
        agregar(contenido, crearEjemplos(), 18);
        agregar(contenido, crearBotones(motor), 22);
        agregar(contenido, panelResultado, 16);
        agregar(contenido, crearEvaluacionNumerica(motor), 10);
        agregar(contenido, crearPie(), 0);

        JPanel raiz = new JPanel(new BorderLayout(0, 20));
        raiz.setBackground(Theme.BG);
        raiz.setBorder(new EmptyBorder(24, 28, 24, 28));
        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        raiz.add(contenido, BorderLayout.CENTER);

        setContentPane(raiz);
        setMinimumSize(new Dimension(660, 560));
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------
    // Construcción de secciones
    // ------------------------------------------------------------

    private JPanel crearEncabezado() {
        JLabel titulo = new JLabel("Cálculo simbólico");
        titulo.setFont(Theme.sans(Font.BOLD, 24));
        titulo.setForeground(Theme.TEXT);

        JLabel subtitulo = new JLabel("Derivadas e integrales resueltas con reglas en Prolog");
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

    private JPanel crearEjemplos() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setOpaque(false);

        JLabel lbl = new JLabel("Ejemplos:");
        lbl.setForeground(Theme.MUTED);
        lbl.setFont(Theme.sans(Font.PLAIN, 12));
        p.add(lbl);

        for (String ej : EJEMPLOS) {
            Buttons chip = new Buttons(ej, Theme.CARD, Theme.TEXT, 12, 6, 11);
            chip.addActionListener(e -> {
                txtEntrada.setText(ej);
                txtEntrada.requestFocusInWindow();
            });
            p.add(chip);
        }
        return p;
    }

    private JPanel crearBotones(Connection motor) {
        Buttons btnDerivar = new Buttons("Derivar", Theme.BLUE, Theme.BG, 26, 11, 14);
        Buttons btnIntegrar = new Buttons("Integrar", Theme.GREEN, Theme.BG, 26, 11, 14);
        btnDerivar.addActionListener(e -> ejecutar(motor, "d"));
        btnIntegrar.addActionListener(e -> ejecutar(motor, "s"));

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        p.add(btnDerivar);
        p.add(btnIntegrar);
        return p;
    }

    private JPanel crearEvaluacionNumerica(Connection motor) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = new JLabel("Evaluación numérica");
        lbl.setFont(Theme.sans(Font.BOLD, 12));
        lbl.setForeground(Theme.MUTED);
        agregar(p, lbl, 6);

        JPanel filaPunto = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filaPunto.setOpaque(false);
        filaPunto.add(new JLabel("x ="){{ setForeground(Theme.MUTED); }});
        filaPunto.add(txtPunto);
        Buttons btnPunto = new Buttons("Derivada en punto", Theme.CARD, Theme.TEXT, 14, 8, 12);
        btnPunto.addActionListener(e -> evaluarDerivadaEnPunto(motor));
        filaPunto.add(btnPunto);
        agregar(p, filaPunto, 6);

        JPanel filaArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filaArea.setOpaque(false);
        filaArea.add(new JLabel("a ="){{ setForeground(Theme.MUTED); }});
        filaArea.add(txtLimA);
        filaArea.add(new JLabel("b ="){{ setForeground(Theme.MUTED); }});
        filaArea.add(txtLimB);
        Buttons btnArea = new Buttons("Área [a, b]", Theme.CARD, Theme.TEXT, 14, 8, 12);
        btnArea.addActionListener(e -> calcularAreaEntreLimites(motor));
        filaArea.add(btnArea);
        agregar(p, filaArea, 0);

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
        return p;
    }

    // ------------------------------------------------------------
    // Lógica
    // ------------------------------------------------------------

    private void ejecutar(Connection motor, String pred) {
        String entrada = txtEntrada.getText().trim();
        String nombre = pred.equals("d") ? "Derivada" : "Integral";
        panelResultado.setTitulo(nombre + " de f(x)");

        if (entrada.isEmpty()) {
            mostrar("", "Escribí una función primero.", Theme.AMBER);
            return;
        }
        try {
            String resultado = motor.consultar(pred, entrada);
            if (resultado.startsWith("Sin solución")) {
                mostrar("", resultado, Theme.AMBER);
            } else {
                mostrar(resultado, "Listo", Theme.GREEN);
            }
        } catch (Exception ex) {
            mostrar("", "Expresión inválida: " + ex.getMessage(), Theme.RED);
        }
    }

    private void evaluarDerivadaEnPunto(Connection motor) {
        String entrada = txtEntrada.getText().trim();
        panelResultado.setTitulo("Derivada evaluada en un punto");
        try {
            double valor = Double.parseDouble(txtPunto.getText().trim());
            String derivada = motor.consultar("d", entrada);
            if (derivada.startsWith("Sin solución")) {
                mostrar("", derivada, Theme.AMBER);
                return;
            }
            double resultado = motor.evaluarEnPunto(derivada, valor);
            mostrar("f'(" + valor + ") = " + resultado, "Listo", Theme.GREEN);
        } catch (NumberFormatException ex) {
            mostrar("", "Ingresá un valor numérico para x.", Theme.AMBER);
        } catch (Exception ex) {
            mostrar("", "No se pudo evaluar: " + ex.getMessage(), Theme.RED);
        }
    }

    private void calcularAreaEntreLimites(Connection motor) {
        String entrada = txtEntrada.getText().trim();
        panelResultado.setTitulo("Área bajo la curva");
        try {
            double a = Double.parseDouble(txtLimA.getText().trim());
            double b = Double.parseDouble(txtLimB.getText().trim());
            double area = motor.calcularArea(entrada, a, b);
            mostrar("Área entre " + a + " y " + b + " = " + area, "Listo", Theme.GREEN);
        } catch (NumberFormatException ex) {
            mostrar("", "Ingresá valores numéricos para a y b.", Theme.AMBER);
        } catch (Exception ex) {
            mostrar("", "No se pudo calcular: " + ex.getMessage(), Theme.RED);
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
