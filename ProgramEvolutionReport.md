**Brittney Ferrian**
**Myles Gavic**
**Sam Miller**

**Csci 4553: Evolve a program**
**Markdown Report**

For our problem we chose one entitled "Make Chocolate" which is a logic puzzle drawn from the intermediate logic section on the website [codingbat.com]
(http://codingbat.com/prob/p191363)

The prompt for the problem is as follows:

Description excerpt from [codingbat.com] (http://codingbat.com/prob/p191363):
*"We want to make a package of goal kilos of chocolate. We have small bars (1 kilo each) and big* 
*bars (5 kilos each). Return the number of small bars to use, assuming we always use big bars* 
*before small bars. Return -1 if it can't be done."*

*makeChocolate(4, 1, 9) → 4*
*makeChocolate(4, 1, 10) → -1*
*makeChocolate(4, 1, 7) → 2*

This problem was chosen because it shares a few properties with the example problem
*“squirrel play”*, as they are both from the logic sections of codingbat.  We also 
chose this problem because it has simple logic but would be more difficult to solve 
than the default problem *“squirrel play”* according to the difficulty ratings on codingBat (224.0 vs. 109.0). 
We wanted to see if we could push the program evolution framework to solve a slightly harder problem.

###Set-up:
To set up the problem, we first created an input set that was more applicable to our particular problem instance, 
this was initially done by adapting the test cases used for the problem on codingbat, but as we began running 
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
Our program evaluates errors by checking to see if the actual-output function returns a value, if not it penalizes it by returning an error of 1000, otherwise it
returns an error by calculating the difference between the expected output and the actual output. The instructions we included for the problem consist of all 
operations related to integers and booleans, as well as the exec_if instruction, the constant 5 (as it is present in every instance of the problem), and the three 
inputs dictated by the problem. These instructions should be all the program needs to evolve a solution as our expected-output function consists of solely variations 
of these instructions. We initially had included more instructions, but had to refine the list as the program seemed to take a suboptimal path immediately.

 
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
After fixing expected-output, the best program of the last generation we ran returned only 2 non-zero 
errors for its final generation of 1001 as follows:										
*Error Map: [0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 4 0 0 0 0 0]

While this wasn’t terrible, it still proved to be frustrating as it is so close to being solved but can't 
seem to clear that final hurdle. This was supposed to be a more difficult program to solve, so it may
be beyond the scope of running 1000 generations in our clojush implementation at this point. 
One thing to note however is that the program that returned this error map appeared to not 
be using the integer 5 consistently, which should be required to give workable answers, so if we were 
able to have the program keep using 5, success may have been in our reach.


###Tweaks/changes made to the program:
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

Adding arguments to the atom-generator section seemed fruitless, so the majority of efforts were focused on running the program to completion as many times as possible
on an increasingly larger input set, as we tried to make the input-set more comprehensive each time. Another frustrating problem that came up was sometimes the program 
would fail to use the integer 5 in its instructions, which the program should need in order to succeed. It would use it sometimes, so it definitely had access to it, 
but it would often seem to ignore it for large sections of generations. Unable to force the program to use it, we just focused instead on giving the right combinations
of inputs to make it realize the importance of that value.

We also limited the atom-generator’s exec instructions to only include exec_if, as the program shouldn’t need any others. This was also done because the problem
kept pulling in exec_k and exec_s, which aren’t conducive to evolving a working solution. As we continued running the program trying to get it to solve, we kind of hit 
a wall in the tuning process. The program already had all the ingredients it needed to form an equivalent function to our expected-output function, so any additional
instructions seemed to take, it in the wrong direction, and mislead it from components it needed. Adding any specific constants seemed pointless, as 5 is the only set one
in the setup, and similarly the program should not need operators for any other data structure other than booleans or integers because those are the only structures the 
setup calls for. Adding other exec instructions like exec_when seemed to have no effect or sometimes even caused the program to stop using exec_if which it should need. 
We tried a number of tweaks but most proved to be of little use to our default setup. Given more time, we probably would have conducted more runs, because it seemed our 
program was close but kept crashing right before the finish line. This lesson was valuable moving forward as we are now aware of potential pitfalls.
