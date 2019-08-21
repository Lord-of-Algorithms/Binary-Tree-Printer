public class Demo {
    public static void main(String[] args) {
        var root = new Node(80);
        var node1 = new Node(44);
        var node2 = new Node(11);
        var node3 = new Node(16);
        var node4 = new Node(10);
        var node5 = new Node(12);
        var node6 = new Node(7);
        var node7 = new Node(15);
        var node8 = new Node(1);
        var node9 = new Node(4);

        root.left = node1;
        root.right = node6;
        node1.left = node2;
        node1.right = node3;
        node2.left = node4;
        node2.right = node5;
        node6.right = node7;
        node7.left = node8;
        node7.right = node9;

        BinaryTreePrinter.print(root);
    }
}
