use std::io::*;

// functions for day1 of advent of code 2019

// with the provided buffer, run with the provided get_fuel func
pub fn calc_fuel<I, F>(lines: I, fuel_calc: F) -> i32
where
    F: Fn(i32) -> i32,
    I: Iterator<Item = Result<String>>,
{
    let mut total = 0;

    // for each line, add the corresponding amt of fuel to total
    for line in lines {
        match line.map(|s| str::parse::<i32>(&s)) {
            Ok(Ok(num)) => total += fuel_calc(num),
            _ => panic!("Error reading number"),
        }
    }

    total
}

// divide by 3, round down, subtract 2
pub fn get_fuel(n: i32) -> i32 {
    (n / 3) - 2
}

// get fuel, but is recursive
pub fn get_fuel_r(n: i32) -> i32 {
    let f = get_fuel(n);
    if f <= 0 {
        return 0;
    } else {
        return f + get_fuel_r(f);
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use std::fs::File;
    use std::io::BufRead;
    use std::io::BufReader;

    #[test]
    fn day1() {
        let rdr = || BufReader::new(File::open("../input/01").unwrap());

        assert_eq!(calc_fuel(rdr().lines(), get_fuel), 3538016);
        assert_eq!(calc_fuel(rdr().lines(), get_fuel_r), 5304147);
    }
}
