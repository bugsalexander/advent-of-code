// day 04 of advent of code 2019

mod tests;

/// produces the number of valid passwords for the two bounds
/// panics if they are not the same length or if they don't parse to u32
pub fn oracle(min: &str, max: &str) -> u64 {
    if min.len() != max.len() {
        panic!("bounds were not the same length!");
    }

    let (nmin, nmax) = match (min.parse::<u32>(), max.parse::<u32>()) {
        (Ok(nmin), Ok(nmax)) => (nmin, nmax),
        _ => panic!("one of the bounds didn't parse to u32"),
    };

    let mut count: u64 = 0;

    for n in (nmin..=nmax).map(|n| n.to_string()) {
        if not_decreasing_has_repeat(&n) {
            count += 1;
        }
    }
    count
}

/// does a number have not decreasing digits?
/// does a number have a repeat?
fn not_decreasing_has_repeat(n: &str) -> bool {
    let mut chars = n.chars();
    let mut min = chars
        .nth(0)
        .expect("not_decreasing: number was not at least 1 digit long");
    let mut has_repeat = false;
    let mut count = 1;
    for digit in chars {
        if min < digit {
            min = digit;
            if count == 2 {
                has_repeat = true;
            }
            count = 1;
        } else if min == digit {
            count += 1;
        } else {
            return false;
        }
    }

    has_repeat || count == 2
}
