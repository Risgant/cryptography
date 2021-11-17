import lombok.AllArgsConstructor;

import java.math.BigInteger;
import java.util.Objects;

@AllArgsConstructor
public class KeysGenerator {
    private int a;
    private int b;
    private int M;

    public int findDegree(Pair<Integer> point) {
        int degree = 1;
        Pair<Integer> point1 = point;
        while(true) {
            ++degree;
            int lambda;
            if(Objects.equals(point1.getX(), point.getX()) && Objects.equals(point1.getY(), point.getY())) {
                lambda = BigInteger.valueOf(3L * point1.getX() * point1.getX() + a)
                        .mod(BigInteger.valueOf(M))
                        .multiply(BigInteger.valueOf(2L * point1.getY()).modInverse(BigInteger.valueOf(M)))
                        .mod(BigInteger.valueOf(M)).intValue();
            } else {
                lambda = BigInteger.valueOf(point.getY() - point1.getY())
                        .mod(BigInteger.valueOf(M))
                        .multiply(BigInteger.valueOf(point.getX() - point1.getX()).modInverse(BigInteger.valueOf(M)))
                        .mod(BigInteger.valueOf(M)).intValue();
            }
            int x3 = BigInteger.valueOf((long) lambda * lambda - point1.getX() - point.getX())
                    .mod(BigInteger.valueOf(M)).intValue();
            int y3 = BigInteger.valueOf(-point1.getY() + (long) lambda * (point1.getX() - x3))
                    .mod(BigInteger.valueOf(M)).intValue();
            if(x3 == point.getX()) {
                return degree+1;
            } else {
                point1 = new Pair<>(x3, y3);
            }
        }
    }

    public Pair<Integer> generateKey(int privateKey, Pair<Integer> point) {
        Pair<Integer> point1 = point;
        for(int i = 0; i < privateKey; ++i) {
            int lambda;
            if(point1.getX().equals(point.getX()) && point1.getY().equals(point.getY())) {
                lambda = BigInteger.valueOf(3L * point1.getX() * point1.getX() + a)
                        .mod(BigInteger.valueOf(M))
                        .multiply(BigInteger.valueOf(2L * point1.getY()).modInverse(BigInteger.valueOf(M)))
                        .mod(BigInteger.valueOf(M)).intValue();
            } else if(point.getX().equals(point1.getX())){
                i = i+1;
                point1 = point;
                continue;
            } else {
                lambda = BigInteger.valueOf(point.getY() - point1.getY())
                        .mod(BigInteger.valueOf(M))
                        .multiply(BigInteger.valueOf(point.getX() - point1.getX()).modInverse(BigInteger.valueOf(M)))
                        .mod(BigInteger.valueOf(M)).intValue();
            }
            int x3 = BigInteger.valueOf((long) lambda * lambda - point1.getX() - point.getX())
                    .mod(BigInteger.valueOf(M)).intValue();
            int y3 = BigInteger.valueOf(-point1.getY() + (long) lambda * (point1.getX() - x3))
                    .mod(BigInteger.valueOf(M)).intValue();
            point1 = new Pair<>(x3, y3);
        }
        return point1;
    }
}
