**Brittney Ferrian**
**Myles Gavic**
**Sam Miller**

**Csci 4553: Evolve a program**
**Markdown Report**

For our proble we chose Make Chocolate which is a logic puzzle drawn from the intermediate logic section on the website [codingbat.com]
(http://codingbat.com/prob/p191363)

*"We want make a package of goal kilos of chocolate. We have small bars (1 kilo each) and big* 
*bars (5 kilos each). Return the number of small bars to use, assuming we always use big bars* 
*before small bars. Return -1 if it can't be done."*

*makeChocolate(4, 1, 9) → 4*
*makeChocolate(4, 1, 10) → -1*
*makeChocolate(4, 1, 7) → 2*

This problem was chosen because it shares a few properties with the example problem
*“squirrel play”*, as they are both from the logic sections of codingbat.  We also 
chose this problem because it has simple logic but would be more difficult to solve 
than *“squirrel play”* according to the difficulty ratings on codingBat (224.0 vs. 109.0). 
We wanted to see if we could push the program evolution framework to solve a slightly harder problem.

###Set-up:
We first created an input set that was more applicable to our problem, this was initially 
done by adapting the test cases used for the problem on codingbat, but as we began running 
the program, we needed to add a number of test cases that are expected to return a larger 
variety of numbers, as the codingBat tests were limited in this respect to the ones showed above.

We then had to implement a solution to the problem in clojure for the *“expected-output”* function 
so our evolved problem had something to compare it’s answers to.
```
(defn expected-output
   [inputs]
   (let [[x y z] inputs
     barsLeft (- z (* 5 (min y (/ z 5))))]
      (if (<= barsLeft x) barsLeft
      (if (> barsLeft x) -1))))
```  
*******************************************
Possible out put???
*******************************************
 
###Results:
Initially, after some tuning we got our problem setup to evolve a program that returned only 
one non-zero error for the last 200 or so generations. The error map for the final generation of
a run using this setup was as follows:	
*Error Map: [0 0 0N 0 0N 0N 0 0 0N 0N 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0]* 

Unfortunately, while this solution program got very close to completely solving all test
cases and seemed to be using a combination of instructions that resembled our 
expected-output function, it turned out that the expected-output function had a flaw so
some of the test cases were being improperly evaluated. After fixing the error (by changing 
the operation” \” to “quot”), the expected-output function was working correctly, yet our 
results were no long as close to being solved so further tuning was needed.
```
(defn expected-output
  [inputs]
  (let [[x y z] inputs
    barsLeft (- z (* 5 (min y (quot z 5))))]
    (if (<= barsLeft x) barsLeft
    (if (> barsLeft x) -1))))
```
After fixing expected-output, the best program of the last generation returned 6 non-zero 
errors as follows:										
*Error Map: [0 3 0 0 0 0 0 1 0 0 0 1 0 0 1 2 0 0 0 0 0 0 0 0 0 0 1]*

While this wasn’t terrible, it still proved to be frustrating as it had got closer before. This 
was supposed to be a more difficult program to solve, so it may be beyond the scope of running 
1000 generations in our clojush implementation at this point. 
One thing to note however is that the program that returned this error map appeared to not 
use the integer 5 anywhere, which should be required to give workable answers, so if we were 
able to have the program keep using 5, success may have been in our reach.


###You should make sure you document any tweaks or changes
After it was brought to our attention that the codingBat tests all evaluated to similar 
numbers, we added more test cases that are expected to evaluate to a larger range of 
numbers so the program doesn’t just guess, it actually has to evolve a working solution.

In addition to adding test cases with a greater range of expected answers, we also added 
test cases with a greater combination of ways to get similar numbers. For example, 
consider two test cases : [140 3 16] and [1 25 126], according to our problem, both cases 
will only need one of the small bars (the first input) to reach the goal (the third input), 
however one case has to use a large number of the 5 kilogram bars to reach the goal 
while the other doesn’t. Both cases evaluate to 1, so technically the computer could guess, 
but as previously mentioned we already added a number of tests to combat guessing, so
the program needs to evolve the functionality to use two ways to get the the same number 
in order to solve both cases.

