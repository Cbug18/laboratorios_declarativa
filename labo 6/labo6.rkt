#lang racket

(define (tasa-interes deposito)
  (cond
    [(<= deposito 40000) 0.035]
    [(<= deposito 260000) 0.04]
    [else 0.045]))

(define (interes deposito)
  (* deposito (tasa-interes deposito)))

(displayln (interes 1000))
(displayln (interes 40000))
(displayln (interes 100000))
(displayln (interes 260000))
(displayln (interes 300000))
