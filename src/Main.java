import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

/**
 * Created by xinweiwang on 2/7/16.
 */
public class Main {

    public static void main(String[] args) {
        if(args.length<6){
            System.out.println("please enter <type 0/1> <prune number> <traning file> <validate file> <test file> <print 0/1>");
            System.exit(0);
        }
        int type=Integer.parseInt(args[0]);

        int num=Integer.parseInt(args[1]);
        String traningFile=args[2];
        String validateFile=args[3];
        String testFile=args[4];
        int print=Integer.parseInt(args[5]);

        List<String> attributes=ID3_Util.getAttributes(traningFile);
        /*
        for(String name:attributes){
            System.out.println(name);
        }
        */
        //List<Data> trainingData=ID3_Util.getDatas(traningFile,attributes);
        /*
        for(int i=0;i<10;i++){
            System.out.println(dataList.get(i));
        }
        */

        //System.out.println(Math.log(8)/Math.log(2));

        TreeNode root = null;

        if(type==0) {
            root=ID3_Util.generateTree(traningFile);
        }else if(type==1){
            root=ID3_Util.generateRandomTree(traningFile);
        }else{
            System.out.println("please enter <type 0/1> <prune number> <traning file> <validate file> <test file> <print 0/1>");
            System.exit(0);
        }
        //ID3_Util.printTree(root);
        //ID3_Util.printTree(root);
        //List<String> attributes=ID3_Util.getAttributes("validation_set.csv");
        List<Data> validateData=ID3_Util.getDatas(validateFile,attributes);
        List<Data> testData=ID3_Util.getDatas(testFile,attributes);
        //ID3_Util.printTree(root);
        System.out.println();
        System.out.println("Result without pruning:");
        double r1=ID3_Util.testData(validateData,root);
        System.out.println("validation: "+r1);
        double r2=ID3_Util.testData(testData,root);
        System.out.println("test: "+r2);
        System.out.println("Total Num Node: " + ID3_Util.totalNumNode(root));
        System.out.println("Avg Depth: " + ID3_Util.avgDepth(root));
        //List<List<TreeNode>> lists=ID3_Util.getNodes(root);

        int n=ID3_Util.randomDeleteNodes(num,root,validateData);
        System.out.println();
        System.out.println("Result prune "+num+" nodes"+": "+n);
        double r3=ID3_Util.testData(validateData,root);
        System.out.println("validation: "+r3);
        double r4=ID3_Util.testData(testData,root);
        System.out.println("test: "+r4);
        System.out.println("Total Num Node: " + ID3_Util.totalNumNode(root));
        System.out.println("Avg Depth: " + ID3_Util.avgDepth(root));
        if(print==1){
            ID3_Util.printTree(root);
        }



    }

}
