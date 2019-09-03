# Binary-Tree-Printer

Current solution prints the nodes with integer key. 

<i>Please note</i> that this repo is auxiliary and it was created to demonstrate the visitor of https://repl.it/@LordOfAlg how the binary tree changes after deleting or inserting a node. 

First method:
```java
BinaryTreePrinter.print(root);
```
only prints the nodes of the binary tree whose keys belong to the interval `[0,99]`. Here is an example of output:
```
              80
              /\
             /  \
            /    \
           /      \
          /        \
         /          \
        /            \
      44               7
      /\               \
     /  \               \
    /    \               \
  11      16              15
  /\                      /\
10  12                   1   4
```
We made the limitation for the maximum level of the binary tree to 6 (if it is bigger than 6 it becomes hard to observe the whole tree).

Example of printed binary tree with 6 levels:
```
                              20
                              /\
                             /  \
                            /    \
                           /      \
                          /        \
                         /          \
                        /            \
                       /              \
                      /                \
                     /                  \
                    /                    \
                   /                      \
                  /                        \
                 /                          \
                /                            \
               7                              40
              /\                              /\
             /  \                            /  \
            /    \                          /    \
           /      \                        /      \
          /        \                      /        \
         /          \                    /          \
        /            \                  /            \
       2               9              22              70
                      /\
                     /  \
                    /    \
                   8      15
                          /\
                        14  17
                           1618
```
If the size of printed binary tree is not a problem, another method can be used:
```java
BinaryTreePrinter.print(root, NumberDigit.FourDigit, 10);
```
Here `NumberDigit.FourDigit` indicates that the key of the node may belong to `[0, 9999]` and the maximum level is 10. 
Internally
```java
BinaryTreePrinter.print(root);
```
calls
```java
BinaryTreePrinter.print(root, NumberDigit.DoubleDigit, 6);
```
