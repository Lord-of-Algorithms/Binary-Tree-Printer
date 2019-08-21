/*
 *  Copyright 2019 (C) github.com/Lord-of-Algorithms
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.util.*;

enum NumberDigit {
    DoubleDigit(2),
    ThreeDigit(3),
    FourDigit(4);

    int digit;

    NumberDigit(int digit) {
        this.digit = digit;
    }
}

final class BinaryTreePrinter {

    private final static int MaxLevel = 6;
    private final static String Blank = " ";
    private final static int MinNodeKeyValue = 0;

    private static int maxNodeKeyValue;
    private static int keyNodeValueDigits;

    private BinaryTreePrinter() {
        // prevent initializing
    }

    /**
     * Prints the binary tree
     *
     * @param root The root of the tree
     */
    static void print(Node root) {
        print(root, NumberDigit.DoubleDigit, MaxLevel);
    }

    /**
     * Prints the binary tree
     *
     * @param root           The root of the tree
     * @param keyNumberDigit The number digit of the key
     * @param maxTreeLevel   The max tree level
     */
    static void print(Node root, NumberDigit keyNumberDigit, int maxTreeLevel) {
        if (root == null) {
            System.out.println("The tree is empty.");
            return;
        }
        keyNodeValueDigits = keyNumberDigit.digit;
        calculateValidMaxNodeKeyValue(keyNumberDigit);

        int maxLevel = getTreeMaxLevel(root);
        if (maxLevel > maxTreeLevel) {
            throw new RuntimeException(
                    "\n\n\n" +
                            "==========================================" +
                            "\nMax level of the binary tree that can be printed is "
                            + maxTreeLevel + ". Current level is " + maxLevel +
                            ".\n==========================================\n\n\n");
        }
        if (maxLevel == maxTreeLevel) {
            // We reduce the length of edges connecting the nodes. It doesn't change the structure of the tree,
            // but makes the printed tree smaller. We do it only for edge case, when level of tree equals to maxTreeLevel.
            maxLevel -= 1;
        }

        var levelMap = new LinkedHashMap<Node, Integer>(); // position plays a role
        int level = 0;
        fillLevelMap(root, level, levelMap);

        var nodeStartMarginMap = new HashMap<Node, Integer>();
        fillNodeStartMarginMap(root, levelMap, true, 0, false, nodeStartMarginMap, maxLevel - 1);
        printBinaryTree(root, maxLevel - 1, nodeStartMarginMap, levelMap);
        System.out.println();
    }

    private static void calculateValidMaxNodeKeyValue(NumberDigit keyNumberDigit) {
        switch (keyNumberDigit) {
            case ThreeDigit:
                maxNodeKeyValue = 999;
                break;
            case FourDigit:
                maxNodeKeyValue = 9999;
                break;
            default:
                maxNodeKeyValue = 99;
        }
    }

    private static int getTreeMaxLevel(Node node) {
        if (node == null)
            return 0;
        return Math.max(getTreeMaxLevel(node.left), getTreeMaxLevel(node.right)) + 1;
    }

    /**
     * Associates each node with its level in the binary tree.
     */
    private static void fillLevelMap(
            Node root,
            int level,
            Map<Node, Integer> levelMap
    ) {
        if (root != null) {

            if (root.key < MinNodeKeyValue || root.key > maxNodeKeyValue) {
                throw new RuntimeException("\n\n\n" +
                        "==========================================" +
                        "\nThe key of the node must be >= "
                        + MinNodeKeyValue + " and <= " + maxNodeKeyValue +
                        "\n==========================================\n\n\n");
            }


            fillLevelMap(root.left, level + 1, levelMap);
            levelMap.put(root, level);
            fillLevelMap(root.right, level + 1, levelMap);
        }
    }

    /**
     * Calculates startMargin for each node (in pre-order way) and associate this value
     * with this node by using a map. Later these margins will be used
     * for printing.
     *
     * @param startMargin start - means from the left (to not mix with left child. There is no connection between these terms).
     */
    private static void fillNodeStartMarginMap(
            Node root,
            Map<Node, Integer> nodeLevelMap,
            boolean isRoot,
            int startMargin,
            boolean isLeftChild,
            Map<Node, Integer> nodeStartMarginMap,
            int maxLevel) {

        if (root != null) {
            int nodeLevel = nodeLevelMap.get(root);
            // Each level has its own space between nodes
            int spaceBetweenNodes = keyNodeValueDigits * ((int) Math.pow(2, (maxLevel - nodeLevel + 1)) - 1);

            if (isRoot) {
                startMargin = keyNodeValueDigits * ((int) Math.pow(2, (maxLevel - nodeLevel)) - 1);
            } else if (isLeftChild) {
                // for left child the margin is smaller
                startMargin = startMargin - spaceBetweenNodes / keyNodeValueDigits - keyNodeValueDigits / 2;
            } else {
                // for right child the margin is bigger
                startMargin = startMargin + spaceBetweenNodes / keyNodeValueDigits + keyNodeValueDigits / 2;
            }

            nodeStartMarginMap.put(root, startMargin);

            fillNodeStartMarginMap(root.left, nodeLevelMap, false, startMargin, true, nodeStartMarginMap, maxLevel);
            fillNodeStartMarginMap(root.right, nodeLevelMap, false, startMargin, false, nodeStartMarginMap, maxLevel);
        }
    }

    /**
     * Uses breadth-first traversal to print the nodes
     */
    private static void printBinaryTree(
            Node root,
            int maxLevel,
            Map<Node, Integer> startMarginMap,
            Map<Node, Integer> levelMap
    ) {

        if (root != null) {
            Queue<Node> queue = new LinkedList<>();
            queue.add(root);
            var currentLevel = 0;
            var currentPosition = 0;

            while (!queue.isEmpty()) {
                Node node = queue.remove();

                // Get the level and margin from the maps for given node.
                int startMargin = startMarginMap.get(node);
                var level = levelMap.get(node);

                if (currentLevel != level) {
                    // New level - go to next line
                    System.out.println();

                    var edgeHeight = Math.pow(2, maxLevel - currentLevel) - 1;
                    for (int i = 0; i < edgeHeight; i++) {
                        printEdges(currentLevel, startMarginMap, levelMap, i);
                    }

                    currentLevel = level;
                    currentPosition = 0;
                }

                // Because each node in the level is printed not from 0 position
                // we need to subtract node's startMargin with current position.
                int spacesBetweenNodes = startMargin - currentPosition;
                for (int i = 0; i < spacesBetweenNodes; i++) {
                    System.out.print(Blank);
                    currentPosition++;
                }
                printNode(node);
                // Remember that each number occupies keyNodeValueDigits digits. So
                // we need to increase the current position on this value.
                currentPosition += keyNodeValueDigits;

                if (node.left != null) {
                    queue.add(node.left);
                }

                if (node.right != null) {
                    queue.add(node.right);
                }
            }
        }
    }

    /**
     * Prints the value of the node's key.
     * <p>
     * Here we adjust the "x-position" of the value to get the good looking view.
     */
    private static void printNode(Node node) {
        var key = node.key;
        if (keyNodeValueDigits == NumberDigit.DoubleDigit.digit) {
            if (key < 10) {
                System.out.print(Blank + key);
            } else {
                System.out.print(key);
            }
        } else if (keyNodeValueDigits == NumberDigit.ThreeDigit.digit) {
            if (key < 10) {
                System.out.print(Blank + key + Blank);
            } else if (key < 100) {
                System.out.print(key + Blank);
            } else {
                System.out.print(key);
            }
        } else if (keyNodeValueDigits == NumberDigit.FourDigit.digit) {
            if (key < 10) {
                System.out.print(Blank + key + Blank + Blank);
            } else if (key < 100) {
                System.out.print(Blank + key + Blank);
            } else if (key < 1000) {
                System.out.print(key + Blank);
            } else {
                System.out.print(key);
            }
        }
    }

    private static void printEdges(
            int currentLevel,
            Map<Node, Integer> startMarginMap,
            Map<Node, Integer> levelMap,
            int iteration
    ) {
        // Get the list of nodes for each currentLevel
        var levelNodeKeys = new ArrayList<Node>();
        for (Map.Entry<Node, Integer> entry : levelMap.entrySet()) {
            var nodeKey = entry.getKey();
            var level = entry.getValue();
            if (level == currentLevel) {
                levelNodeKeys.add(nodeKey);
            }
        }

        var spaceBetweenSlashAndBackSlash = new StringBuilder();
        // The space between slash and backslash increases on two positions each iteration
        spaceBetweenSlashAndBackSlash.append(Blank.repeat(Math.max(0, 2 * iteration)));

        int currentPosition = 0;
        for (Node node : levelNodeKeys) {
            int startMargin = startMarginMap.get(node);
            int slashPosition = startMargin - currentPosition - iteration + (keyNodeValueDigits / 2 - 1);

            // Starting from the left border we print the blanks
            for (int i = 0; i < slashPosition; i++) {
                System.out.print(Blank);
                currentPosition++;
            }

            // If the node doesn't have a child, we replace the child's place with a blank.
            String slash = node.left != null ? "/" : Blank;
            var backSlash = node.right != null ? "\\" : Blank;

            System.out.print(slash + spaceBetweenSlashAndBackSlash + backSlash);
            currentPosition += 2 * (iteration + 1);
        }
        System.out.println("");
    }
}
