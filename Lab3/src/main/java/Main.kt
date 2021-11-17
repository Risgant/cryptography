import kotlin.math.sqrt

fun main(args: Array<String>) {
    val M = args[0].toInt()
    if(!isPrime(M)) {
        println("$M is not prime")
        return
    }
    val paramsGenerator = ParamsGenerator(M)
    val availableParams = paramsGenerator.findParams()
    val paramIdx = (Math.random() * availableParams.size).toInt()
    val param = availableParams[paramIdx]
    val a = param.first
    val b = param.second
    println("a: $a, b: $b")
    val groupGenerator = GroupGenerator(a, b, M)
    val availablePoints = groupGenerator.findPoints()
    val pointIdx = (Math.random() * availablePoints.size).toInt()
    val point = availablePoints[pointIdx]
    println("G: $point")
    val keysGenerator = KeysGenerator(a, b, M)
    val degree = keysGenerator.findDegree(point)
    println("c: $degree")
    var privateA: Int
    var publicA: Pair<Int, Int>
    while(true) {
        privateA = (Math.random() * (degree - 1)).toInt()
        publicA = keysGenerator.generateKey(privateA, point)
        if(publicA.second != 0) {
            break
        }
    }
    println("n_a: $privateA")
    println("Pa: $publicA")
    var privateB: Int
    var publicB: Pair<Int, Int>
    while(true) {
        privateB = (Math.random() * (degree - 1)).toInt()
        publicB = keysGenerator.generateKey(privateB, point)
        if(publicB.second != 0) {
            break
        }
    }
    println("n_b: $privateB")
    println("Pb: $publicB")
    val keyAB = keysGenerator.generateKey(privateA, publicB)
    println("key n_aPb: $keyAB")
    val keyBA = keysGenerator.generateKey(privateB, publicA)
    println("key n_bPa: $keyBA")
    if(keyAB != keyBA) {
        println("FAIL")
        return
    }
}

fun isPrime(n: Int): Boolean {
    if(n == 2) {
        return true
    }
    if(n % 2 == 0) {
        return false
    }
    for(i in 3..sqrt(n.toDouble()).toInt() step 2) {
        if(n % i == 0) {
            return false
        }
    }
    return true
}