import org.junit.Test;

public class test {
    @Test
    public void numberFormat(){
        System.out.println(10/3);
        String s=Double.valueOf(10/3).toString();
        System.out.println(s);
    }
}
