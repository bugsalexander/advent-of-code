#![allow(dead_code)]

use std::fs::read_to_string;

// mod day01;
// mod day02;
mod day03;

fn main() {
    // what pair of inputs produces 19690720?

    let str = read_to_string("./input/03").unwrap();
    let parsed = day03::parse(&str);
    let result = day03::find_closest(parsed.0, parsed.1);

    println!("{:#?}", result);
    println!("{}", result.manhattan());
}
