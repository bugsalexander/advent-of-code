# day 5

I've been working on this for about a week now, maybe 30 minutes a day, and I'm definitely having more trouble with this one than the others.

## conundrums

### reading line N from stdin

I/O took me a while to do in a way that satisfied me. I ended up choosing a design that I knew would do what I wanted it to do, over a design that I wasn't sure what it would do (in hindsight, perhaps I should have tested it out).

googling yielded few satisfying results. most of them were something alone the lines of:

```rust
for line in std::io::stdin().lock().lines() {}
```

... but I wasn't sure if this would run for _all_ lines in stdin, which wasn't what I wanted to do. so instead, I opted to iterate over N lines until I reached the one that I wanted, then returning that singular line. Of course this is quite inefficient, but I went with it anyway.

### parsing parameter modes

this is a huge pain. notice I said "is". this is because I have NOT FINISHED IT YET.

I have to parse the right two digits of a string, but the leftmost of the right two might not exist, and then from there, parse the rest travelling from the right. It's a huge pain. How do you concatenate the two strings? How do we know if there is a next string? It's a huge pain.

However, writing this out made me realize that it might be possible to perhaps try and slice off the last two bits, conditionally, and then parse those and pass the rest as param modes, so I'll try that and get back to us.

Update 1: It worked. In the process, I realized that because usize is unsigned, subtracting 1 from usize(0) results in overflow, which panics the thread. So I had to do a bunch of really ugly checks instead.

Update 2: I was able to fix the above by instead pattern matching over slices instead, which got rid of all the length checks. This made it much nicer.

### code debt

a shit design really led to a lot of code debt on this one. Initially when I had written the intcode computer, I didn't bother to figure out how to convert from usize to i32, so I simply made the computer registers store usizes, even though that was semantically incorrect. This came to bite me, as day5 added negative numbers (usize -> unsigned -> no negatives). Subtracting 1 from a usize 0 produces an overflow -> panic, see the above.

I also tried doing incremental design, first building in opcodes 3+4, and then refactoring to allow for parameter modes, which resulted in a lot of unneeded work. If I had instead tried to think of a design that would work from the get-go, I could have avoided a few bad decisions.

Moral of the story: try to spend more time making good design choices to avoid future code debt.
