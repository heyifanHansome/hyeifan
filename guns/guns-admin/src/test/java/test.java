import com.stylefeng.guns.modular.lijun.util.LocationUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.*;

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

    @Test
    public void ListRemove(){
        List<Map<String,Object>>list1=new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Map<String,Object>map=new HashMap<>();
            map.put("data",i);
            list1.add(map);
        }
        System.err.println(list1);
        Map<String,Object>information=new HashMap<>();
        Iterator<Map<String,Object>> it=list1.iterator();
        while (it.hasNext()){
            System.err.println("--");
            information.put("data",it.next());
            it.remove();
            break;
        }
        System.err.println(information);
        System.err.println(list1);
    }
    @Test
    public void split(){
        String ids="28, ";
        List<String>idss=Arrays.asList(ids.split(","));
        System.err.println(ids.split(",").length);
        System.err.println(idss.size());
    }

    @Test
    public void randomIntZeroOrOne(){
        for (int i=0;i<20;i++)
        System.err.println(new Random().nextInt(2));
    }
}
