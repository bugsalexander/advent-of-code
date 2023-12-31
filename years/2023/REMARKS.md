## 2023

the year i actually get further than day 4?

### day 5

part 2 was quite tricky. i ended up completely bruteforcing it with a super naive approach.
it's been about 12 minutes so far, and 8/10 tasks are completed. so it will probably take around
20 minutes total.

jk it took 52 mins. slowed down a lot at the end, not sure why

### day 10 part 2

this was the first algorithmically tricky one. my initial instinct after finishing part1, was to
try and re-use the loop somehow to calculate area contained in the loop.

i was trying to go in slices of the loop, starting/stopping a count as we iterate through the
slice of the loop. unfortunately, I don't believe this actually is possible, and after trying
it out, i gave up.

i was trying to avoid having to just do floodfill with some math, but that ended up being the way
that i solved the problem.

this was by far the most time I've spent so far on a problem, half due to getting caught up in
code refactors, but also I spent too much time trying to make the first way work.

around 2-3 days spent total, working a couple hours a day? yeesh.

### day 12 part 1

sort of a pain to program, lots of edge cases.
most thorough requirement of testing/bugfixing so far.

### day 12 part 2

of course i tried to make my solution multithreaded first :P but after that didn't work, and
i just converted it to dp. thankfully i had already sort of structured it in a dp-compatible
manner. the conversion was straightforward, and after switching to biginteger (and adding ?
in between the duplicated spring input) the solution worked.

overall spent way less time than day 10 part 2. only took around an hour.

### day 13

sort of feels like cheating, but to speed it up a bit, i hash the entire row/colum to avoid checking stuff lmao.
tbh i'm not even sure if this speeds it up since I did this preemptively, so I'm curious how slow it'd be if i didn't hash

also, to skip having to refactor everything to biginteger for day2, I've been trying to predict whether or not I'll need it when
writing the solution for day1. however I feel like I'm basically coinflipping (today I used it, but definitely did not need it).

### day 14

definitely taking advantage of hashcode XD

I am noticing the challenges are getting significantly more difficult.

### day 15

- part1: 13:44
- part2: 38:25

I decided I'm going to start recording my times, for fun (since this is long after AOC ended).

This one was pretty straightforward, implementing an algorithm handed to us.

### day 16

- part1: 73m
- part2: ~5m

Pretty straightforward solution with DFS and a 3D array to memoize results. Didn't spend much time on design,
just sort of had an idea that ended up working out. Took a while to implement.

Part 2 was very satisfying/simple to implement, since the solution from part1 was already optimized.

### day 17

- part1: 3 days???

First, tried finding any way to algorithmically solve this. Day 2, after writing down that we need to keep track of
"potential solutions" and "shortest distances", realized that this is an application of Dijkstra's algorithm.

Figured out pretty fast how to transform the input graph and 3-in-a-row same direction limitation into a single directed
graph. Spent the rest of the day here and there implementing Dijkstra's.
