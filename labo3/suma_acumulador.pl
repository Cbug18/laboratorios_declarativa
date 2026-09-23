suma(N, S) :- suma(N, 0, S).
suma(0, Acc, Acc).
suma(N, Acc, S) :-
    N > 0,
    Acc1 is Acc + N,
    N1 is N - 1,
    suma(N1, Acc1, S).
