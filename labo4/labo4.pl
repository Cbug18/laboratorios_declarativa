
almacenar(N, [N]) :-
    N >= 0,
    N < 10.

almacenar(N, [D|T]) :-
    N >= 10,
    D is N mod 10,
    R is N // 10,
    almacenar(R, T).

almacenar(N, L) :-
    N < 0,
    P is abs(N),
    almacenar(P, L).
