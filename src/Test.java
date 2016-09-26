import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinweiwang on 3/21/16.
 */
public class Test {

    public static void main(String[] args){

        List<Integer> list=new ArrayList<>();
        list.add(7);
        list.add(10);
        list.add(15);
        System.out.println(list);
        list.remove(2);
        System.out.println(list);
        list.add(2,15);
        System.out.println(list);
        Integer[] arr=list.toArray(new Integer[list.size()]);
        for(Integer i:arr){
            System.out.println(i);
        }


    }

}
