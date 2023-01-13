# OOP-Assignment-2
 Part 1 Ex 5:
 We can see that in 'run1', in which we created one file, using indevidual threads beat just running a for loop while a threadpool took a lot longer than both, we deduce it is due to the time it takes to set up the threadpool. If wso we can assume threadpool would show greater results in a begger test that requires many threads.
 In 'run2' we tested for 10,000 files. We saw the simple for loop performing poorly and the use of threads help dramatically. And like we assumed we saw threadpool perform even better than the single threads!
