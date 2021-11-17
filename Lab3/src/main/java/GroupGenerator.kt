import java.util.ArrayList
import kotlin.math.pow

class GroupGenerator(private val a: Int, private val b: Int, private val M: Int) {

    fun findPoints(): List<Pair<Int, Int>> {
        val point = ArrayList<Pair<Int, Int>>()
        for (x in 1 until M) {
            val yy = f(x) % M
            val roots = getRoots(yy)
            for (y in roots) {
                if(y != 0) {
                    point.add(Pair(x, y))
                }
            }
        }
        return point
    }

    private fun getRoots(x: Int): List<Int> {
        val roots = ArrayList<Int>()
        for (i in 0 until M) {
            if (i.toDouble().pow(2.0).toInt() % M == x) {
                roots.add(i)
            }
        }
        return roots
    }

    private fun f(x: Int): Int {
        return x.toDouble().pow(3.0).toInt() + a * x + b
    }
}