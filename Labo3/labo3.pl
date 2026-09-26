% 2. dado N, sumar N con todos los anteriores hasta 1.
% Y = N + (N - 1) + ... + 1

suma(1, 1).
suma(X, Y) :-
    X > 1,
    N is X - 1,
    suma(N, R),
    Y is X + R.
