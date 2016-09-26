import com.csvreader.CsvReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xinweiwang on 2/7/16.
 */
public class ID3_Util {


    public static TreeNode generateTree(String filename) {

        List<String> attributes = getAttributes(filename);
        List<Data> dataList = getDatas(filename, attributes);
        TreeNode root = helper(dataList, attributes);
        return root;
    }

    private static TreeNode helper(List<Data> dataList, List<String> attributes) {
        TreeNode node = new TreeNode();
        int o = 0;
        int z = 0;
        for (Data data : dataList) {
            if (data.getC() == 1) {
                o++;
            } else if (data.getC() == 0) {
                z++;
            }
        }
        if (o >= z) {
            node.c = 1;
        } else {
            node.c = 0;
        }
        if (attributes.isEmpty()) {
            node.leaf=true;
            return node;
        }

        double initial = classEntropy(dataList);
        if (initial == 0) {
            node.c = dataList.get(0).getC();
            node.leaf=true;
            return node;
        }
        double max = -100;
        String minAttribute = "";
        List<String> removed = new ArrayList<>();
        for (String attribute : attributes) {
            //System.out.println(attribute);
            double aEntropy = attributeEntropy(dataList, attribute);
            if (aEntropy == -10) {
                removed.add(attribute);
                continue;
            }
            //System.out.println(initial-aEntropy);
            if ((initial - aEntropy) > max) {
                max = initial - aEntropy;
                minAttribute = attribute;
            }
        }


        if (minAttribute.equals("")) {
            node.leaf=true;
            return node;
        }
        //System.out.println(minAttribute);
        List<String> newAttribute = new ArrayList<>(attributes);
        newAttribute.remove(minAttribute);
        newAttribute.removeAll(removed);
        List<Data> one = new LinkedList<>();
        List<Data> zero = new LinkedList<>();
        for (Data data : dataList) {
            try {
                if (data.getAttribute(minAttribute)) {
                    one.add(data);
                } else {
                    zero.add(data);
                }
            } catch (Exception e) {
                System.out.println(data);
                //System.out.println(minAttribute);
                e.printStackTrace();
                System.exit(0);
            }

        }
        //System.out.println(minAttribute);
        node.attibute = minAttribute;
        node.oneRight = helper(one, newAttribute);
        node.zeroLeft = helper(zero, newAttribute);

        return node;
    }

    public static TreeNode generateRandomTree(String filename) {

        List<String> attributes = getAttributes(filename);
        List<Data> dataList = getDatas(filename, attributes);
        TreeNode root = randomHelper(dataList, attributes);
        return root;
    }

    private static TreeNode randomHelper(List<Data> dataList, List<String> attributes) {
        TreeNode node = new TreeNode();
        int o = 0;
        int z = 0;
        for (Data data : dataList) {
            if (data.getC() == 1) {
                o++;
            } else if (data.getC() == 0) {
                z++;
            }
        }
        if (o >= z) {
            node.c = 1;
        } else {
            node.c = 0;
        }
        if (attributes.isEmpty()||o==0||z==0) {
            node.leaf=true;
            return node;
        }


        String attribute =null;
        //System.out.println(minAttribute);
        List<String> newAttribute = new ArrayList<>(attributes);
        //newAttribute.remove(attribute);
        List<Data> one = new LinkedList<>();
        List<Data> zero = new LinkedList<>();
        while((!newAttribute.isEmpty())&&(one.isEmpty()||zero.isEmpty())) {
            one = new LinkedList<>();
            zero = new LinkedList<>();
            attribute=newAttribute.get((int)(Math.random()*newAttribute.size()));
            newAttribute.remove(attribute);
            for (Data data : dataList) {
                if (data.getAttribute(attribute)) {
                    one.add(data);
                } else {
                    zero.add(data);
                }
            }
        }
        if(zero.isEmpty()||one.isEmpty()){
            node.leaf=true;
            return node;
        }
        node.attibute = attribute;
        node.oneRight = helper(one, newAttribute);
        node.zeroLeft = helper(zero, newAttribute);

        return node;
    }

    public static void printTree(TreeNode root) {
        printTreeHelper(root, 0);
    }

