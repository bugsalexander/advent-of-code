# Day 03 Advent of Code

This one was a little more difficult. Here I'll go over some things I learned, and also a bit about how my code works.

## Greedy Closures

I was still in the process of figuring out how borrowing references works with scope, especially with closures. One of the things I do normally is write closures to abstract out repeat code. However, these closures usually have to refer to mutable references. 

The only problem is that closures effectively produce a new scope, and "borrow" the mutable references they refer to. This might not seem so bad, because I can still call the closure a few times, so where's the problem?

After making a few calls (lol), you may want to access the mutable references again. But you can't! They currently belong to the closure. 

The fix to this issue is super simple: introduce a new scope, declare the closure, and make your calls. When you exit the scope, the closure will "give back" the references it borrowed, because its lifetime expired.

## Code Structure

### Part 1

For part 1, my code was pretty simple: parse the strings, convert the directions to a discrete list of positions, stick all the positions of the first path in a set. for each position in the second path, check to see if it's in the set. if it is, then add that posn to a priority queue, sorted by manhattan distance. at the end, pop once to get rid of (0, 0), and pop twice to return.

### Part 2

For part 2, my code got a little bit more complex. The general idea was the same, however. I added a new wrapper class for posns to store their combined distance, and implemented Ord for that struct to sort by the total distance.

* added a `distance` field to Posn, to store how far it is along the path. 
* modified my function that converts from `Vec<Dir>` to `Vec<Posn>` to build them with distance.
* implemented a new struct, `TwoPosn`, which stores two posns, and their combined distance.
* implemented `PartialOrd` for it.
* ... some other things, probably.

I should probably write more tests, but I haven't been punished by bad bugs yet...