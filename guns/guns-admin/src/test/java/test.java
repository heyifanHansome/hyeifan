import com.stylefeng.guns.modular.lijun.util.LocationUtils;
import org.junit.Test;

public class test {
    @Test
    public void numberFormat(){
        double a=LocationUtils.getDistance(39.904030,116.407526,26.647661,106.630153);
        System.err.println(a);
    }
}
