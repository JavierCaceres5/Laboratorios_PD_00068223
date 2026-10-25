% Suma
suma(A, B, R) :-
    number(A), number(B), is(R, A + B).

% Resta
resta(A, B, R) :-
    number(A), number(B), is(R, A - B).

% Multiplicacion
multiplicacion(A, B, R) :-
    number(A), number(B), is(R, A * B).

% Division
division(A, B, R) :-
    number(A), number(B), B =\= 0, is(R, (A * 1.0) / B).
