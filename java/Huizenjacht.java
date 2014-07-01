
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Bereken de eerste 20 oplossingen van de Code Challenge uit Java Magazine #3.
 *
 * @author Raymond Cuenen
 */
public class Huizenjacht {

    /** Constante voor het getal &frac12;. */
    private static final BigDecimal HALF = BigDecimal.valueOf(0.5);
    /** Constante voor het getal 2. */
    private static final BigDecimal TWO = BigDecimal.valueOf(2L);
    /** Constante voor het getal 3. */
    private static final BigDecimal THREE = BigDecimal.valueOf(3L);
    /** Constante voor het getal 4. */
    private static final BigDecimal FOUR = BigDecimal.valueOf(4L);

    /**
     * Bereken &radic;2 met de Newton-Raphson methode tot een gegeven precisie.
     *
     * @param xn de benadering voor &radic;2
     * @param sig de gewenste significantie factor
     * @return een betere benadering voor &radic;2
     */
    private static BigDecimal sqrtTwo(BigDecimal xn, int sig) {
        BigDecimal currentSquare = xn.pow(2);
        BigDecimal currentPrecision = currentSquare.subtract(TWO).abs();
        if (currentPrecision.compareTo(BigDecimal.ONE.divide(BigDecimal.TEN.pow(sig))) < 0) {
            return xn;
        } else {
            BigDecimal fx = currentSquare.subtract(TWO);
            BigDecimal fpx = xn.multiply(TWO);
            BigDecimal xn1 = xn.subtract(fx.divide(fpx, 2 * sig, RoundingMode.HALF_DOWN));
            return sqrtTwo(xn1, sig);
        }
    }

    /**
     * Bereken de som van de getallen {@code from} tot en met {@code to}, met
     * een stapgrootte van {@code step}.
     *
     * @param from Het begingetal
     * @param to Het eindgetal
     * @param step De stapgrootte
     * @return De som van de rekenkundige rij
     */
    private static BigInteger sum(BigInteger from, BigInteger to, BigInteger step) {
        BigDecimal a = new BigDecimal(from);
        BigDecimal d = new BigDecimal(step);
        BigDecimal n = new BigDecimal(to).add(BigDecimal.ONE).subtract(a).divide(d);
        return TWO.multiply(a).add(n.subtract(BigDecimal.ONE).multiply(d))
                .multiply(n).divide(TWO).toBigIntegerExact();
    }

    /**
     * Controleer of de gegeven oplossing juist is.
     *
     * @param number Het huisnummer
     * @param end Het laatste huisnummer van de straat
     * @return {@code true} als de oplossing juist is
     */
    private static boolean validate(BigInteger number, BigInteger end) {
        BigInteger sum1 = sum(BigInteger.ONE, number.subtract(BigInteger.ONE), BigInteger.ONE);
        BigInteger sum2 = sum(number.add(BigInteger.ONE), end, BigInteger.ONE);
        return sum1.compareTo(sum2) == 0;
    }

    /**
     * Bereken de waarde van &frac14;(3+2&radic;2)<sup>t</sup>.
     *
     * @param t het nummer van de oplossing
     * @param wortel de waarde van &radic;2
     * @return de berekende waarde
     */
    private static BigDecimal plusTerm(int t, BigDecimal wortel) {
        return THREE.add(TWO.multiply(wortel)).pow(t).divide(FOUR);
    }

    /**
     * Bereken de waarde van &frac14;(3-2&radic;2)<sup>t</sup>.
     *
     * @param t het nummer van de oplossing
     * @param wortel de waarde van &radic;2
     * @return de berekende waarde
     */
    private static BigDecimal minTerm(int t, BigDecimal wortel) {
        return THREE.subtract(TWO.multiply(wortel)).pow(t).divide(FOUR);
    }

    public static void main(String[] args) {
        int start = 0; // Begin bij 0.
        int end = 25; // Eindig bij 25 (de 20 oplossingen plus een beetje).

        if (args.length > 0) {
            try {
                end = Integer.parseInt(args[0]); // Neem eerste argument over als einde.
            } catch (NumberFormatException ex) {
            }
        }

        if (args.length > 1) {
            start = end; // Neem eerste argument over als begin.
            try {
                end = Integer.parseInt(args[1]); // Neem tweede argument over als einde.
            } catch (NumberFormatException ex) {
            }
        }

        int sig = 20;
        BigDecimal sqrtTwo = sqrtTwo(BigDecimal.ONE, sig); // Waarde voor wortel 2.
        for (int t = start; t <= end; t++) {
            BigDecimal plusTerm = plusTerm(t, sqrtTwo);
            BigDecimal minTerm = minTerm(t, sqrtTwo);
            BigInteger huisnummer = plusTerm.subtract(minTerm).divide(sqrtTwo).toBigInteger();
            BigInteger laatsteHuisnummer = plusTerm.add(minTerm).subtract(HALF).toBigInteger();
            if (validate(huisnummer, laatsteHuisnummer)) { // Controleer de oplossing
                System.out.printf("%2s %32s %32s\n", t, huisnummer, laatsteHuisnummer);
            } else {
                // Bereken een betere waarde voor wortel 2 en probeer opnieuw.
                sig += 20;
                sqrtTwo = sqrtTwo(sqrtTwo, sig);
                t--;
            }
        }
    }
}
