% 3. Dado un valor entero, almacenar cada uno de los digitos que lo
% componen en una lista, un digito por casilla.
% ?- almacenar(82671, L).
% L = [1, 7, 6, 2, 8]

% Caso base
almacenar(N, [N]) :-
    N < 10.

% Se extrae el ultimo digito con mod 10 y se quita del numero con // 10.
almacenar(N, [D|T]) :-
    N >= 10,
    D is N mod 10,
    R is N // 10,
    almacenar(R, T).
