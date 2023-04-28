# ForzaForza performace 

## 2023-04-28

### engine = new MoveEngine(6, 7, 4, 10) **all**

    depth: 1, time: 1
    depth: 2, time: 13
    depth: 3, time: 15
    depth: 4, time: 18
    depth: 5, time: 23
    depth: 6, time: 45
    depth: 7, time: 90
    depth: 8, time: 197
    depth: 9, time: 649
    depth: 10, time: 1670
    depth: 11, time: 8735
    tempo finito
    Move col: 3; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 1688958 cut: 403477 rfator: 0.4655379562947722 }
    Move col: 2; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 645313 cut: 170787 rfator: 0.17787161977411414 }
    Move col: 4; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 1022471 cut: 236401 rfator: 0.28183001573199096 }
    Move col: 5; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 907453 cut: 218396 rfator: 0.2501268918786375 }
    Move col: 1; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 350249 cut: 93616 rfator: 0.09654130159203939 }
    Move col: 6; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 570388 cut: 150575 rfator: 0.15721957787882382 }
    Move col: 0; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 233020 cut: 63919 rfator: 0.06422874611198609 

----------

### engine = new MoveEngine(6, 7, 4, 10) **no ordering**

    depth: 1, time: 1
    depth: 2, time: 13
    depth: 3, time: 15
    depth: 4, time: 18
    depth: 5, time: 23
    depth: 6, time: 40
    depth: 7, time: 88
    depth: 8, time: 229
    depth: 9, time: 788
    depth: 10, time: 2465
    tempo finito
    Move col: 0; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 1127192 cut: 355096 rfator: 0.31069491368750246% }
    Move col: 1; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 350249 cut: 93616 rfator: 0.09654130159203939% }
    Move col: 2; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 645313 cut: 170787 rfator: 0.17787161977411414% }
    Move col: 3; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 1517470 cut: 344629 rfator: 0.41826965652113784% }
    Move col: 4; player: 0; score: { score: 2; state: OPEN; };	perf: { d: 11 n: 1022471 cut: 236401 rfator: 0.28183001573199096% }
    Move col: 5; player: 0; score: { score: 0; state: OPEN; };	perf: { d: 10 n: 113966 cut: 37304 rfator: 0.18847892745855138% }
    Move col: 6; player: 0; score: { score: 0; state: OPEN; };	perf: { d: 10 n: 38506 cut: 12125 rfator: 0.06368188390150553% }
----------

### engine = new MoveEngine(6, 7, 4, 10) **no alphabeta**

    depth: 1, time: 1
    depth: 2, time: 15
    depth: 3, time: 19
    depth: 4, time: 28
    depth: 5, time: 52
    depth: 6, time: 203
    depth: 7, time: 1202
    depth: 8, time: 8069
    tempo finito
    Move col: 3; player: 0; score: { score: -1; state: OPEN; };	perf: { d: 8 n: 804918 cut: 0 rfator: 47.92273948331047% }
    Move col: 2; player: 0; score: { score: -1; state: OPEN; };	perf: { d: 8 n: 809238 cut: 0 rfator: 48.179941129401% }
    Move col: 4; player: 0; score: { score: -1; state: OPEN; };	perf: { d: 8 n: 809238 cut: 0 rfator: 48.179941129401% }
    Move col: 5; player: 0; score: { score: -1; state: OPEN; };	perf: { d: 8 n: 813558 cut: 0 rfator: 48.437142775491544% }
    Move col: 1; player: 0; score: { score: -1; state: OPEN; };	perf: { d: 8 n: 813558 cut: 0 rfator: 48.437142775491544% }
    Move col: 6; player: 0; score: { score: -1; state: OPEN; };	perf: { d: 8 n: 817878 cut: 0 rfator: 48.69434442158207% }
    Move col: 0; player: 0; score: { score: -1; state: OPEN; };	perf: { d: 8 n: 817878 cut: 0 rfator: 48.69434442158207% }

----------


