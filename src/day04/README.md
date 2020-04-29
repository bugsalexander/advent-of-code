# Day 04 – password cracking

**Edit: I didn't use any of these ideas. Skip to the bottom if you want to get to the end.**

input: 240298-784956. how many passwords within the range meet the criteria?

Constraints:

- always increasing digits
- two digits repeat, at least once

## Notes

our starting number is `240298`. however, one of the constraints is that we have always increasing or equal digits, meaning the smallest number the password could be is `244444`.

### Idea 1

one possible strategy is assuming the first few digits are set: 244xxx. the remaining can be whatever. this has 1000 possible configurations. we could then say alright, now do the same for 255xxx. however, there are some problems, like we would double count when moving down the two repeating digits to further in the password, and also we aren't accounting for the always increasing digits criteria.

yeah, this ain't gonna work.

### Idea 2

Instead, let's let the code do the work. each digit has a min value, and a max value. the rest of the digits depend on whether or not there has been a repeat digit yet, and also what value the previous digit is (we must be >= it).

then the answer is something like "assume digit 1 is 2 -> calculate the rest. assume digit 1 is 2 -> calculate the rest. ... sum up the values, and return.

cool, this seems like it will work. we either do or do not increase ... ah, there's a problem. _we either do or do not increase_, meaning that each digit _does not have a min/max value_. darn.

### Idea 3

let's split possibilities into two kinds: either we have a normal range, like 000-999, or we have a bounded range, like 247-555. these normal ranges are _easy_ to calculate, because they can be done so with a formula. the bounded ranges however can be broken down into sums of normal and bounded ranges, though. let's call P our function that returns the # of possible permutations without repeats, and R for with repeats.

R[247-555] =

this is hard i give up

### The Solution

Sometimes, the best solution isn't the most elegant one. It's the one that works.

I initially wrote an oracle intending to use it for automated testing, but it turns out that the oracle was fast enough to be used for the actual solution. It runs in less than half a second.
