import java.math.BigInteger


fun main(args: Array<String>) {
    println("-----------------User A----------------")
    val M = args[0].toInt()
    val message = args.copyOfRange(1, args.size).joinToString(" ")
    if(!isPrime(M)) {
        println("$M is not prime")
        return
    }
    println("message: $message")
    val paramsGenerator = ParamsGenerator(M)
    val availableParams = paramsGenerator.findParams()
    var a: Int
    var b: Int
    var q: Int
    var keysGenerator: KeysGenerator
    var G: Pair<Int, Int>
    while(true) {
        val paramIdx = (Math.random() * availableParams.size).toInt()
        val param = availableParams[paramIdx]
        a = param.first
        b = param.second
        val groupGenerator = GroupGenerator(a, b, M)
        val availablePoints = groupGenerator.findPoints()
        keysGenerator = KeysGenerator(a, b, M)
        var i = 0
        var found = false
        while(true) {
            val pointIdx = (Math.random() * availablePoints.size).toInt()
            G = availablePoints[pointIdx]
            q = keysGenerator.findDegree(G)
            if(isPrime(q) && q > M/2) {
                found = true
                break
            }
            if(i > 1000) {
                break
            }
            ++i
        }
        if(found) {
            break
        }
    }
    println("a: $a, b: $b")
    println("G: $G")
    println("q: $q")
    var privateA: Int
    var publicA: Pair<Int, Int>
    while(true) {
        privateA = (Math.random() * q + 1).toInt()
        publicA = keysGenerator.generateKey(privateA, G)
        if(publicA.second != 0) {
            break
        }
    }
    println("n_a: $privateA")
    println("Pa: $publicA")
    val h = sha1Hash(message)
    println("h: $h")
    var k: Int
    var r: Int
    var s: Int
    while(true) {
        k = (Math.random() * (q - 1) + 1).toInt()
        r = keysGenerator.generateKey(k, G).first % q
        if(r != 0) {
            s = ((BigInteger.valueOf(k.toLong()).pow(q-2) * (h + BigInteger.valueOf((privateA * r).toLong()))) % BigInteger.valueOf(q.toLong())).toInt()
            if(s != 0) {
                break
            }
        }
    }
    println("k: $k")
    val sign = Pair(r, s)
    println("sign: $sign")
    println("-----------------User B----------------")
    if(sign.first <= 0 || sign.first >= q || sign.second <= 0 || sign.second >= q) {
        println("sign is invalid")
        return
    }
    val h_ = sha1Hash(message)
    println("h: $h_")
    val w = BigInteger.valueOf(sign.second.toLong()).pow(q-2) % (BigInteger.valueOf(q.toLong()))
    println("w: $w")
    val u1 = h_.multiply(BigInteger.valueOf(w.toLong())) % BigInteger.valueOf(q.toLong())
    println("u1: $u1")
    val u2 = BigInteger.valueOf(sign.first.toLong()).multiply(BigInteger.valueOf(w.toLong())) % BigInteger.valueOf(q.toLong())
    println("u2: $u2")
    val p1 = keysGenerator.generateKey(u1.toInt(), G)
    println("p1: $p1")
    val p2 = keysGenerator.generateKey(u2.toInt(), publicA)
    println("p2: $p2")
    val r_ = keysGenerator.sumPoints(p1, p2).first % q

    println("r*: $r_")
    if(r_ != r) {
        println("sign is invalid")
        return
    }
}