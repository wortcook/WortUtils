# WortUtils
Set of Java utility classes for Java 11 and above.

[CircularListIterator](https://github.com/wortcook/WortUtils/blob/main/src/main/java/com/wortcook/util/CircularListIterator.java) is a ListIterator that wraps around a List and allows for circular iteration.
 Depending on the constructor, the iterator can start at a specific index and iterate through the list
 or start at the beginning of the list. Once the iterator reaches the end of the list it will wrap around
 to the beginning of the list and vice versa. So unlike a regular ListIterator, the CircularListIterator
 will never throw a NoSuchElementException unless the list is empty or the maximum number of steps is reached.
  
