import java.math.BigInteger

class KeysGenerator(private val a: Int, private val b: Int, private val M: Int) {

    fun findDegree(point: Pair<Int, Int>): Int {
        var degree = 1
        var point1 = point
        while (true) {
            ++degree
            var lambda: Int
            lambda = if (point1.first == point.first && point1.second == point.second) {
                BigInteger.valueOf(3L * point1.first * point1.first + a)
                        .mod(BigInteger.valueOf(M.toLong()))
                        .multiply(BigInteger.valueOf(2L * point1.second).modInverse(BigInteger.valueOf(M.toLong())))
                        .mod(BigInteger.valueOf(M.toLong())).toInt()
            } else {
                BigInteger.valueOf((point.second - point1.second).toLong())
                        .mod(BigInteger.valueOf(M.toLong()))
                        .multiply(BigInteger.valueOf((point.first - point1.first).toLong()).modInverse(BigInteger.valueOf(M.toLong())))
                        .mod(BigInteger.valueOf(M.toLong())).toInt()
            }
            val x3 = BigInteger.valueOf(lambda.toLong() * lambda - point1.first - point.first)
                    .mod(BigInteger.valueOf(M.toLong())).toInt()
            val y3 = BigInteger.valueOf(-point1.second + lambda.toLong() * (point1.first - x3))
                    .mod(BigInteger.valueOf(M.toLong())).toInt()
            if (x3 == point.first) {
                return degree + 1
            } else {
                point1 = Pair(x3, y3)
            }
        }
    }

    fun generateKey(privateKey: Int, point: Pair<Int, Int>): Pair<Int, Int> {
        if(point.first == 0 && point.second == 0) {
            return Pair(0, 0)
        }

        var point1 = point
        var i = 1
        while (i < privateKey) {
            var lambda: Int
            if (point1.first == point.first && point1.second == point.second) {
                lambda = BigInteger.valueOf(3L * point1.first * point1.first + a)
                        .mod(BigInteger.valueOf(M.toLong()))
                        .multiply(BigInteger.valueOf(2L * point1.second).modInverse(BigInteger.valueOf(M.toLong())))
                        .mod(BigInteger.valueOf(M.toLong())).toInt()
            } else if (point.first == point1.first && i == privateKey - 1) {
                return Pair(0, 0)
            } else if (point.first == point1.first) {
                point1 = point
                i += 2
                continue
            } else {
                lambda = BigInteger.valueOf((point.second - point1.second).toLong())
                        .mod(BigInteger.valueOf(M.toLong()))
                        .multiply(BigInteger.valueOf((point.first - point1.first).toLong()).modInverse(BigInteger.valueOf(M.toLong())))
                        .mod(BigInteger.valueOf(M.toLong())).toInt()
            }
            val x3 = BigInteger.valueOf(lambda.toLong() * lambda - point1.first - point.first)
                    .mod(BigInteger.valueOf(M.toLong())).toInt()
            val y3 = BigInteger.valueOf(-point1.second + lambda.toLong() * (point1.first - x3))
                    .mod(BigInteger.valueOf(M.toLong())).toInt()
            point1 = Pair(x3, y3)
            ++i
        }
        return point1
    }
}