    private static void printTreeHelper(TreeNode node, int depth) {
        if (node == null) {
            return;
        }
        if(node.leaf||node.attibute==null){
            System.out.println(" "+node.c);

            return;
        }
        System.out.println();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < depth; i++) {
            sb.append("| ");
        }
        String text = "";
        if (node.attibute != null) {
            text += node.attibute;
        }
        System.out.print(sb.toString() + text+" = 0 :");
        printTreeHelper(node.zeroLeft, depth + 1);
        System.out.print(sb.toString() + text+" = 1 :");
        printTreeHelper(node.oneRight, depth + 1);

    }


    private static double attributeEntropy(List<Data> dataList, String attribute) {
        int aone = 0;
        int azero = 0;
        double aone_one = 0;
        double aone_zero = 0;
        double azero_one = 0;
        double azero_zero = 0;
        for (Data data : dataList) {
            if (data.getAttribute(attribute)) {
                aone++;
                if (data.getC() == 0) {
                    aone_zero++;
                } else if (data.getC() == 1) {
                    aone_one++;
                }
            } else {
                azero++;
                if (data.getC() == 0) {
                    azero_zero++;
                } else if (data.getC() == 1) {
                    azero_one++;
                }
            }
        }
        double ig = 0;
        if (aone == 0 || azero == 0) {
            return -10;
        }

        return (double) azero / (double) dataList.size() * caculateEntropy(azero_one, azero_zero, azero) + (double) aone / (double) dataList.size() * caculateEntropy(aone_one, aone_zero, aone);
    }

    private static double classEntropy(List<Data> dataList) {
        double one = 0;
        double zero = 0;
        for (Data data : dataList) {
            if (data.getC() == 1) {
                one++;
            } else if (data.getC() == 0) {
                zero++;
            }
        }
        double ig = caculateEntropy(one, zero, dataList.size());

        return ig;

    }


    private static double caculateEntropy(double one, double zero, int total) {

        double ig = 0;
        if (one != 0 && zero != 0) {
            ig = (zero * -1 / (double) total) * (Math.log(zero / (double) total) / Math.log(2)) - (one / (double) total) * (Math.log(one / (double) total) / Math.log(2));
        } else if ((one == 0 && zero != 0) || (one != 0 && zero == 0)) {
            ig = 0;
        } else {
            throw new RuntimeException("Both of them are zero!" + one + " " + zero + " " + total);
        }
        /*
        if(ig.isNaN()){
            System.out.println(one+" "+zero+" "+total);
        }
        */
        return ig;
    }


    public static List<String> getAttributes(String filename) {
        ArrayList<String> attributes = new ArrayList<>();
        CsvReader reader = null;
        try {
            reader = new CsvReader(filename);
            if (reader.readRecord()) {
                //System.out.println(reader.getColumnCount());
                for (int i = 0; i < reader.getColumnCount() - 1; i++) {

                    attributes.add(reader.get(i));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }


        return attributes;
    }

    public static List<Data> getDatas(String filename, List<String> attributes) {
        List<Data> datas = new LinkedList<>();
        CsvReader reader = null;
        try {
            reader = new CsvReader(filename);
            reader.readRecord();
            while (reader.readRecord()) {
                Data data = new Data();
                for (int i = 0; i < attributes.size(); i++) {
                    data.addAttribute(attributes.get(i), reader.get(i));
                }
                data.setC(Integer.parseInt(reader.get(attributes.size())));
                datas.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }


        return datas;
    }

    public static double testData(List<Data> dataList, TreeNode root) {
        int correct = 0;
        for (Data data : dataList) {
            if (verify(data, root)) {
                correct++;
            }
        }

        return (double) correct / (double) dataList.size();
    }

    public static boolean verify(Data data, TreeNode root) {
        int c = predict(data, root);
        if (data.getC() == c) {
            return true;
        } else {
            return false;
        }
    }


    public static int predict(Data data, TreeNode root) {
        if (root.leaf) {
            return root.c;
        }
        if(root.attibute==null){
            System.out.println(root.leaf);
            return root.c;
        }
        if (data.getAttribute(root.attibute)) {
            return predict(data, root.oneRight);
        } else {
            return predict(data, root.zeroLeft);
        }

    }

    public static int randomDeleteNodes(int num,TreeNode root,List<Data> dataList){
        double max=testData(dataList,root);
        int res=0;
        while(res<num) {
            TreeNode node = randomDeleteNode(root);
            if (node != null) {
                double now = testData(dataList, root);
                if (now > max) {
                    max = now;
                    res++;
                } else {
                    node.leaf = false;
                }
            } else {
                break;
            }
        }
        return res;
    }


    public static TreeNode randomDeleteNode(TreeNode root) {
        List<List<TreeNode>> lists = getNodes(root);
        Iterator<List<TreeNode>> it=lists.iterator();
        while(it.hasNext()){
            List<TreeNode> list=it.next();
            if(list.isEmpty()){
                it.remove();
            }
        }
        if(lists.isEmpty()){
            return null;
        }
        List<TreeNode> list = lists.get((int) (Math.random() * (lists.size())));
        TreeNode n = list.get((int) (Math.random() * list.size()));
        delete(n);
        return n;
    }

    private static void delete(TreeNode node) {
        if (node == null || node.leaf) {
            return;
        }
        node.tested=true;
        node.leaf=true;


    }


    public static List<List<TreeNode>> getNodes(TreeNode root) {
        List<List<TreeNode>> lists = new ArrayList<>();
        getNodesHelper(root, 0, lists);
        return lists;
    }

    private static void getNodesHelper(TreeNode node, int depth, List<List<TreeNode>> lists) {
        if (node == null || node.leaf) {
            return;
        }
        while ((depth + 1) > lists.size()) {
            lists.add(new ArrayList<>());
        }
        List<TreeNode> nodes = lists.get(depth);
        if(!node.tested) {
            nodes.add(node);
        }
        getNodesHelper(node.zeroLeft, depth + 1, lists);
        getNodesHelper(node.oneRight, depth + 1, lists);

    }

    public static int totalNumNode(TreeNode root){
        return totalNumhelper(root);
    }

    private static int totalNumhelper(TreeNode node){
        if(node.leaf){
            return 1;
        }
        return totalNumhelper(node.oneRight)+totalNumhelper(node.zeroLeft)+1;
    }

    public static double avgDepth(TreeNode root){
        List<Integer> list=new ArrayList<>();
        avgDepthHelper(root,0,list);
        int sum=0;
        for(Integer d:list){
            sum+=d;
        }
        return (double)sum/(double)list.size();
    }

    private static void avgDepthHelper(TreeNode node,int depth, List<Integer> list){
        if(node.leaf){
            list.add(depth);
            return;
        }
        avgDepthHelper(node.oneRight,depth+1,list);
        avgDepthHelper(node.zeroLeft,depth+1,list);
    }


}
