import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Main {

    static int trials = 1;

    public static void main(String[] args) {

        Object[] primes = new Object[]{
                "899809343",
                "20988936657440586486151264256610222593863921",
                mersenne(19),
                mersenne(31),
                mersenne(61),
                mersenne(89),
                mersenne(107),
                mersenne(127),
                mersenne(521),
                mersenne(607)
        };

        for (int m = 1; m < 5; m++) {
            testAll(primes, m);
            testFile("primes.txt", m);
        }

        for (int m = 1; m < 5; m++) {
            for (Object p : primes) {
                tryManyTimes(p, true, m,5000);
                tryManyTimes(getNum(p).add(BigInteger.valueOf(6)), false, m,1000);
            }
        }



    }

    public static BigInteger getNum(Object num) {
        BigInteger n;
        if (num instanceof String)
            n = new BigInteger((String) num);
        else if (num instanceof BigInteger)
            n = (BigInteger) num;
        else if (num instanceof BigDecimal)
            n = ((BigDecimal) num).toBigInteger();
        else if (num instanceof Number)
            n = BigInteger.valueOf(((Number) num).longValue());
        else
            throw new NumberFormatException();

        return n;
    }

    public static void testAll(Object[] nums, int m) {
        BigInteger n;
        int wrong = 0;

        for (int i = 0; i < nums.length; i++) {
            n = getNum(nums[i]);
            if (!PrimalityTester.isPrime(n, m)) {
                System.err.println(i+ ") Error: " + n + " is found to be not prime");
                wrong++;
            }
            wrong = PrimalityTester.isPrime(n, m) ? 0 : 1;
        }

        printStats(nums.length, m, wrong);
    }

    public static void testFile(String fileName, int m) {

        int wrong = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            BigInteger i = BigInteger.ZERO;

            while ((line = br.readLine()) != null) {

                BigInteger nextPrime = new BigInteger(line);


                for(i = nextPrime.add(BigInteger.ONE); i.compareTo(nextPrime) < 0; i = i.add(BigInteger.ONE)) {
                    if (PrimalityTester.isPrime(i, m)) {
                        System.out.println(i + " is found to be prime when it isn't.");
                        wrong++;
                    }
                }

                if (!PrimalityTester.isPrime(nextPrime, m)) {
                    System.out.println(i + " is found to be not prime when it is.");
                    wrong++;
                }

            }

            printStats(i.intValue(), m, wrong);

        } catch (Exception e) {
            System.out.println("Cannot open file");
        }
    }

    public static void printStats(int total, int m, int wrong) {
        if (total == 0) return;

        if (wrong != 0)
            System.out.println("<<<<<<----------------------------------------");

        System.out.println("Run " + trials++);
        System.out.println("Errors: " + wrong + "/" + total);
        System.out.printf("Certainty (m): " + m + "\nExpected accuracy: %.2f%%\n", 100*(1 - Math.pow(2, -m)));
        System.out.printf("Accuracy: %.2f%%\n", (total - wrong)*100.0/total);

        System.out.println();
    }

    public static void tryManyTimes(Object num, boolean shouldBePrime, int m, int times) {
        int wrong = 0;
        for (int i = 0; i < times; i++) {
            wrong += PrimalityTester.isPrime(getNum(num), m) == shouldBePrime ? 0 : 1;
        }
        printStats(times, m, wrong);
    }


    public static BigInteger mersenne(int m) {
        return BigInteger.valueOf(2).pow(m).subtract(BigInteger.ONE);
    }

    public static void prova(BigInteger x) {

        System.out.println("Numero: " + x);
        System.out.println(PrimalityTester.isPrime(x, 10));
    }
}
