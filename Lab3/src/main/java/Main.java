public class Main {
    public static void main(String[] args) {
        for(int i = 0; i < 100; i++) {
            System.out.println(i);
            var M = 71;
            var paramsGenerator = new ParamsGenerator(M);
            var availableParams = paramsGenerator.findParams();
            var paramIdx = (int)(Math.random()*availableParams.size());
            var param = availableParams.get(paramIdx);

            var a = param.getX();
            var b = param.getY();
            System.out.println("a: "+a+", b: "+b);
            var groupGenerator = new GroupGenerator(a, b, M);
            var availablePoints = groupGenerator.findPoints();
            var pointIdx = (int)(Math.random()*availablePoints.size());
            var point = availablePoints.get(pointIdx);
            System.out.println("G: "+point);
            var keysGenerator = new KeysGenerator(a, b , M);
            var degree = keysGenerator.findDegree(point);
            System.out.println("c: "+degree);

            var privateA = (int)(Math.random()*(degree-1));
            System.out.println("n_a: "+privateA);
            var publicA = keysGenerator.generateKey(privateA, point);
            System.out.println("Pa: "+publicA);

            var privateB = (int)(Math.random()*(degree-1));
            System.out.println("n_b: "+privateB);
            var publicB = keysGenerator.generateKey(privateB, point);
            System.out.println("Pb: "+publicB);

            var keyAB = keysGenerator.generateKey(privateA, publicB);
            System.out.println("key n_aPb: "+keyAB);
            var keyBA = keysGenerator.generateKey(privateB, publicA);
            System.out.println("key n_bPa: "+keyBA);
        }

    }
}
