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

    // Arma la consulta: sumar(A, B, R).  o  restar(A, B, R).
    public String consultar(String predicado, String a, String b) throws Exception {
        SolveInfo info = engine.solve(predicado + "(" + a + ", " + b + ", R).");
        return info.isSuccess() ? info.getTerm("R").toString()
                : "Sin solución: los dos valores deben ser números";
    }
}
