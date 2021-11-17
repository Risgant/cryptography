import lombok.AllArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ParamsGenerator {
    private int M;

    public  List<Pair<Integer>> findParams() {
        var availableParams = new ArrayList<Pair<Integer>>();
        for(int i = 0; i < M; ++i) {
            for(int j = 0; j < M; ++j) {
                if(BigInteger.valueOf(4L * (int)Math.pow(i, 3) + 27L * (int)Math.pow(j, 2)).mod(BigInteger.valueOf(M)).intValue() != 0) {
                    availableParams.add(new Pair<Integer>(i, j));
                }
            }
        }
        return availableParams;
    }
}
