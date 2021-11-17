import java.util.ArrayList
import kotlin.math.pow

class ParamsGenerator(private val M: Int) {
    fun findParams(): List<Pair<Int, Int>> {
        val availableParams = ArrayList<Pair<Int, Int>>()
        for (i in 0 until M) {
            for (j in 0 until M) {
                if ((4 * i.toDouble().pow(3).toInt() + 27 * j.toDouble().pow(2).toInt()) % M != 0) {
                    availableParams.add(Pair(i, j))
                }
            }
        }
        return availableParams
    }
}