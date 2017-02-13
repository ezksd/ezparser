package parser.evaluator;

import ezksd.Result;
import org.junit.Assert;
import org.junit.Test;


public class EvaluatorTest {
    @Test
    public void test() {
        Result<Integer> r = new Evaluator().evaluate("1+2*3-4/(1+1)");
        Assert.assertTrue(r.isSucess());
        Assert.assertEquals(Integer.valueOf(5), r.get());
    }

}