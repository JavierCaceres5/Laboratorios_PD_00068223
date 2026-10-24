package org.example;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;

import java.io.InputStream;

public class Connection {
    private final Prolog engine = new Prolog();

    public Connection() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/calculo.pl")) {
            if (in == null) throw new IllegalStateException("No se encontró calculo.pl");
            engine.setTheory(new Theory(in));
        }
    }

    public String consultar(String predicado, String expresion) throws Exception {
        SolveInfo info = engine.solve(predicado + "(x, " + expresion + ", R).");
        return info.isSuccess() ? engine.toString(info.getTerm("R"))
                : "Sin solución para esa expresión";
    }

    // Evalua una expresion simbolica (en funcion de x) en un valor numerico
    public double evaluarEnPunto(String expresion, double valor) throws Exception {
        String sustituida = expresion.replaceAll("\\bx\\b", "(" + valor + ")").replace("^", "**");
        SolveInfo info = engine.solve("R is " + sustituida + ".");
        if (!info.isSuccess()) throw new IllegalStateException("No se pudo evaluar en x = " + valor);
        return Double.parseDouble(info.getTerm("R").toString());
    }

    // Area bajo la curva entre a y b usando la antiderivada F(x) = F(b) - F(a)
    public double calcularArea(String funcion, double a, double b) throws Exception {
        String antiderivada = consultar("s", funcion);
        if (antiderivada.startsWith("Sin solución")) {
            throw new IllegalStateException("No se pudo integrar la función");
        }
        double fb = evaluarEnPunto(antiderivada, b);
        double fa = evaluarEnPunto(antiderivada, a);
        return fb - fa;
    }
}
