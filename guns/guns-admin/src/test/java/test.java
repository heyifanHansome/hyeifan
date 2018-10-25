import com.stylefeng.guns.modular.lijun.util.LocationUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class test {
    @Test
    public void numberFormat(){
        double a=LocationUtils.getDistance(39.904030,116.407526,26.647661,106.630153);
        System.err.println(a);
    }

    @Test
    public void SQL(){
        String[]arg={"id"};
        for (int i = 0; i < arg.length; i++) {
            arg[i]=(" "+arg[i]+" is not null and "+arg[i]+"<>'' ");

        }
        String SQL=StringUtils.join(arg," and ");
        System.err.println(SQL);
    }

}
