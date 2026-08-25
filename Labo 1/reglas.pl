:- consult('hechos.pl').

personaje_veterano(Personaje) :-
	edad(Personaje, Edad), 
	Edad > 25.

zona_peligrosa(Personaje) :- 
	spawn(Personaje, Lugar), 
	(dificultad(Lugar, alta) ; dificultad(Lugar, muy_alta)).

mismo_spawn(P1, P2, Lugar) :- 
	spawn(P1, Lugar), 
	spawn(P2, Lugar), 
	P1 \== P2.
	