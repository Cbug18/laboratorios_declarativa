
sumar(A, B, R) :-
    number(A), number(B),
    R is A + B.

restar(A, B, R) :-
    number(A), number(B),
    R is A - B.
