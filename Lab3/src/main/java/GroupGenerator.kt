import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class GroupGenerator {
    private int a;
    private int b;
    private int M;

    public List<Pair<Integer>> findPoints() {
        var point = new ArrayList<Pair<Integer>>();
        for(int x = 0; x < M; ++x) {
            int yy = f(x) % M;
            var roots = getRoots(yy);
            for(int y : roots) {
                point.add(new Pair<>(x, y));
            }
        }
        return point;
    }

    private List<Integer> getRoots(int x) {
        var roots = new ArrayList<Integer>();
        for(int i = 0; i < M; ++i) {
            if(Math.pow(i, 2) % M == x) {
                roots.add(i);
            }
        }
        return roots;
    }

    private int f(int x) {
        return (int)Math.pow(x, 3) + a * x + b;
    }
}
