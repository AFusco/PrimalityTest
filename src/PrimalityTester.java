import java.math.BigInteger;
import java.util.Random;

/**
 * @author afusco
 * @date 18/04/17
 */
public class PrimalityTester {

    private static Random r = new Random(System.currentTimeMillis());

    public static boolean isPrime(BigInteger prime, double probability) {
        if (probability <= 0.5) {
            return isPrime(prime, 1);
        }
        return isPrime(prime, (int) Math.floor(Math.log(probability) / Math.log(2)));
    }

    public static boolean isPrime(BigInteger prime, int m) {

        if (prime.equals(BigInteger.valueOf(2))) {
            return true;
        }

        if (prime.compareTo(BigInteger.ONE) <= 0
                || prime.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            return false;
        }

        // Do not calculate every time
        BigInteger nm1 = prime.subtract(BigInteger.ONE);

        BigInteger[] b = new BigInteger[m];
        //TODO improve random generation to make unique
        for (int i = 0; i < m; i++) {
            b[i] = randomBigInt(BigInteger.valueOf(2), prime);
        }


        for (int i = 0; i < m; i++) {

            // Fermat test
            if (b[i].modPow(prime.subtract(BigInteger.ONE), prime).compareTo(BigInteger.ONE) != 0) {
                return false;
            }


            BigInteger twoToJ = BigInteger.valueOf(2);

            // Start from biggest j so k is minimum
            // No logarithm operation is available with BigIntegers, so this will take log2(n) iterations to find j
            //while (twoToJ.compareTo(nm1) <= 0) { twoToJ = twoToJ.multiply(BigInteger.valueOf(2)); }

            //BitLength of a BigInteger is ceil(log_2(x))
            twoToJ = BigInteger.valueOf(2).pow(nm1.bitLength());

            while (twoToJ.compareTo(BigInteger.ONE) > 0) {

                // k[0] = n-1 / (2^j)
                // k[1] = n-1 % (2^j)
                BigInteger[] k = nm1.divideAndRemainder(twoToJ);

                if (k[1].compareTo(BigInteger.ZERO) == 0) {

                    // modPow can be used because gcd(a,b) = gcd(b,a%b) (euclid's algorithm)
                    // also, the subtraction can be performed later, because
                    // (b^k-1) % nint = ((b^k) % nint) -1
                    BigInteger gcd = b[i].modPow(k[0], prime).subtract(BigInteger.ONE).mod(prime).gcd(prime);

                    if (gcd.compareTo(prime) < 0 && gcd.compareTo(BigInteger.ONE) > 0) {
                        return false;
                    }
                }

                twoToJ = twoToJ.divide(BigInteger.valueOf(2));
            }

            //System.out.println("\n\n");
        }

        return true;
    }


    private static BigInteger randomBigInt(BigInteger max) {
        BigInteger i;
        do {
            i = new BigInteger(max.bitLength(), r);
        } while (i.compareTo(max) >= 0);
        return i;
    }

    private static BigInteger randomBigInt(BigInteger min, BigInteger max) {
        return randomBigInt(max.subtract(min)).add(min);
    }

    public static BigInteger gcd(BigInteger p, BigInteger q) {
        while (q.compareTo(BigInteger.ZERO) > 0) {
            BigInteger temp = q;
            q = p.mod(q);
            p = temp;
        }
        return p;
    }

}
