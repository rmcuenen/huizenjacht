# Code Challenge uit Java Magazine #3
## Op huizenjacht
De opdracht is om de eerste _20_ paren _(x, y)_ te vinden waarvan het huisnummer _x_ in een straat met huisnummers _1_ tot en met _y_ zodanig is dat de som van de huisnummers _1_ tot _x_ gelijk is aan de som van de huisnummers vanaf _x_ tot en met _y_.

Mede door de hint uit de tekst lijkt me dit meer op __Math Challenge__ in plaats van een __Code Challenge__.   
Gauss liet rond 1800 zien dat de som van de eerste _n_ gehele getallen gelijk is aan het [rekenkundig gemiddelde](http://nl.wikipedia.org/wiki/Rekenkundig_gemiddelde), oftewel:
```
 __ n        1         
\        k = -n(n + 1).
/__ k = 1    2         
```
Dit is eigenlijk een speciaal geval van de algemenere [rekenkundige rij](http://nl.wikipedia.org/wiki/Rekenkundige_rij), waarvoor geldt:
```
 __ n - 1           n                
\        (a + kd) = -(2a + (n - 1)d),
/__ k = 0           2                
```
waarbij _a_ de eerste term is (voor Gauss is dit _1_), _d_ is het verschil of de afstand tussen de getallen (voor Gauss is dit ook _1_) en _n_ is het aantal getallen waarover gesommeerd wordt.

Voor de uitvoering van de opdracht moeten we dus oplossingen vinden waarbij twee rekenkundige rijen aan elkaar gelijk zijn. Maar wat zijn de parameters van die rijen? Allereerst geldt voor ons ook dat _d = 1_; we hebben immers opeenvolgende huisnummers.   
Voor de eerste som beginnen we bij _1_, dus _a = 1_, en we tellen tot ons huisnummer _x_, dus _n = x - 1_, oftewel:
```
 __ x - 2          1         
\        (1 + k) = -x(x - 1).
/__ k = 0          2         
```
Voor de andere som beginnen we bij het huisnummer van de buren, dus _a = x + 1_, en we tellen tot het eind van de straat, dus _n = y - x_, oftewel:
```
 __ y - x - 1                 1                   
\            (x + 1 + k) =  - -(x - y)(x + y + 1).
/__ k = 0                     2                   
```
Wanneer we deze twee polynomen aan elkaar gelijk stellen kunnen we deze herschrijven tot:
```
                      1               1                  
                      -x(x - 1)  =  - -(x - y)(x + y + 1)
                      2               2                  
                                                         
1           1                                            
-x(x - 1) + -(x - y)(x + y + 1)  = 0                     
2           2                                            
                                                         
                   2x² - y² - y  = 0.                    
```
We hebben nu de opdracht gereduceerd tot het oplossen van bovenstaande tweede-orde bivariate [Diophantische vergelijking](http://nl.wikipedia.org/wiki/Diophantische_vergelijking). Dit is inderdaad wel een beetje lastig om zelf te doen, maar gelukkig hebben we [WolframAlpha](http://www.wolframalpha.com/share/clip?f=d41d8cd98f00b204e9800998ecf8427epoad5fkns7) en vinden we al snel:
```
     1          t            t        
x  = -((3 + 2√2)  - (3 - 2√2) )  / √2,
     4                                
                                      
     1          t            t    1   
y  = -((3 + 2√2)  + (3 - 2√2) ) - -.  
     4                            2   
```
In de tekst wordt al aangegeven dat de triviale oplossing met maar één huis (_t = 1_) buiten beschouwing wordt gelaten. Eigenlijk is een straat zonder huis (_t = 0_) ook een oplossing, maar die laten we ook maar buiten beschouwing. De volgende twee oplossingen _t = 2 ⇒ (6, 8)_ en _t = 3 ⇒ (35, 49)_ zijn ook al gegeven. Voor de volgende _20_ oplossingen hoeven we dus alleen maar _x_ en _y_ uit te rekenen voor _4 ≤ t < 24_.   
Dit vergt natuurlijk niet zo heel veel code (in elke willekeurige taal). Het enige waar we op moeten letten is de naukeurigheid van de gebruikte wiskundige functies en de binaire representatie van de getallen.   
Voor een `long`/`double` implementatie hebben we te maken met een `Long.MAX_VALUE` van _2<sup>63</sup> - 1_ en een double-precisie van ongeveer _16_ getallen. Wanneer we _y ≤ 2<sup>63</sup> - 1_ en _log<sub>10</sub>(y) + 1 ≤ 16_ oplossen vinden we _t ≤ 25_ en _t ≤ 20_ resp. Dit betekent dat we op deze manier niet aan de opdracht kunnen voldoen.   
We moeten dus een implementatie maken met `BigInteger`/`BigDecimal`. Hierbij hebben we het probleem dat er geen `sqrt`-methode is gedefinieerd voor deze objecten. Gelukkig kunnen we de [Methode van Newton-Raphson](http://nl.wikipedia.org/wiki/Methode_van_Newton-Raphson) toepassen om de waarde van _√2_ te berekenen voor, laten we zeggen, _150_ significante getallen.

Aan de formules kunnen we ook zien dat de limiet van _t → ∞_ ook oneindig als resultaat heeft. Als er dus verder geen restricties aan de straat verbonden zijn is er dus __geen__ grootste straat die voldoet.

### De eerste 20 oplossingen
Nr. |     Huisnummer      | Laatste huisnummer
--: | ------------------: | ------------------:
  0 |                   0 |                   0
  1 |                   1 |                   1
  2 |                   6 |                   8
  3 |                  35 |                  49
  4 |                 204 |                 288
  5 |                1189 |                1681
  6 |                6930 |                9800
  7 |               40391 |               57121
  8 |              235416 |              332928
  9 |             1372105 |             1940449
 10 |             7997214 |            11309768
 11 |            46611179 |            65918161
 12 |           271669860 |           384199200
 13 |          1583407981 |          2239277041
 14 |          9228778026 |         13051463048
 15 |         53789260175 |         76069501249
 16 |        313506783024 |        443365544448
 17 |       1827251437969 |       2584123765441
 18 |      10650001844790 |      15061377048200
 19 |      62072759630771 |      87784138523761
 20 |     361786555939836 |     511643454094368
 21 |    2108646576008245 |    2982076586042449
 22 |   12290092900109634 |   17380816062160328
 23 |   71631910824649559 |  101302819786919521
 24 |  417501372047787720 |  590436102659356800
 25 | 2433376321462076761 | 3441313796169221281
