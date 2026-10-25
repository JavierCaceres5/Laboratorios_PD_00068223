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

    // Consulta la regla de la operacion con los dos numeros
    public String operar(String predicado, String a, String b) throws Exception {
        SolveInfo info = engine.solve(predicado + "(" + a + ", " + b + ", R).");
        return info.isSuccess() ? engine.toString(info.getTerm("R"))
                : "Sin solución para esa operación";
    }
}
