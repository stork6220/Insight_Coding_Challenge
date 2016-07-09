1. Java was used for this problem. I have created a jar file in the root directory, which can be called by run.sh. I also modified the run.sh in the root directory and run_tests.sh in the insight_testsuite folder, so they are now compatible with my java code.
2. I used an external libray for JSON parsing. The library was downloaded from here: http://www.java2s.com/Code/JarDownload/java-json/java-json.jar.zip
3. The source code consisted two major parts:
	Part A. CalcNode.java and Calculator.java 
	In this part, I calculated the median values by putting the (users, frequency) into two TreeSet, which are sorted by the frequency of user appearance. The two TreeSets have the same length if the total users are even number, and left TreeSet.size() == right TreeSet.size() + 1 if users are odd number. TreeSet is a balanced binary search tree, and the average update time complexity is O(logn), n is the number of users in set.    
	
	B. Edge.java, TimeUnit.java and MedianDegree.java
	In this part, I used a circular array to record the graph with 60 seconds. Whenever time advanced, I scanned from the tail and got rid of the timeout relationships. When a coming relationship is within the 60s window, I update the graph. However, not every update of the graph will cause a calculation of average median degree. Because same payments between two people can happen more than once. I only update the median degree when there is a new relationship or removed relationship. To do this, I used a count map to keep the record of the occurrence of each relationship. 
4. The code also contained some JUnit test to make sure the JSON parsing and median update process are correct.  
